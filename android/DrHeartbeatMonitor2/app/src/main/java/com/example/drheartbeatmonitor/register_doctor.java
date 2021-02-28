package com.example.drheartbeatmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register_doctor extends AppCompatActivity {
    private EditText Name,  Password, Email, phone, address;
    Button register_doctor;
    Button cancel;
    private EditText first,second,last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
        first = findViewById(R.id.full_name_ed);
        second = findViewById(R.id.full_name_ed1);
        last = findViewById(R.id.full_name_ed2);
        Name = findViewById(R.id.username_ed);
        Password = findViewById(R.id.password_ed);
        register_doctor = findViewById(R.id.RegisterD);
        cancel = findViewById(R.id.cancel);
        Email = findViewById(R.id.Email_ed);
        phone = findViewById(R.id.Phone_ed);
        address = findViewById(R.id.Address);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register_doctor.this, LogIn.class);
                startActivity(intent);
            }
        });
        register_doctor.setOnClickListener(v -> {
            String email = Email.getText().toString(), pass = Password.getText().toString();
            String Username = Name.getText().toString().toLowerCase();
            String fullName = first.getText().toString()+" "+second.getText().toString()+" "+last.getText().toString();
            String Phone = phone.getText().toString();
            String Address = address.getText().toString();
            Map<String, String> newUser = new HashMap();
            newUser.put("Full Name", fullName);
            newUser.put("Username", Username);
            newUser.put("Password", pass);
            newUser.put("Phone Number", Phone);
            newUser.put("Email", email);
            newUser.put("Address", Address);
            if ( first.getText().toString().equals("")||second.getText().toString().equals("")||last.getText().toString().equals("") || Username.equals("") || pass.equals("")  || Phone.equals("") || email.equals("") || Address.equals("")) {
                Toast.makeText(register_doctor.this, "you forgot to enter some required information.\nplease check and try again ", Toast.LENGTH_LONG).show();
                if(first.getText().toString().length()<2){
                    first.setError("Your First Number is required!");
                }
                if(second.getText().toString().length()<2){
                    second.setError("Your middle Number is required!");
                }
                if(last.getText().toString().length()<2){
                    last.setError("Your Last Number is required!");
                }
                if (Username.length() == 0) {
                    Name.setError("USERNAME is required!");
                }
                if (email.length() == 0) {

                    Email.setError("Enter valid E-mail");
                }
                if (address.length()==0) {
                    address.setError("enter location of your clinic");
                    Toast.makeText(register_doctor.this, "you must choose your country", Toast.LENGTH_LONG).show();
                }

            } else {
                Query id_query = FirebaseDatabase.getInstance().getReference().child("Doctor").equalTo(Username);
                id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Name.setError("this Username is used ...\nChoose another one");
                            Toast.makeText(register_doctor.this, "Username is used", Toast.LENGTH_LONG).show();
                        } else {
                            if (Password.length() < 8) {
                                Toast.makeText(register_doctor.this, "your password must be at least 8 litters", Toast.LENGTH_LONG).show();
                                Password.setError("your password must be at least 8 litters");
                            } else {

                                Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").equalTo(Username);
                                Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Name.setError("This UserName is used\nChoose another UserName");
                                            Toast.makeText(register_doctor.this, "This UserName is used\nChoose another UserName", Toast.LENGTH_LONG).show();
                                        } else {
                                            if (!isPhoneValid(Phone)) {
                                                phone.setError("Invalid phone number...\nplease, enter correct phone number");

                                                Toast.makeText(register_doctor.this, "invalid phone number...\nplease, enter correct phone number", Toast.LENGTH_LONG).show();
                                            } else if (!isEmailValid(email)) {
                                                Email.setError("Invalid E-mail\nPlease,Enter Valid E-mail");
                                                Toast.makeText(register_doctor.this, "Invalid E-mail\nPlease,Enter Valid E-mail", Toast.LENGTH_LONG).show();
                                            } else {
                                                Query Username_query = FirebaseDatabase.getInstance().getReference().child("Doctor").orderByChild("Email").equalTo(email);
                                                Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.getChildrenCount() == 0) {
                                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                            DatabaseReference reference = database.getReference("Doctor");
                                                            Toast.makeText(register_doctor.this, "wait!!!", Toast.LENGTH_LONG).show();

                                                            reference.child(Username).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(register_doctor.this, "user has Register successfully :) ", Toast.LENGTH_LONG).show();
                                                                        Intent intent = new Intent(register_doctor.this, LogIn.class);
                                                                        startActivity(intent);

                                                                    } else
                                                                        Toast.makeText(register_doctor.this, "failed Register :( ! ", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(register_doctor.this, "this E-mail is used in another account...", Toast.LENGTH_LONG).show();
                                                            Email.setError("Enter another  valid E-mail");
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }

                                });


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public Boolean isPhoneValid(String phone) {
        String s = phone.substring(0, 3);
        boolean isValid = false;
        if (phone.length() == 10) {
            if (s.equals("077") || s.equals("078") || s.equals("079"))
                isValid = true;
        }
        return isValid;
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