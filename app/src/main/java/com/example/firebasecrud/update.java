package com.example.firebasecrud;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class update extends AppCompatActivity {

    private EditText editUsername, editNim, editJurusan, editGender;
    private Button btnUpdate, btnDelete;
    private DatabaseReference database;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editUsername = findViewById(R.id.editUsername);
        editNim = findViewById(R.id.editNim);
        editJurusan = findViewById(R.id.editJurusan);
        editGender = findViewById(R.id.editGender);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        String username = intent.getStringExtra("username");
        String nim = intent.getStringExtra("nim");
        String jurusan = intent.getStringExtra("jurusan");
        String gender = intent.getStringExtra("gender");

        editUsername.setText(username);
        editNim.setText(nim);
        editJurusan.setText(jurusan);
        editGender.setText(gender);

        database = FirebaseDatabase.getInstance().getReference("users");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editUsername.getText().toString();
                String newNim = editNim.getText().toString();
                String newJurusan = editJurusan.getText().toString();
                String newGender = editGender.getText().toString();

                if (newUsername.isEmpty() || newNim.isEmpty() || newJurusan.isEmpty() || newGender.isEmpty()) {
                    Toast.makeText(update.this, "Fieldnya tolong di isi banh", Toast.LENGTH_SHORT).show();
                } else {
                    updateUser(userId, newUsername, newNim, newJurusan, newGender);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void updateUser(String userId, String newUsername, String newNim, String newJurusan, String newGender) {
        DatabaseReference userRef = database.child(userId);

        userRef.child("username").setValue(newUsername);
        userRef.child("nim").setValue(newNim);
        userRef.child("jurusan").setValue(newJurusan);
        userRef.child("gender").setValue(newGender, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(update.this, "Update gagal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(update.this, "Update berhasil", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(update.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });
    }

    private void deleteUser(String userId) {
        database.child(userId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(update.this, "Delete gagal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(update.this, "Delete berhasil", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(update.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda yakin ingin menghapus data ini?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(userId);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
