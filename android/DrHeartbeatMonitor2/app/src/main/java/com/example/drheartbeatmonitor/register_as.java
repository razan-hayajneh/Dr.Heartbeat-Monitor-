package com.example.drheartbeatmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class register_as extends AppCompatActivity {
    Button d,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as);
        d=findViewById(R.id.register_doctor);
        p=findViewById(R.id.register_patient);
        p.setOnClickListener(v -> {
            Intent intent = new Intent(register_as.this, Register.class);
            startActivity(intent);
        });
        d.setOnClickListener(v -> {
            Intent intent = new Intent(register_as.this, register_doctor.class);
            startActivity(intent);
        });

    }
}