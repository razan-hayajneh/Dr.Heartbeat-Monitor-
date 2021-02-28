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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RemovePatient extends AppCompatActivity {
    DrawerLayout drawerLayout1;
    TextView title;
    String[] patient=new String[Doctor.rowP];
    private ListView listView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_patient);
        drawerLayout1=findViewById(R.id.drawer_layout1);
        title=findViewById(R.id.tit1);
        title.append("Remove Patient");
        listView = findViewById(R.id.Patient1);
        TextView textView=findViewById(R.id.txt6);
        int i=0;
        for (;i<Doctor.rowP;i++){
            patient[i]=Doctor.patient_data_list[i][0];
        }
        if(i>0) {
            textView.setText("List of your patients ");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patient);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Remove_Patient(RemovePatient.this,patient[i],Doctor.patient[i],i);
                }
            });
        }
        else{
            textView.setText("List of your patients  is empty");
        }
    }
    public  void Remove_Patient(Activity activity,String name ,String User,int index){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Remove Patient");
        builder.setMessage("Are you sure you want to remove "+name+" from your patients?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Doctor_patient");
                reference.child(User).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            for (int u=index;u<patient.length-1;u++){
                                patient[u]=patient[u+1];
                                Doctor.patient[u]=Doctor.patient[u+1];
                                Doctor.patient_data_list[u]=Doctor.patient_data_list[u+1];
                            }
                            patient[patient.length-1]=null;
                            Doctor.patient[Doctor.patient.length-1]=null;
                            Doctor.patient_data_list[Doctor.patient_data_list.length-1]=null;
                            Doctor.rowP--;
                        }
                        recreate();
                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("AddDoctor");
                reference.child(User).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
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
    public void ClickAddPatient(View view){
        Doctor.redirectActivity(this, AddPatient.class);
    }
    public void ClickRemovePatient(View view){
        recreate();
    }
    public void ClickProfile(View view){
        Doctor.redirectActivity(this,ProfileD.class);
    }
    public void ClickLogOut(View view){
        Doctor.logout(this);
    }
    @Override
    protected void onPause(){
        super .onPause();
        Doctor.closeDrawer(drawerLayout1);
    }
}