package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileD extends AppCompatActivity {
    public static boolean edit=false;
    DrawerLayout drawerLayout1;
    TextView Name,Email,phone,address;
    TextView title;
    static int rowP=Doctor.rowP;
    Button edit_data;
    static String fullName,email,Address,PhoneNumber;
    static String fullName1,email1,Address1,PhoneNumber1;
    EditText fName,sName,lName,Email1,phone1,address1;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_d);
        drawerLayout1=findViewById(R.id.drawer_layout1);
        Name=findViewById(R.id.full_name1);
        Email=findViewById(R.id.Email_ed1);
        phone=findViewById(R.id.Phone_ed1);
        address=findViewById(R.id.Address1);
        fName=findViewById(R.id.full_name0);
        sName=findViewById(R.id.full_name11);
        lName=findViewById(R.id.full_name2);
        Email1=findViewById(R.id.Email_ed11);
        phone1=findViewById(R.id.Phone_ed11);
        address1=findViewById(R.id.Address11);
        title=findViewById(R.id.tit1);
        title.append("Profile");
        if(!edit) {
            fullName = Doctor.fullName;
            email = Doctor.email;
            Address = Doctor.Address;
            PhoneNumber = Doctor.PhoneNumber;
            Name.setText("\nName :                  " + fullName);
            Email.setText("\nEmail :                 " + email);
            phone.setText("\nPhone Number :       " + PhoneNumber);
            address.setText("\nLocation of clinic : " + Address);
            fName.setText("");
            sName.setText("");
            lName.setText("");
            Email1.setText("");
            phone1.setText("");
            address1.setText("");
            fName.setEnabled(false);
            sName.setEnabled(false);
            lName.setEnabled(false);
            Email1.setEnabled(false);
            phone1.setEnabled(false);
            address1.setEnabled(false);
            if (rowP > 0) {
                address.append("\n\nNumber of Your Patient : " + String.valueOf(Doctor.rowP ));
            }
        }else{
            System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyy");
            Name.setText("Name :");
            Email.setText("Email :");
            phone.setText("Phone Number :");
            address.setText("Location of clinic : ");
            String[] fn = new String[3];
            fn = fullName.split(" ");
            System.out.println(phone1.getText().toString());
            fName.setText(fn[0]);
            sName.setText(fn[1]);
            lName.setText(fn[2]);
            Email1.setText(email);
            phone1.setText(PhoneNumber);
            address1.setText(Address);
            fName.setEnabled(true);
            sName.setEnabled(true);
            lName.setEnabled(true);
            Email1.setEnabled(true);
            phone1.setEnabled(true);
            address1.setEnabled(true);
            if (rowP > 0) {
                address.append("\n\nNumber of Your Patient : " + String.valueOf(Doctor.patient.length - 2));
            }
        }
        edit_data=findViewById(R.id.edit_info1);
        edit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit) {
                    System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyy");
                    Name.setText("Name :");
                    Email.setText("Email :");
                    phone.setText("Phone Number :");
                    address.setText("Location of clinic : ");
                    String[] fn = new String[3];
                    fn = fullName.split(" ");
                    fName.setText(fn[0]);
                    sName.setText(fn[1]);
                    lName.setText(fn[2]);
                    Email1.setText(email);
                    phone1.setText(PhoneNumber);
                    address1.setText(Address);
                    if (rowP > 0) {
                        address.append("\n\nNumber of Your Patient : " + String.valueOf(Doctor.rowP));
                    }
                    fName.setEnabled(true);
                    sName.setEnabled(true);
                    lName.setEnabled(true);
                    Email1.setEnabled(true);
                    phone1.setEnabled(true);
                    address1.setEnabled(true);
                    edit = true;
                    edit_data.setText("UPDATE");
                }
                else {
                    fullName1 = fName.getText().toString() + " " + sName.getText().toString() + " " + lName.getText().toString();
                    email1 = Email1.getText().toString();
                    PhoneNumber1 = phone1.getText().toString();
                    Address1 = address1.getText().toString();
                    PhoneNumber = PhoneNumber1;
                    email = email1;
                    Address = Address1;
                    fullName = fullName1;
                    Doctor.PhoneNumber = PhoneNumber1;
                    Doctor.email = email1;
                    Doctor.Address = Address1;
                    Doctor.fullName = fullName1;
                    if (!fullName1.equals(fullName) || !email1.equals(email) || !PhoneNumber1.equals(PhoneNumber) || !Address1.equals(Address)) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database.getReference("Doctor");
                        reference1.child(Doctor.Username).child("Phone Number").setValue(PhoneNumber1).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                reference1.child(Doctor.Username).child("Email").setValue(email1).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        reference1.child(Doctor.Username).child("Address").setValue(Address1).addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                reference1.child(Doctor.Username).child("Full Name").setValue(fullName1).addOnCompleteListener(task3 -> {
                                                    if (task3.isSuccessful()) {
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    Name.setText("Name :                  " + fullName);
                    Email.setText("Email :                 " + email);
                    phone.setText("Phone Number :       " + PhoneNumber);
                    address.setText("Location of clinic : " + Address);
                    fName.setEnabled(false);
                    sName.setEnabled(false);
                    lName.setEnabled(false);
                    Email1.setEnabled(false);
                    phone1.setEnabled(false);
                    address1.setEnabled(false);
                    fName.setText("");
                    sName.setText("");
                    lName.setText("");
                    Email1.setText("");
                    phone1.setText("");
                    address1.setText("");
                    if (rowP > 0) {
                        address.append("\n\nNumber of Your Patient : " + String.valueOf(Doctor.rowP));
                    }
                    edit = false;
                    edit_data.setText("Edit");
                }
            }
        });
    }
    public void  ClickMenuD(View view){
        Doctor.openDrawer(drawerLayout1);
    }
    public void ClickHome(View view){
        Doctor.redirectActivity(this, Doctor.class);
    }
    public void ClickView(View view){
        Doctor.redirectActivity(this,ViewPatientData.class);
    }
    public void ClickContactD(View view){
        Doctor.redirectActivity(this, ContactDoctor.class);
    }
    public void ClickAddPatient(View view){
        Doctor.redirectActivity(this, AddPatient.class);
    }
    public void ClickRemovePatient(View view){ Doctor.redirectActivity(this,RemovePatient.class);}
    public void ClickProfile(View view){recreate();}
    public void ClickLogOut(View view){
        Doctor.logout(this);
    }
    @Override
    protected void onPause(){
        super .onPause();
        Doctor.closeDrawer(drawerLayout1);
    }
}