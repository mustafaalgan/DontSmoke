package com.example.dontsmoke;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Data_input extends AppCompatActivity {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref;
    String userId, sigara_tut;
    String mytime = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
    TextView textView;
    RadioButton yemekdisi, radio_button_once, kahvalti, radio_button_sonra, icecek_yok, gun_ici;
    RadioButton genderradioButton1, genderradioButton2, genderradioButton3, genderradioButton4;
    RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4;
    Map<String, String> userData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);
        yemekdisi = findViewById(R.id.yemek_disi);
        radio_button_once = findViewById(R.id.radio_button_once);
        radio_button_sonra = findViewById(R.id.radio_button_sonra);
        textView = findViewById(R.id.textView);
        kahvalti = findViewById(R.id.kahvalti);
        icecek_yok = findViewById(R.id.icecek_yok);
        gun_ici = findViewById(R.id.gun_ici);
        gun_ici.setChecked(true);
        icecek_yok.setChecked(true);
        yemekdisi.setChecked(true);
        radio_button_once.setChecked(true);
        radio_button_once.setVisibility(View.INVISIBLE);
        radio_button_sonra.setVisibility(View.INVISIBLE);
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
        radioGroup4 = findViewById(R.id.radioGroup4);
        fStore = FirebaseFirestore.getInstance();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(Data_input.this, (documentSnapshot, e) -> {
            if (documentSnapshot.exists()) {

                sigara_tut = documentSnapshot.getString("smoke");

            } else {
                Log.d("tag", "onEvent: Belge mevcut değil");
            }
        });
    }


    public void yemek_disi(View view) {
        if (radio_button_once.getVisibility() == View.VISIBLE) {
            radio_button_once.setVisibility(View.INVISIBLE);
            radio_button_sonra.setVisibility(View.INVISIBLE);
        }
    }

    public void yemek_ici(View view) {
        if (radio_button_once.getVisibility() == View.INVISIBLE) {
            radio_button_once.setVisibility(View.VISIBLE);
            radio_button_sonra.setVisibility(View.VISIBLE);
        }

    }

    public void veri_gonder(View view) {
        int selectedId1 = radioGroup1.getCheckedRadioButtonId();
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();
        int selectedId3 = radioGroup3.getCheckedRadioButtonId();
        int selectedId4 = radioGroup4.getCheckedRadioButtonId();
        genderradioButton1 = findViewById(selectedId1);
        genderradioButton2 = findViewById(selectedId2);
        genderradioButton3 = findViewById(selectedId3);
        genderradioButton4 = findViewById(selectedId4);
        if (genderradioButton2.getText() == yemekdisi.getText()) {

            Toast.makeText(Data_input.this, genderradioButton1.getText() + "\n" + genderradioButton2.getText()
                    + "\n" + genderradioButton4.getText(), Toast.LENGTH_SHORT).show();

            ref = database.child(userId).push();
            userData.put("smoke", sigara_tut);
            userData.put("id", userId);
            userData.put("date", mytime);
            userData.put("sleep", (String) genderradioButton1.getText());
            userData.put("food", (String) genderradioButton2.getText());
            userData.put("status", "");
            userData.put("drink", (String) genderradioButton4.getText());

            ref.setValue(userData);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } else {

            Toast.makeText(Data_input.this, genderradioButton1.getText() + "\n" + genderradioButton2.getText() + "\n" + genderradioButton3.getText() + "\n" + genderradioButton4.getText()
                    , Toast.LENGTH_SHORT).show();

            ref = database.child(userId).push();
            userData.put("smoke", sigara_tut);
            userData.put("id", userId);
            userData.put("date", mytime);
            userData.put("sleep", (String) genderradioButton1.getText());
            userData.put("food", (String) genderradioButton2.getText());
            userData.put("status", (String) genderradioButton3.getText());
            userData.put("drink", (String) genderradioButton4.getText());

            ref.setValue(userData);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();


        }


    }


}