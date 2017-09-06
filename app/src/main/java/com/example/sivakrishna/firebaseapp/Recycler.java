package com.example.sivakrishna.firebaseapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sivakrishna.firebaseapp.Modelclasses.ImageUpload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siva krishna on 9/5/2017.
 */

public class Recycler extends RecyclerView.Adapter<Recycler.viewholder> {
    Context context;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    public static final String FB_Storage_Path = "image/";
    String url;

    java.util.List<ImageUpload> imglist=new ArrayList<>();
    public Recycler(Context context, List<ImageUpload> imgList) {
this.context=context;
        this.imglist=imgList;
   firebaseAuth=FirebaseAuth.getInstance();
    }


    @Override
    public Recycler.viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(Recycler.viewholder holder, final int position) {
       holder. tvName.setText(imglist.get(position).getName());
        Glide.with(context).load(imglist.get(position).getUrl()).into(holder.img);
        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                url=imglist.get(position).getUrl();
                FirebaseUser user=firebaseAuth.getCurrentUser();
                StorageReference ref=FirebaseStorage.getInstance().getReferenceFromUrl(url);
                DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
                Query query=ref1.child("image").child(user.getUid()).orderByChild("url").equalTo(url);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()){
                            appleSnapshot.getRef().removeValue();

                        imglist.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,imglist.size());
                            notifyDataSetChanged();

                            Toast.makeText(context,"removed",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Recycler.this.notifyDataSetChanged();
                        Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return imglist.size();
    }
    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView img;
        ImageView image1;
        public viewholder(View itemView) {
            super(itemView);
             tvName = (TextView) itemView.findViewById(R.id.tvImageName);
            img = (ImageView) itemView.findViewById(R.id.imgView);
             image1=(ImageView)itemView.findViewById(R.id.image1);


        }



    }}
