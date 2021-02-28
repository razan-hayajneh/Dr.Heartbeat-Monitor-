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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddDoctor extends AppCompatActivity {
    static DrawerLayout drawerLayout;
    TextView title,txt;
    ListView listView;
    String[] doctor_lst;
    int row;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        drawerLayout=findViewById(R.id.drawer_layout);
        title=findViewById(R.id.tit);
        txt=findViewById(R.id.txt8);
        title.append("Add Doctor");
        listView = findViewById(R.id.Doctors);
        row=Patient.row1;
        doctor_lst=new String[row];
        if(!Patient.doctor_name.equals("")){
            txt.setText("Your doctor is "+Patient.doctor_name+"\n\nIf you want to change your doctor, you can send a request and waiting new doctor to accept");
        }else{txt.setText("You don\'t have doctor, you can send a request and waiting doctor to accept");}
        if (row >= 0) System.arraycopy(Patient.doctor_lst, 0, doctor_lst, 0, row);
        if(row>0){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctor_lst);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String doctor=doctor_lst[i];
                String[] index=doctor.split(" ");
                String[] index2=index[2].split("\n");
                Choice_doctor(AddDoctor.this,index2[0]);
            }
        });
        }
    }
    public  void Choice_doctor(Activity activity,String user){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Choice Doctor");
        if(Patient.doctor_name.equals(user)){
            builder.setMessage("Dr." + user + " is your doctor, you can choose another one");
        }
        else {
            builder.setMessage("You chose Dr." + user + "\nAre You Sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, String> newUser = new HashMap<>();
                    newUser.put("Doctor", user);
                    newUser.put("Patient", Patient.Username);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("AddDoctor");
                    reference.child(Patient.Username).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddDoctor.this, "user has Add successfully :) \nWaiting Doctor agree", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(AddDoctor.this, "failed Register :( ! ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.show();
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
    public void ClickContact(View view){ Patient.redirectActivity(this,Contact.class); }
    public void ClickAddDoctor(View view){ recreate(); }
    public void ClickProfile(View view){
        Patient.redirectActivity(this,Profile.class);
    }
    public void ClickLogOut(View view){
        Patient.logout(this);
    }
    @Override
    protected void onPause(){
        super .onPause();
        Patient.closeDrawer(drawerLayout);
    }

}