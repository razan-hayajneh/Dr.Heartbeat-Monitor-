package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ChangePassword1 extends AppCompatActivity {
    EditText birthdaytxt;
    Button verify;
    String Username,health_insure,birthday;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Username = extras.getString("Username");
            birthday = extras.getString("birthday");
        }
        birthdaytxt=findViewById(R.id.birthday);
        final Calendar c=Calendar.getInstance();
        final int year=c.get(Calendar.YEAR);
        final int Month=c.get(Calendar.MONTH);
        final int Day=c.get(Calendar.DAY_OF_MONTH);
        birthdaytxt.setOnClickListener(v -> {
            @SuppressLint("DefaultLocale") DatePickerDialog datePick=new DatePickerDialog(ChangePassword1.this, (view, year1, month, dayOfMonth) ->
                    birthdaytxt.setText(String.format("%d/%d/%d", year1, month+1, dayOfMonth)),year,Month,Day);
            datePick.setTitle("SELECT YOUR BIRTHDAY");
            datePick.show();
        });
        verify=findViewById(R.id.verify);
        verify.setOnClickListener(v -> {
            if (birthdaytxt.getText().toString().equals(birthday)) {
                Intent intent = new Intent(ChangePassword1.this, ChangePassword2.class);
                intent.putExtra("Username", Username);
                startActivity(intent);
            } else {
                Toast.makeText(ChangePassword1.this, "Invalid Input\nTry again", Toast.LENGTH_LONG).show();
            }

            i++;
            if (i > 4) {
                Toast.makeText(ChangePassword1.this, "You exceeded the number of allowed attempts without success, \nTry again later", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChangePassword1.this, LogIn.class);
                startActivity(intent);
            }
        });
    }
}