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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserInformation extends AppCompatActivity {
    EditText Email,phone,address;
    Button save,cancel;
    String Username,PhoneNumber,email,Address;
    boolean flag=false;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information);
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            Username=extras.getString("user");
            PhoneNumber = extras.getString("Phone Number");
            email = extras.getString("Email");
            Address = extras.getString("Address");
            System.out.println(Username+"..........."+PhoneNumber+"//////////////////"+email+"****************"+Address);
        }
        save=findViewById(R.id.save);
        cancel=findViewById(R.id.cancel);
        phone=findViewById(R.id.Phone_ed);
        Email=findViewById(R.id.Email_ed);
        address=findViewById(R.id.Address1);
        address.setText(Address);
        phone.setText(PhoneNumber);
        Email.setText(email);
        Query Username_query = FirebaseDatabase.getInstance().getReference().child("Doctor").orderByChild("Username").equalTo(Username);
        Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot.exists()) {
                      flag = false;
                  }
              }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save.setOnClickListener(v -> {
            Query Username1_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("Username").equalTo(Username);
            Username1_query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        flag=false;
                        String PhoneNo = phone.getText().toString();
                        String newEmail = Email.getText().toString();
                        String newAddress = address.getText().toString();
                        if (!isPhoneValid(PhoneNo)) {
                            phone.setError("Invalid phone number...\nplease, enter correct phone number");
                            Toast.makeText(EditUserInformation.this, "invalid phone number...\nplease, enter correct phone number", Toast.LENGTH_LONG).show();
                        } else if (!isEmailValid(newEmail)) {
                            Email.setError("Invalid E-mail\nPlease,Enter Valid E-mail");
                            Toast.makeText(EditUserInformation.this, "Invalid E-mail\nPlease,Enter Valid E-mail", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditUserInformation.this, "wait!!!", Toast.LENGTH_LONG).show();
                            if (!PhoneNo.equals(PhoneNumber) || !newEmail.equals(email) || !newAddress.equals(Address)) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Patient");
                                reference.child(Username).child("Phone Number").setValue(PhoneNo).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        reference.child(Username).child("Email").setValue(newEmail).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                reference.child(Username).child("Address").setValue(newAddress).addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        Profile.edit = true;
                                                        Toast.makeText(EditUserInformation.this, "done :) ", Toast.LENGTH_LONG).show();

                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                if (Profile.edit) {
                                    Profile.PhoneNumber=PhoneNo;
                                    Profile.email=newEmail;
                                    Profile.Address=newAddress;
                                    Patient.PhoneNumber=PhoneNo;
                                    Patient.email=newEmail;
                                    Patient.Address=newAddress;
                                    Intent intent = new Intent(EditUserInformation.this, Profile.class);
                                    intent.putExtra("user", Username);
                                    intent.putExtra("Phone Number", PhoneNo);
                                    intent.putExtra("Email", newEmail);
                                    intent.putExtra("Address", newAddress);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                    else {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database.getReference("Doctor");
                        Query Usernam_query = reference1.orderByChild("Username").equalTo(Username);
                        Usernam_query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if(snapshot.exists()) {
                                    flag=true;
                                    String PhoneNo = phone.getText().toString();
                                    String newEmail = Email.getText().toString();
                                    String newAddress = address.getText().toString();
                                    if (!isPhoneValid(PhoneNo)) {
                                        phone.setError("Invalid phone number...\nplease, enter correct phone number");
                                        Toast.makeText(EditUserInformation.this, "invalid phone number...\nplease, enter correct phone number", Toast.LENGTH_LONG).show();
                                    } else if (!isEmailValid(newEmail)) {
                                        Email.setError("Invalid E-mail\nPlease,Enter Valid E-mail");
                                        Toast.makeText(EditUserInformation.this, "Invalid E-mail\nPlease,Enter Valid E-mail", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(EditUserInformation.this, "wait!!!", Toast.LENGTH_LONG).show();
                                        if (!PhoneNo.equals(PhoneNumber) || !newEmail.equals(email) || !newAddress.equals(Address)) {
                                            reference1.child(Username).child("Phone Number").setValue(PhoneNo).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    reference1.child(Username).child("Email").setValue(newEmail).addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            reference1.child(Username).child("Address").setValue(newAddress).addOnCompleteListener(task2 -> {
                                                                if (task2.isSuccessful()) {
                                                                    ProfileD.edit = true;
                                                                    Toast.makeText(EditUserInformation.this, "done :) ", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }
                                    if (ProfileD.edit) {

                                        Intent intent = new Intent(EditUserInformation.this, ProfileD.class);
                                        intent.putExtra("user", Username);
                                        intent.putExtra("Phone Number", PhoneNo);
                                        intent.putExtra("Email", newEmail);
                                        intent.putExtra("Address", newAddress);
                                        startActivity(intent);
                                    }
                                }
                                else {
                                    Toast.makeText(EditUserInformation.this, "You didn't change anything", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
        cancel.setOnClickListener(v -> {
            if(flag) {
                Intent intent = new Intent(EditUserInformation.this, ProfileD.class);
                startActivity(intent);
            }
            else {

                Intent intent = new Intent(EditUserInformation.this, Profile.class);
                startActivity(intent);
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