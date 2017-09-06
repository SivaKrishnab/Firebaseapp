package com.example.sivakrishna.firebaseapp.Activities;

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

import com.example.sivakrishna.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText editname,editpassword;
    Button blogin;
    TextView text;
    ProgressDialog progress;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editname=(EditText)findViewById(R.id.editname);
        editpassword=(EditText)findViewById(R.id.editpassword);
        blogin=(Button)findViewById(R.id.buttonlogin);
        firebaseauth=FirebaseAuth.getInstance();
        progress=new ProgressDialog(this);
        text=(TextView)findViewById(R.id.text);
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            login();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Signin.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String email1= editname.getText().toString().trim();
        Log.e("ss",email1);
        String password1= editpassword.getText().toString().trim();
        if(TextUtils.isEmpty(email1)){
            Toast.makeText(Login.this,"Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password1)){
            Toast.makeText(Login.this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }progress.setMessage("Signing In");
        progress.show();
        firebaseauth.signInWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if(task.isSuccessful()){
                            Intent i=new Intent(Login.this,MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(Login.this,"failed",Toast.LENGTH_SHORT).show();
                        }
    }

});
    }}
