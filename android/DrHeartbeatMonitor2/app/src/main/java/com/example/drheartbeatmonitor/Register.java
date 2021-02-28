package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private EditText Name,  Password, ID_Device, health_insure, Email, phone;
    TextView Birthday;
    private Spinner address;
    private RadioGroup Gender;
    Button register;
    Button cancel;
    private EditText first,second,last;

    //private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        first = findViewById(R.id.full_name_ed);
        second = findViewById(R.id.full_name_ed1);
        last = findViewById(R.id.full_name_ed2);
        Name = findViewById(R.id.username_ed);
        Password = findViewById(R.id.password_ed);
        register = findViewById(R.id.Register);
        cancel = findViewById(R.id.cancel);
        ID_Device = findViewById(R.id.ID_Device_ed);
        //mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.Email_ed);
        phone = findViewById(R.id.Phone_ed);
        address = findViewById(R.id.Address);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.address,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        address.setAdapter(staticAdapter);
        Birthday = findViewById(R.id.Birthday_ed);
        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int Month = c.get(Calendar.MONTH);
        final int Day = c.get(Calendar.DAY_OF_MONTH);
        Birthday.setOnClickListener(v -> {
            @SuppressLint("DefaultLocale") DatePickerDialog datePick = new DatePickerDialog(Register.this, (view, year1, month, dayOfMonth) -> {
                if(year<=year1){
                    Toast.makeText(Register.this, "Incorrect date\nPlease enter your birthday correctly to know your age for health reasons", Toast.LENGTH_LONG).show();
                    Birthday.setError("Incorrect date\nPlease enter your birthday correctly to know your age for health reasons");
                }else {
                    if ((month + 1) <10)
                        if (dayOfMonth <10) {
                            Birthday.setText(String.format("%d/0%d/0%d", year1, month + 1, dayOfMonth));
                        }else {
                            Birthday.setText(String.format("%d/0%d/%d", year1, month + 1, dayOfMonth));
                        }
                    else
                    if (dayOfMonth <10) {
                        Birthday.setText(String.format("%d/%d/0%d", year1, month + 1, dayOfMonth));
                    }else {
                        Birthday.setText(String.format("%d/%d/%d", year1, month + 1, dayOfMonth));
                    }
                }
            }, year, Month, Day);
            datePick.setTitle("SELECT YOUR BIRTHDAY");
            datePick.show();
        });
        health_insure = findViewById(R.id.healthInsurance_ed);
        Gender = findViewById(R.id.Gender);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LogIn.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(v -> {
            String g;
            String email = Email.getText().toString(), pass = Password.getText().toString();
            String id_d = ID_Device.getText().toString();
            String Username = Name.getText().toString().toLowerCase();
            String healthInsure = health_insure.getText().toString();
            String fullName = first.getText().toString()+" "+second.getText().toString()+" "+last.getText().toString();
            String BirthDay = Birthday.getText().toString();
            String Phone = phone.getText().toString();
            if (Gender.getCheckedRadioButtonId() == R.id.radioM)
                g = "Male";
            else
                g = "Female";
            String Address = address.getSelectedItem().toString();
            Map<String, String> newUser = new HashMap();
            newUser.put("ID_Device", id_d);
            newUser.put("Full Name", fullName);
            newUser.put("Username", Username);
            newUser.put("Password", pass);
            newUser.put("Birthday", BirthDay);
            newUser.put("Gender", g);
            newUser.put("Phone Number", Phone);
            newUser.put("Email", email);
            newUser.put("Address", Address);
            if(healthInsure.length()>0)
                newUser.put("Health Insure", healthInsure);
            if (id_d.equals("") || first.getText().toString().equals("")||second.getText().toString().equals("")||last.getText().toString().equals("")|| Username.equals("") || pass.equals("")  || BirthDay.equals("") || Phone.equals("") || email.equals("") || Address.equals("")) {
                Toast.makeText(Register.this, "you forgot to enter some required information.\nplease check and try again ", Toast.LENGTH_LONG).show();
                if(first.getText().toString().length()<2){
                    first.setError("Your First Number is required!");
                }
                if(second.getText().toString().length()<2){
                    second.setError("Your middle Number is required!");
                }
                if(last.getText().toString().length()<2){
                    last.setError("Your Last Number is required!");
                }
                if (id_d.length() == 0) {
                    ID_Device.setError("Enter Number of your Device;\nIf you don'tDevice, you can't Register in this App...");
                }
                if (Username.length() == 0) {
                    Name.setError("USERNAME is required!");
                }
                if (email.length() == 0) {
                    Email.setError("Enter valid E-mail");
                }
            } else {
                Query doctor_query = FirebaseDatabase.getInstance().getReference().child("Doctor").equalTo(Username);
                doctor_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()) {
                            Query id_query = FirebaseDatabase.getInstance().getReference().child("Devices").orderByChild("ID_Device").equalTo(id_d);
                            id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        ID_Device.setError("invalid device number ...\nplease, enter correct number");
                                        Toast.makeText(Register.this, "invalid device number ...\nplease, enter correct number", Toast.LENGTH_LONG).show();
                                    } else {
                                        Query Id_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("ID_Device").equalTo(id_d);
                                        Id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Name.setError("This device is used\ncheck id device");
                                                    Toast.makeText(Register.this, "This device is used\ncheck id device", Toast.LENGTH_LONG).show();
                                                } else {
                                                    if (Password.length() < 8) {
                                                        Toast.makeText(Register.this, "your password must be at least 8 litters", Toast.LENGTH_LONG).show();
                                                        Password.setError("your password must be at least 8 litters");
                                                    } else {
                                                        Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").equalTo(Username);
                                                        Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    Name.setError("This UserName is used\nChoose another UserName");
                                                                    Toast.makeText(Register.this, "This UserName is used\nChoose another UserName", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Query healthInsure_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("health_insure").equalTo(healthInsure);
                                                                    healthInsure_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if (snapshot.getChildrenCount() > 0) {
                                                                                health_insure.setError("This health insure has account");
                                                                                Toast.makeText(Register.this, "This health insure has account", Toast.LENGTH_LONG).show();
                                                                            } else if (pass.length() < 8) {
                                                                                Password.setError("your password must be at least 8 character");
                                                                                Toast.makeText(Register.this, "your password must be almost 8 character", Toast.LENGTH_LONG).show();
                                                                            } else {
                                                                                if (!isPhoneValid(Phone)) {
                                                                                    phone.setError("Invalid phone number...\nplease, enter correct phone number");

                                                                                    Toast.makeText(Register.this, "invalid phone number...\nplease, enter correct phone number", Toast.LENGTH_LONG).show();
                                                                                } else if (!isEmailValid(email)) {
                                                                                    Email.setError("Invalid E-mail\nPlease,Enter Valid E-mail");
                                                                                    Toast.makeText(Register.this, "Invalid E-mail\nPlease,Enter Valid E-mail", Toast.LENGTH_LONG).show();
                                                                                } else if (Address.equals("--select city--")) {
                                                                                    Email.setError("click to choose your country");
                                                                                    Toast.makeText(Register.this, "you must choose your country", Toast.LENGTH_LONG).show();
                                                                                } else {
                                                                                    Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("Email").equalTo(email);
                                                                                    Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            if (snapshot.getChildrenCount() != 1) {
                                                                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                                                DatabaseReference reference = database.getReference("Patient");
                                                                                                Toast.makeText(Register.this, "wait!!!", Toast.LENGTH_LONG).show();

                                                                                                reference.child(Username).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Toast.makeText(Register.this, "user has register successfully :) ", Toast.LENGTH_LONG).show();
                                                                                                            Intent intent = new Intent(Register.this, LogIn.class);
                                                                                                            startActivity(intent);

                                                                                                        } else
                                                                                                            Toast.makeText(Register.this, "failed register :( ! ", Toast.LENGTH_LONG).show();
                                                                                                    }
                                                                                                });
                                                                                            } else {
                                                                                                Toast.makeText(Register.this, "this E-mail is used in another account...", Toast.LENGTH_LONG).show();
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

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            Name.setError("This UserName is used\nChoose another UserName");
                            Toast.makeText(Register.this, "This UserName is used\nChoose another UserName", Toast.LENGTH_LONG).show();
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
