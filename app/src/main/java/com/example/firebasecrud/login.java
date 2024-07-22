package com.example.firebasecrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    private EditText Etusername, Etnim, Etjurusan, Etgender;
    private Button BtnRegis, BtnLogin;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Etusername = findViewById(R.id.LogUsername);
        Etnim = findViewById(R.id.LogNim);
        Etjurusan = findViewById(R.id.LogJurusan);
        Etgender = findViewById(R.id.LogGender);
        BtnRegis = findViewById(R.id.btnRegis);
        BtnLogin = findViewById(R.id.btnLogin);

        database = FirebaseDatabase.getInstance().getReference("users");

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Etusername.getText().toString();
                String nim = Etnim.getText().toString();
                String jurusan = Etjurusan.getText().toString();
                String gender = Etgender.getText().toString();

                if (username.isEmpty() || nim.isEmpty() || jurusan.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean loginSuccess = false;

                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String dbUsername = userSnapshot.child("username").getValue(String.class);
                                String dbNim = userSnapshot.child("nim").getValue(String.class);
                                String dbJurusan = userSnapshot.child("jurusan").getValue(String.class);
                                String dbGender = userSnapshot.child("gender").getValue(String.class);

                                if (dbUsername != null && dbNim != null && dbJurusan != null && dbGender != null) {
                                    if (dbUsername.equals(username) && dbNim.equals(nim) && dbJurusan.equals(jurusan) && dbGender.equals(gender)) {
                                        loginSuccess = true;
                                        break;
                                    }
                                }
                            }

                            if (loginSuccess) {
                                Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(main);
                            } else {
                                Toast.makeText(getApplicationContext(), "Username, NIM, Jurusan atau Jenis Kelamin salah", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Error Handling
                        }
                    });
                }
            }
        });

        BtnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(), register.class);
                startActivity(register);
            }
        });
    }
}
