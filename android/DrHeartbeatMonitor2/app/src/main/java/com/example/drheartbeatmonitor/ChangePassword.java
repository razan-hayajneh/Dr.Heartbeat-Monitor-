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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//
//
public class ChangePassword extends AppCompatActivity {
    EditText email;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        email=findViewById(R.id.email);
        send=findViewById(R.id.send);
        send.setOnClickListener(v -> {
            String email0 = email.getText().toString();
            if(email0.equals("")){
                Toast.makeText(ChangePassword.this, "you must enter valid Username", Toast.LENGTH_LONG).show();
            }
            else if(isEmailValid(email0))
            {
                Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("Email").equalTo(email0);
                Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot User0:snapshot.getChildren()) {
                                String User = User0.getKey();
                                String Username = Objects.requireNonNull(snapshot.child(User).child("Username").getValue()).toString();
                                String health_insure = Objects.requireNonNull(snapshot.child(User).child("Health Insure").getValue()).toString();
                                String birthday = Objects.requireNonNull(snapshot.child(User).child("Birthday").getValue()).toString();
                                Intent intent = new Intent(ChangePassword.this, ChangePassword1.class);
                                intent.putExtra("Username", Username);
                                intent.putExtra("health_insure", health_insure);
                                intent.putExtra("birthday", birthday);
                                startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(ChangePassword.this, "you must enter valid E-mail", Toast.LENGTH_LONG).show();
                            email.setError("Enter valid E-mail");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {
                Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("Username").equalTo(email0);
                Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount()==1){
                            String birthday= Objects.requireNonNull(snapshot.child(email0).child("Birthday").getValue()).toString();
                            Intent intent = new Intent(ChangePassword.this, ChangePassword1.class);
                            intent.putExtra("Username", email0);
                            intent.putExtra("birthday", birthday);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(ChangePassword.this, "you must enter valid Username", Toast.LENGTH_LONG).show();
                            email.setError("Enter valid Username");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public Boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}