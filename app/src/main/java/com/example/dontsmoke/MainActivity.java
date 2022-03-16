package com.example.dontsmoke;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    String sigara_tut, userId, userId1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference reference,ref;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private ProgressDialog loader;
    Map<String, String> userData = new HashMap<>();
    String key = "", drink, date, sleep, food, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
         recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child(onlineUserID);
        userId = mAuth.getCurrentUser().getUid();


        userId1 = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("users").document(userId1);
        documentReference.addSnapshotListener(MainActivity.this, (documentSnapshot, e) -> {
            if (documentSnapshot.exists()) {

                sigara_tut = documentSnapshot.getString("smoke");

            } else {
                Log.d("tag", "onEvent: Belge mevcut değil");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(reference, Model.class).build();

        FirebaseRecyclerAdapter<Model, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MainActivity.MyViewHolder holder, int position, @NonNull Model model) {
                holder.setDate(model.getDate());
                holder.setDrink(model.getDrink());
                holder.setSleep(model.getSleep());
                holder.setFood(model.getFood());
                holder.setStatus(model.getStatus());

                holder.mView.setOnClickListener(view -> {
                    key = getRef(position).getKey();
                    drink = model.getDrink();
                    sleep = model.getSleep();
                    date = model.getDate();
                    status = model.getStatus();
                    food = model.getFood();
                    updateTask();

                    Log.i("keykeykey", key);
                });

            }

            @NonNull
            @Override
            public MainActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout, parent, false);
                return new MainActivity.MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public void data_input(View view) {
        startActivity(new Intent(getApplicationContext(), Data_input.class));

    }

    public void profil(View view) {
        startActivity(new Intent(getApplicationContext(), Profile.class));

    }

    public void speed_data_input(View view) {

        Toast.makeText(MainActivity.this, "GÜN İÇİ\nYEMEK DIŞI\nİÇECEK YOK", Toast.LENGTH_SHORT).show();
        ref = database.child(userId).push();
        String mytime = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        userData.put("id", userId);
        userData.put("date", mytime);
        userData.put("sleep", "GÜN İÇİ");
        userData.put("smoke", sigara_tut);
        userData.put("food", "YEMEK DIŞI");
        userData.put("status", "");
        userData.put("drink", "İÇECEK YOK");
        ref.setValue(userData);
        Log.i("bilgi", ref.getKey());

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDrink(String drink) {
            TextView taskTextView = mView.findViewById(R.id.drink);
            taskTextView.setText(drink);
        }

        public void setStatus(String status) {
            TextView descTextView = mView.findViewById(R.id.status);
            descTextView.setText(status);
        }

        public void setFood(String food) {
            TextView dateTextView = mView.findViewById(R.id.food);
            dateTextView.setText(food);
        }

        public void setSleep(String sleep) {
            TextView dateTextView = mView.findViewById(R.id.sleep);
            dateTextView.setText(sleep);
        }

        public void setDate(String date) {
            TextView dateTextView = mView.findViewById(R.id.date);
            dateTextView.setText(date);
        }
    }


    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_data, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button deleteBtn = view.findViewById(R.id.btnDelete);

        deleteBtn.setOnClickListener(view1 -> {
            reference.child(key).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Silme İşlmei Başarılı.", Toast.LENGTH_SHORT).show();
                } else {
                    String error = task.getException().toString();
                    Toast.makeText(MainActivity.this, "Hata : " + error, Toast.LENGTH_SHORT).show();
                }
            });

            dialog.dismiss();
        });

        dialog.show();
    }


}
