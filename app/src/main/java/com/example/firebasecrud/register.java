package com.example.firebasecrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class register extends AppCompatActivity {

    private EditText Etusername, Etnim, Etjurusan, Etgender;
    private Button BtnRegis, BtnLogin;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        Etusername = findViewById(R.id.RegUsername);
        Etnim = findViewById(R.id.RegNim);
        Etjurusan = findViewById(R.id.RegJurusan);
        Etgender = findViewById(R.id.RegGender);
        BtnRegis = findViewById(R.id.btnRegis);
        BtnLogin = findViewById(R.id.btnLogin);

        database = FirebaseDatabase.getInstance().getReference("users");

        BtnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Etusername.getText().toString();
                String nim = Etnim.getText().toString();
                String jurusan = Etjurusan.getText().toString();
                String gender = Etgender.getText().toString();

                if (username.isEmpty() || nim.isEmpty() || jurusan.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fieldnya Tolong Di Isi", Toast.LENGTH_SHORT).show();
                } else {
                    String userId = UUID.randomUUID().toString();
                    database.child(userId).child("username").setValue(username);
                    database.child(userId).child("nim").setValue(nim);
                    database.child(userId).child("jurusan").setValue(jurusan);
                    database.child(userId).child("gender").setValue(gender);

                    Toast.makeText(getApplicationContext(), "Daftar Berhasil", Toast.LENGTH_SHORT).show();
                    Intent register = new Intent(getApplicationContext(), login.class);
                    startActivity(register);
                }
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), login.class);
                startActivity(login);
            }
        });
    }
}
