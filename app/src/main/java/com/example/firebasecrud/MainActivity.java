package com.example.firebasecrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView userListView;
    private ArrayList<String> userList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userListView = findViewById(R.id.userListView);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("users");
        loadDataFromFirebase();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] parts = userList.get(position).split("\n");
                String username = parts[0].split(": ")[1];
                String nim = parts[1].split(": ")[1];
                String jurusan = parts[2].split(": ")[1];
                String gender = parts[3].split(": ")[1];

                database.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userId = userSnapshot.getKey();
                            Intent updateIntent = new Intent(MainActivity.this, update.class);
                            updateIntent.putExtra("userId", userId);
                            updateIntent.putExtra("username", username);
                            updateIntent.putExtra("nim", nim);
                            updateIntent.putExtra("jurusan", jurusan);
                            updateIntent.putExtra("gender", gender);
                            startActivity(updateIntent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
            }
        });
    }

    private void loadDataFromFirebase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    String nim = userSnapshot.child("nim").getValue(String.class);
                    String jurusan = userSnapshot.child("jurusan").getValue(String.class);
                    String gender = userSnapshot.child("gender").getValue(String.class);
                    if (username != null && nim != null && jurusan != null && gender != null) {
                        userList.add("Username: " + username + "\nNIM: " + nim + "\nJurusan: " + jurusan + "\nJenis Kelamin: " + gender);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromFirebase();
    }
}
