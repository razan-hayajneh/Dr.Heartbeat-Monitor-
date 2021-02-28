package com.example.drheartbeatmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword2 extends AppCompatActivity {
    EditText pass,verify_pass;
    Button verify;
    String Username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);
        pass=findViewById(R.id.new_password);
        verify_pass=findViewById(R.id.verfiy_password);
        verify=findViewById(R.id.verify);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Username = extras.getString("Username");
        }
        verify.setOnClickListener(v -> {
            if(pass.getText().toString().length()>=8) {
                if (pass.getText().toString().equals(verify_pass.getText().toString())) {
                    String new_pass = pass.getText().toString();
                    Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").child("Username").equalTo(Username);
                    Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Patient");
                            Toast.makeText(ChangePassword2.this, "wait!!!", Toast.LENGTH_LONG).show();
                            reference.child(Username).child("Password").setValue(new_pass).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                    Toast.makeText(ChangePassword2.this, "done :) ", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ChangePassword2.this, LogIn.class);
                                    startActivity(intent);
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(ChangePassword2.this, "password and verify password are not equals\nplease enter equal passwords", Toast.LENGTH_LONG).show();
                    verify_pass.setError("please enter equal passwords");
                }
            } else {
                Toast.makeText(ChangePassword2.this, "password must be at least 8 characters", Toast.LENGTH_LONG).show();
            }

        });

    }
}