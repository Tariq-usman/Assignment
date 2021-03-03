package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button btnSignIn;
    private String email, pass;
    Dialog dialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        dialog = Utils.dialog(SignInActivity.this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        etEmail = findViewById(R.id.et_signin_email);
        etPass = findViewById(R.id.et_signin_pass);
        if (currentUser!=null){
            startActivity(new Intent(SignInActivity.this,HomeActivity.class));
            finish();
        }

        findViewById(R.id.tv_click_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                finish();
            }
        });

        btnSignIn = findViewById(R.id.btn_singin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                pass = etPass.getText().toString().trim();

                if (email.isEmpty() || email.equals("")) {
                    etEmail.setError("Enter your email");
                } else if (pass.isEmpty() || pass.equals("") || pass.length() < 8) {
                    etPass.setError("Enter proper Password");
                } else {
                    dialog.show();
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(SignInActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SignInActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}