package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.drheartbeatmonitor.Patient.doctor_name;

public class Profile extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView title;
    Spinner disease;
    Button add_disease,remove_disease;
    Spinner list_disease;
    TextView Email,phone,address;
    Button edit_data;
    String[] lst=new String[Patient.row_d+2];
    EditText fName,sName,lName,Email1,phone1,address1,ID_Dev1,health;
    static String fullName,email,Address,PhoneNumber;
    static String fullName1,email1,Address1,PhoneNumber1;
    public static boolean edit=false;
    boolean f=true;
    int row=0;
    private String HealthInsure,HealthInsure1;
    private String ID_Device,ID_Device1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawerLayout=findViewById(R.id.drawer_layout);
        title=findViewById(R.id.tit);
        String Username=Patient.Username;
        fullName=Patient.fullName;
        String Birth=Patient.Birth;
        String Gender=Patient.Gender;
        edit_data=findViewById(R.id.edit_info);
        remove_disease=findViewById(R.id.remove_disease);
        TextView name = findViewById(R.id.username_ed);
        TextView fullName11 = findViewById(R.id.full_name_ed);
        TextView ID_Dev = findViewById(R.id.ID_Device_ed);
        TextView health_insure = findViewById(R.id.healthInsurance_ed);
        TextView birthday = findViewById(R.id.Birthday_ed);
        TextView gender = findViewById(R.id.Gender_ed);
        TextView doctor = findViewById(R.id.Doctor_name);
        health=findViewById(R.id.health_insure1);
        ID_Dev1=findViewById(R.id.id_dev1);
        Email = findViewById(R.id.Email_ed);
        phone = findViewById(R.id.Phone_ed);
        address = findViewById(R.id.Address);
        disease=findViewById(R.id.diseases);
        add_disease=findViewById(R.id.add_disease);
        list_disease=findViewById(R.id.Your_diseases);
        fName=findViewById(R.id.full_name00);
        sName=findViewById(R.id.full_name10);
        lName=findViewById(R.id.full_name20);
        Email1=findViewById(R.id.Email_ed10);
        phone1=findViewById(R.id.Phone_ed10);
        address1=findViewById(R.id.Address10);
        if(f) {
            row=Patient.row_d;
            if (row > 0)
                System.arraycopy(Patient.disease_lst, 0, lst, 0, row);
            f=false;
        }
        if(lst.length==2){
            lst[0]="you don't have any disease";
        }
        if(!doctor_name.equals("")){
            doctor.setText("Your Doctor  :  "+ Patient.doctor_data[0]);
        }
        else doctor.setText("You don\'t have doctor,go to Add doctor page and doctor");
        doctor.setOnClickListener(v -> {
            if(!doctor_name.equals(" "))Patient.redirectActivity(Profile.this,Contact.class);
            else Patient.redirectActivity(Profile.this,AddDoctor.class);
        });
        ArrayList<String> stringArrayList=new ArrayList<>(Arrays.asList(lst));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,lst);
        list_disease.setAdapter(arrayAdapter);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.your_disease,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        disease.setAdapter(staticAdapter);
        title.append("Profile");
        name.setText(Username);
        fullName11.setText(fullName);
        gender.setText(Gender);
        birthday.setText(Birth);

        if(!edit) {
            email=Patient.email;
            Address=Patient.Address;
            PhoneNumber=Patient.PhoneNumber;
            ID_Device=Patient.ID_Device;
            HealthInsure=Patient.HealthInsure;
        }
        else {
            Patient.email=email;
            Patient.Address=Address;
            Patient.PhoneNumber=PhoneNumber;
            Patient.ID_Device=ID_Device;
            Patient.HealthInsure=HealthInsure;
        }
        if (HealthInsure.length()>2) {
            health_insure.setText("Health Insurance : "+HealthInsure+"\n");
        }
        ID_Dev.setText(ID_Device);
        Email.setText(email);
        phone.setText(PhoneNumber);
        address.setText(Address);
        remove_disease.setOnClickListener(v -> {
            String diseases =list_disease.getSelectedItem().toString();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Patient");
            if(diseases.equals("Your Disease : ")){
                Toast.makeText(Profile.this, "choose disease to remove from your information  ", Toast.LENGTH_LONG).show();
            }
            else {
                reference.child(Username).child("your_disease").child(diseases).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Profile.this, "disease removed from your information  ", Toast.LENGTH_LONG).show();
                        int co = 0;
                        while (co < row) {
                            if (diseases.equals(lst[co])) {
                                for (int i = co; i < Patient.row_d - 1; i++) {
                                    lst[i] = lst[i + 1];
                                    Patient.disease_lst[i] = Patient.disease_lst[i + 1];
                                    ;
                                }
                                Patient.disease_lst[row - 1] = null;
                                lst[row - 1] = null;
                                row--;
                                Patient.row_d--;
                                recreate();
                            }
                            co++;
                        }
                    }
                });
            }

        });
        add_disease.setOnClickListener(v -> {
            String diseases =disease.getSelectedItem().toString();
            if(diseases.equals("--select disease--")){
                Toast.makeText(Profile.this, "you must choose your disease before add", Toast.LENGTH_LONG).show();
            }
            else {
                    if(!stringArrayList.contains(diseases)) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Patient");
                        Toast.makeText(Profile.this, "wait!!!", Toast.LENGTH_LONG).show();
                        reference.child(Username).child("your_disease").child(diseases).setValue(diseases).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Profile.this, "disease has added in your information  ", Toast.LENGTH_LONG).show();
                                    Patient.disease_lst[Patient.row_d++] = (diseases);
                                    lst[row++]=diseases;
                                    recreate();
                                }
                            }
                        });
                    }
                    else
                        Toast.makeText(Profile.this, "disease found in your data  ", Toast.LENGTH_LONG).show();
            }
        });
        edit_data.setOnClickListener(v -> {
            if (!edit) {
                String[] fn = new String[3];
                fn = fullName.split(" ");
                fName.setText(fn[0]);
                sName.setText(fn[1]);
                lName.setText(fn[2]);
                Email1.setText(email);
                phone1.setText(PhoneNumber);
                address1.setText(Address);
                ID_Dev1.setText(ID_Device);
                health_insure.setText("Health Insurance : ");
                if (HealthInsure.length()>2) {
                    health.setText(HealthInsure);
                }
                else {
                    health.setText("               ");
                }
                health.setBackgroundResource(R.drawable.background_stroke);
                fName.setBackgroundResource(R.drawable.background_stroke);
                sName.setBackgroundResource(R.drawable.background_stroke);
                lName.setBackgroundResource(R.drawable.background_stroke);
                Email1.setBackgroundResource(R.drawable.background_stroke);
                ID_Dev1.setBackgroundResource(R.drawable.background_stroke);
                phone1.setBackgroundResource(R.drawable.background_stroke);
                address1.setBackgroundResource(R.drawable.background_stroke);
                fullName11.setText("");
                Email.setText("");
                phone.setText("");
                address.setText("");
                edit = true;
                edit_data.setText("UPDATE");
                title.setText("Edit Information");
            }
            else {
                title.setText("Profile");
                fullName1 = fName.getText().toString() + " " + sName.getText().toString() + " " + lName.getText().toString();
                email1 = Email1.getText().toString();
                PhoneNumber1 = phone1.getText().toString();
                Address1 = address1.getText().toString();
                HealthInsure1=health.getText().toString();
                ID_Device1=ID_Dev1.getText().toString();
                health.setBackground(null);
                fName.setBackground(null);
                sName.setBackground(null);
                lName.setBackground(null);
                Email1.setBackground(null);
                address1.setBackground(null);
                phone1.setBackground(null);
                ID_Dev1.setBackground(null);
                if (!fullName1.equals(fullName) || !email1.equals(email) || !PhoneNumber1.equals(PhoneNumber) || !Address1.equals(Address)||!HealthInsure.equals(HealthInsure1)) {
                    PhoneNumber = PhoneNumber1;
                    email = email1;
                    Address = Address1;
                    fullName = fullName1;
                    HealthInsure=HealthInsure1;
                    ID_Device=ID_Device1;
                    Patient.PhoneNumber = PhoneNumber1;
                    Patient.email = email1;
                    Patient.Address = Address1;
                    Patient.fullName = fullName1;
                    Patient.HealthInsure=HealthInsure1;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference1 = database.getReference("Patient");
                    reference1.child(Patient.Username).child("Phone Number").setValue(PhoneNumber1).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            reference1.child(Patient.Username).child("Email").setValue(email1).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    reference1.child(Patient.Username).child("Address").setValue(Address1).addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            reference1.child(Patient.Username).child("Full Name").setValue(fullName1).addOnCompleteListener(task3 -> {
                                                if (task3.isSuccessful()) {
                                                    Query healthInsure_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("health_insure").equalTo(HealthInsure1);
                                                    healthInsure_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(!snapshot.exists()){
                                                                reference1.child(Patient.Username).child("health_insure").setValue(HealthInsure1).addOnCompleteListener(task4 -> {
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    Query id_query = FirebaseDatabase.getInstance().getReference().child("Devices").orderByChild("ID_Device").equalTo(ID_Device1);
                                                    id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.exists()){
                                                                Query Id_query = FirebaseDatabase.getInstance().getReference().child("Patient").orderByChild("ID_Device").equalTo(ID_Device1);
                                                                Id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if(!snapshot.exists()){
                                                                            reference1.child(Patient.Username).child("ID_Device").setValue(ID_Device1).addOnCompleteListener(task5 -> {

                                                                            });
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
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                fullName11.setText( fullName);
                Email.setText( email);
                phone.setText( PhoneNumber);
                address.setText( Address);
                ID_Dev.setText(ID_Device);
                if (HealthInsure.length()>2) {
                    health_insure.setText("Health Insurance : "+HealthInsure+"\n");
                }else health_insure.setText("");
                fName.setText("");
                sName.setText("");
                lName.setText("");
                Email1.setText("");
                phone1.setText("");
                address1.setText("");
                health.setText("");
                ID_Dev1.setText("");
                edit = false;
                edit_data.setText("Edit");
            }
        });


    }
    public void  ClickMenu(View view){
        Patient.openDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        Patient.redirectActivity(this,Patient.class);
    }
    public void ClickBPM(View view){
        Patient.redirectActivity(this,MonitorBPM.class);
    }
    public void ClickContact(View view){
        Patient.redirectActivity(this,Contact.class);
    }
    public void ClickProfile(View view){recreate(); }
    public void ClickAddDoctor(View view){ Patient.redirectActivity(this,AddDoctor.class); }
    public void ClickLogOut(View view){
        Patient.logout(this);
    }
    @Override
    protected void onPause(){
        super .onPause();
        Patient.closeDrawer(drawerLayout);
    }
}