package com.example.sivakrishna.firebaseapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Mainfragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private java.util.List<ImageUpload> imgList;
    FirebaseAuth firebaseAuth;
    private ListView lv;
    private ImageListAdapter adapter;
    private Recycler recyclerviewAdapter;
    private ProgressDialog progressDialog;
    public static final String FB_Database_Path = "image/";
    RecyclerView recyclerView;

    public Mainfragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_mainfragment, container, false);
        imgList = new ArrayList<>();
//        lv = (ListView)view. findViewById(R.id.listViewImage);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        Decorate decorate=new Decorate(getContext());
        recyclerView.addItemDecoration(decorate);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();
        setRetainInstance(true);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_Database_Path+user.getUid());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }
                int i=imgList.size();
                if(i==0){
                  Snackbar snackbar=  Snackbar.make(getView(),"No Images to display",Snackbar.LENGTH_LONG);

                    snackbar.show();
                }else{
                if(getActivity()!=null){
                    recyclerviewAdapter=new Recycler(getActivity(),imgList);
                    recyclerView.setAdapter(recyclerviewAdapter);
}}
           }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });
        return view;
    }

    }


