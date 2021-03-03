package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText etEmail, etPass, etLocation;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    String id, email, pass, location;
    Dialog dialog;
    PreferencesClass preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dialog = Utils.dialog(SignUpActivity.this);

        preference = new PreferencesClass(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        etEmail = findViewById(R.id.et_signup_email);
        etPass = findViewById(R.id.et_signup_pass);
        etLocation = findViewById(R.id.et_location);

        btnSignUp = findViewById(R.id.btn_singup);

        findViewById(R.id.tv_have_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                location = etLocation.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                pass = etPass.getText().toString().trim();

                if (email.isEmpty() || email.equals("")) {
                    etEmail.setError("Enter your email");
                } else if (pass.isEmpty() || pass.equals("") || pass.length() < 8) {
                    etPass.setError("Enter proper Password");
                } else if (location.isEmpty() || location.equals("")) {
                    etLocation.setError("Enter your location");
                } else {
                    dialog.show();
                    auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        UserModel users = new UserModel(email, pass, location);
                                        id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(users);
                                        preference.setUserLocation(users.getLocation());
                                        Log.d("loc", users.getLocation());
                                        dialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                        finish();
                                        ;
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}