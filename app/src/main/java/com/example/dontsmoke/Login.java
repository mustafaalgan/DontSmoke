package com.example.dontsmoke;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn, forgotTextLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);
        forgotTextLink = findViewById(R.id.forgotPassword);


        mLoginBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email gereklidir.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Şifre gereklidir.");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);


            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Başarıyla giriş yapıldı", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            });

        });

        mCreateBtn.setOnClickListener(v -> {


            startActivity(new Intent(getApplicationContext(), Register.class));
            finish();
        });

        forgotTextLink.setOnClickListener(v -> {

            String eMail = mEmail.getText().toString().trim();

            if (eMail.length() > 11) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Şifremi Unuttum");
                passwordResetDialog.setMessage("Email Adresine Şifre Yenileme Linki Gönderilsin mi ?");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Evet", (dialog, which) -> {
                    Toast.makeText(this, "Şifre Yenileme Linki Gönderildi", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().sendPasswordResetEmail(eMail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent");
                                }
                            });
                });

                passwordResetDialog.setNegativeButton("Hayır", (dialog, which) -> {
                });

                passwordResetDialog.create().show();
            } else if (eMail.length() < 10) {
                Toast.makeText(this, "Lütfen Geçerli Bir Email Giriniz", Toast.LENGTH_SHORT).show();
            }


        });


    }
}
