package com.example.sivakrishna.firebaseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {
    EditText editemail,editpass;
    Button buttons;
    TextView text1;
    FirebaseAuth firebasea;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        editemail=(EditText)findViewById(R.id.editnames);
        editpass=(EditText)findViewById(R.id.editpasswords);
        buttons=(Button)findViewById(R.id.buttonsignn);
        firebasea=FirebaseAuth.getInstance();
        progress=new ProgressDialog(this);
        text1=(TextView)findViewById(R.id.text1);
        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signin.this,Login.class));
            }
        });
    }

    private void signin() {
        String email1= editemail.getText().toString().trim();
        Log.e("ss",email1);
        String password1= editpass.getText().toString().trim();
        if(TextUtils.isEmpty(email1)){
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password1)){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        progress.setMessage("Registering");
        progress.show();
        firebasea.createUserWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signin.this,"Success",Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            Intent i=new Intent(Signin.this,MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(Signin.this,"failed",Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                        }
                    }
                });

    }
}
