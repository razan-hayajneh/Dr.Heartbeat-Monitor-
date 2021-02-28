package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddPatient extends AppCompatActivity {
    DrawerLayout drawerLayout1;
    TextView title;
    String[] add_patient=new String[Doctor.row1];
    int row_d,row1,row;
    ListView listView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        drawerLayout1=findViewById(R.id.drawer_layout1);
        title=findViewById(R.id.tit1);
        title.append("Add Patient");
        listView = findViewById(R.id.Patient);
        TextView textView=findViewById(R.id.txt5);
        row=Doctor.row1;
        for (int i=0;i<row;i++){
            add_patient[i]=Doctor.added_patient[i][1];
        }
        if(row>0) {
            textView.setText("List of patients added you");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, add_patient);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final String[] disease_lst = {""};
                    DatabaseReference Disease_query = FirebaseDatabase.getInstance().getReference().child("Patient").child(add_patient[i]).child("your_disease");
                    Disease_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0) {
                                row_d = 1;
                                disease_lst[0] += "disease:\n";
                                for (DataSnapshot dsp : snapshot.getChildren()) {
                                    disease_lst[0] = String.format("%s%d . %s\n", disease_lst[0], row1, Objects.requireNonNull(dsp.getValue()).toString());
                                    row_d++;
                                }
                            }
                            Add_patient(AddPatient.this,Doctor.added_patient[i][0], add_patient[i], disease_lst, i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
        else{
            textView.setText("List of patients added you is empty");
        }
    }
    public  void Add_patient(Activity activity, String User, String fullName, String[] disease_lst,int index){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Add Patient");
        builder.setMessage("You want to accept "+fullName.toString()+"\n"+disease_lst[0].toString()+"\nAre You Sure?");
        builder.setPositiveButton("accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> newUser = new HashMap<>();
                newUser.put("Doctor", Doctor.Username);
                newUser.put("Patient", User);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Doctor_patient");
                reference.child(User).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").child(User);
                            Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Doctor.patient_data_list[Doctor.rowP + 1][0] = snapshot.child("Full Name").getValue().toString();
                                        Doctor.patient_data_list[Doctor.rowP + 1][1] = "";
                                        if (snapshot.child("Health Insure").exists()) {
                                            Doctor.patient_data_list[Doctor.rowP + 1][1] = snapshot.child("Health Insure").getValue().toString();
                                        }
                                        Doctor.patient_data_list[Doctor.rowP + 1][2] = snapshot.child("Birthday").getValue().toString();
                                        Doctor.patient_data_list[Doctor.rowP + 1][3] = snapshot.child("Gender").getValue().toString();
                                        Doctor.patient_data_list[Doctor.rowP + 1][4] = snapshot.child("Phone Number").getValue().toString();
                                        Doctor.patient_data_list[Doctor.rowP + 1][5] = snapshot.child("Email").getValue().toString();
                                        Doctor.patient_data_list[Doctor.rowP + 1][6] = snapshot.child("Address").getValue().toString();
                                        Doctor.arrayAdapterP.add(Doctor.patient_data_list[Doctor.rowP + 1][0]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            if (row == 1) {
                                add_patient[0] = " ";
                                Doctor.added_patient[0] = null;
                                row--;
                                Doctor.row1--;
                            } else {
                                for (int i = index; i < row - 1; i++) {
                                    add_patient[i] = add_patient[i + 1];
                                    Doctor.added_patient[i] = Doctor.added_patient[i + 1];
                                    row--;
                                    Doctor.row1--;
                                }
                                add_patient[row] = "";
                                Doctor.added_patient[row] = null;

                            }
                            DatabaseReference reference = database.getReference("AddDoctor");
                            reference.child(User).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        Toast.makeText(AddPatient.this, "Patient has Add successfully :)", Toast.LENGTH_LONG).show();
                                    recreate();
                                }
                            });
                        } else
                            Toast.makeText(AddPatient.this, "failed Register :( ! ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("AddDoctor");
                reference.child(User).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(AddPatient.this, "Patient has Add successfully :)", Toast.LENGTH_LONG).show();
                        recreate();
                    }
                });
                dialog.dismiss();
            }
        });
        builder.show();
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
    public void ClickAddPatient(View view){recreate();}
    public void ClickRemovePatient(View view){ Doctor.redirectActivity(this,RemovePatient.class);}
    public void ClickProfile(View view){Doctor.redirectActivity(this, ProfileD.class);}
    public void ClickLogOut(View view){
        Doctor.logout(this);
    }
    @Override
    protected void onPause(){
        super .onPause();
        Doctor.closeDrawer(drawerLayout1);
    }
}