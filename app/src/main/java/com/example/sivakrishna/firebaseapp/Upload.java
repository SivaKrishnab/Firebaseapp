package com.example.sivakrishna.firebaseapp;


import android.*;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Upload extends Fragment {
    FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    ImageView imageView;
    private EditText editText;

    private Uri imguri;
    Button buttonimage, buttonupload, buttonlist, camera;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public static final String FB_Storage_Path = "image/";
    public static final String FB_Database_Path = "image";
    public static final int Request_Code = 1234;
    public static final int Request_Code1 = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(FB_Database_Path);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        editText = (EditText) view.findViewById(R.id.txtImageName);
        firebaseAuth = FirebaseAuth.getInstance();
//        buttonlist = (Button) view.findViewById(R.id.button2);
        buttonimage = (Button) view.findViewById(R.id.button3);
        buttonupload = (Button) view.findViewById(R.id.button4);
        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imguri != null) {
                    final ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setTitle("Uploading image");
                    dialog.show();
                    setRetainInstance(true);
                    final FirebaseUser userid = firebaseAuth.getCurrentUser();

                    StorageReference ref = storageReference.child(userid.getUid()).child(FB_Storage_Path + System.currentTimeMillis() + "." + getImageExt(imguri));


                    ref.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            dialog.dismiss();
                            Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                            ImageUpload imageUpload = new ImageUpload(editText.getText().toString(), taskSnapshot.getDownloadUrl().toString());

                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(userid.getUid()).child(uploadId).setValue(imageUpload);

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    //Dimiss dialog when error
                                    dialog.dismiss();
                                    //Display err toast msg
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    //Show upload progress

                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    dialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), Request_Code);
            }
        });
//        buttonlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(getContext(),List.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imguri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imguri);
                imageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }


    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
