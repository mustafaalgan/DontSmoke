package com.example.dontsmoke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    TextView fullName, email, phone,smoke;
    FirebaseAuth fAuth;
    String userId;
    FirebaseFirestore fStore;
    Button resetPassLocal, changeProfileImage;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        smoke = findViewById(R.id.editsmoke);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);

        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));


        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(Profile.this, (documentSnapshot, e) -> {
            if (documentSnapshot.exists()) {
                phone.setText(documentSnapshot.getString("phone"));
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
                smoke.setText(documentSnapshot.getString("smoke"));

            } else {
                Log.d("tag", "onEvent: Belge mevcut değil");
            }
        });

        resetPassLocal.setOnClickListener(v -> {

            final EditText resetPassword = new EditText(v.getContext());

            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Şifre Değiştirme");
            passwordResetDialog.setMessage("Yeni şifre >= 8 karekter olmalıdır");
            passwordResetDialog.setView(resetPassword);

            resetPassword.setHint("Yeni Şifre");

            passwordResetDialog.setPositiveButton("Evet", (dialog, which) -> {
                String newPassword = resetPassword.getText().toString();
                Log.i("", newPassword);
                if (newPassword.length() < 6) {
                    Toast.makeText(Profile.this, "Eksik veya Hatalı Şifre", Toast.LENGTH_SHORT).show();

                } else if (newPassword.length() > 6) {

                    user.updatePassword(newPassword).addOnSuccessListener(aVoid ->
                            Toast.makeText(Profile.this, "Şifre Değiştirildi.", Toast.LENGTH_SHORT).
                                    show()).addOnFailureListener(e ->
                            Toast.makeText(Profile.this, "Bir Hata Oluştu.", Toast.LENGTH_SHORT).show());
                }
            });

            passwordResetDialog.setNegativeButton("Hayır", (dialog, which) -> {
            });

            passwordResetDialog.create().show();

        });

        changeProfileImage.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), EditProfile.class);
            i.putExtra("fullName", fullName.getText().toString());
            i.putExtra("email", email.getText().toString());
            i.putExtra("phone", phone.getText().toString());
            i.putExtra("smoke", smoke.getText().toString());
            startActivity(i);

        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        Intent intent = new Intent(Profile.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
