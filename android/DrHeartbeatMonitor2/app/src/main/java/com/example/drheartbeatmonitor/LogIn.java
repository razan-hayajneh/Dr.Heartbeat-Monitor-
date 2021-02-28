package com.example.drheartbeatmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.makeText;

public class LogIn extends AppCompatActivity {
    EditText pass,username;
    TextView Account,change_pass;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.username_ed);
        pass=findViewById(R.id.password_ed);
        Account=findViewById(R.id.Account);
        change_pass=findViewById(R.id.change);
        login=findViewById(R.id.login);
        login.setOnClickListener(v -> {
            String Username=username.getText().toString().toLowerCase();
            String Password=pass.getText().toString();
            if(Username.equals("rt")&&Password.equals("1998"))
            {
                Intent intent = new Intent(LogIn.this, admin.class);
                startActivity(intent);
            }
            else if(Username.equals("")||pass.getText().toString().equals(""))
            {
                makeText(LogIn.this, "you miss one or more from input\nplease enter all input", Toast.LENGTH_LONG).show();
            }
            else {
                Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("Username").equalTo(Username);
                Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("Email").equalTo(Username);
                            Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        Query Username_query = FirebaseDatabase.getInstance().getReference().child("Doctor").orderByChild("Username").equalTo(Username);
                                        Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (!snapshot.exists()) {
                                                    Query Username_query = FirebaseDatabase.getInstance().getReference().child("Doctor").orderByChild("Email").equalTo(Username);
                                                    Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (!snapshot.exists()) {
                                                                makeText(LogIn.this, "Invalid Username", Toast.LENGTH_LONG).show();
                                                            }
                                                            else {
                                                                for(DataSnapshot User0:snapshot.getChildren()) {
                                                                    String User = User0.getKey();
                                                                    if (snapshot.child(User).child("Password").getValue().toString().equals(Password)) {
                                                                        String fullName = snapshot.child(User).child("Full Name").getValue().toString();
                                                                        String PhoneNumber = snapshot.child(User).child("Phone Number").getValue().toString();
                                                                        String email = snapshot.child(User).child("Email").getValue().toString();
                                                                        String Address = snapshot.child(User).child("Address").getValue().toString();
                                                                        Intent intent = new Intent(LogIn.this, Doctor.class);
                                                                        intent.putExtra("Full Name", fullName);
                                                                        intent.putExtra("user", User);
                                                                        intent.putExtra("Phone Number", PhoneNumber);
                                                                        intent.putExtra("Email", email);
                                                                        intent.putExtra("Address", Address);
                                                                        startActivity(intent);
                                                                    } else {
                                                                        makeText(LogIn.this, "Invalid Password", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                                else {
                                                    if(snapshot.child(Username).child("Password").getValue().toString().equals(Password)) {
                                                        String fullName = snapshot.child(Username).child("Full Name").getValue().toString();
                                                        String PhoneNumber = snapshot.child(Username).child("Phone Number").getValue().toString();
                                                        String email = snapshot.child(Username).child("Email").getValue().toString();
                                                        String Address = snapshot.child(Username).child("Address").getValue().toString();
                                                        Intent intent = new Intent(LogIn.this, Doctor.class);
                                                        intent.putExtra("Full Name", fullName);
                                                        intent.putExtra("user", Username);
                                                        intent.putExtra("Phone Number", PhoneNumber);
                                                        intent.putExtra("Email", email);
                                                        intent.putExtra("Address", Address);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        makeText(LogIn.this, "Invalid Password", Toast.LENGTH_LONG).show();
                                                    }

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else {
                                        for(DataSnapshot User0:snapshot.getChildren()) {
                                            String User = User0.getKey();
                                            if (snapshot.child(User).child("Password").getValue().toString().equals(Password)) {
                                                String fullName = snapshot.child(User).child("Full Name").getValue().toString();
                                                String ID_Device = snapshot.child(User).child("ID_Device").getValue().toString();
                                                String HealthInsure = "";
                                                if(snapshot.child(User).child("Health Insure").exists())HealthInsure= snapshot.child(User).child("Health Insure").getValue().toString();
                                                String Birth = snapshot.child(User).child("Birthday").getValue().toString();
                                                String Gender = snapshot.child(User).child("Gender").getValue().toString();
                                                String PhoneNumber = snapshot.child(User).child("Phone Number").getValue().toString();
                                                String email = snapshot.child(User).child("Email").getValue().toString();
                                                String Address = snapshot.child(User).child("Address").getValue().toString();
                                                Intent intent = new Intent(LogIn.this, Patient.class);
                                                intent.putExtra("Full Name", fullName);
                                                intent.putExtra("user", User);
                                                intent.putExtra("ID_Device", ID_Device);
                                                intent.putExtra("Health Insure", HealthInsure);
                                                intent.putExtra("Birthday", Birth);
                                                intent.putExtra("Gender", Gender);
                                                intent.putExtra("Phone Number", PhoneNumber);
                                                intent.putExtra("Email", email);
                                                intent.putExtra("Address", Address);
                                                startActivity(intent);
                                            } else {
                                                makeText(LogIn.this, "Invalid Password", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else {
                            if(snapshot.child(Username).child("Password").getValue().toString().equals(Password)) {
                                String fullName = snapshot.child(Username).child("Full Name").getValue().toString();
                                String ID_Device = snapshot.child(Username).child("ID_Device").getValue().toString();
                                String HealthInsure="";
                                if(snapshot.child(Username).child("Health Insure").exists()){
                                    HealthInsure = snapshot.child(Username).child("Health Insure").getValue().toString();
                                }
                                String Birth = snapshot.child(Username).child("Birthday").getValue().toString();
                                String Gender = snapshot.child(Username).child("Gender").getValue().toString();
                                String PhoneNumber = snapshot.child(Username).child("Phone Number").getValue().toString();
                                String email = snapshot.child(Username).child("Email").getValue().toString();
                                String Address = snapshot.child(Username).child("Address").getValue().toString();
                                Intent intent = new Intent(LogIn.this, Patient.class);
                                intent.putExtra("Full Name", fullName);
                                intent.putExtra("user", Username);
                                intent.putExtra("ID_Device", ID_Device);
                                intent.putExtra("Health Insure", HealthInsure);
                                intent.putExtra("Birthday", Birth);
                                intent.putExtra("Gender", Gender);
                                intent.putExtra("Phone Number", PhoneNumber);
                                intent.putExtra("Email", email);
                                intent.putExtra("Address", Address);
                                startActivity(intent);
                            }
                            else{
                                makeText(LogIn.this, "Invalid Password", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, register_as.class);
                startActivity(intent);
            }
        });
    }

}