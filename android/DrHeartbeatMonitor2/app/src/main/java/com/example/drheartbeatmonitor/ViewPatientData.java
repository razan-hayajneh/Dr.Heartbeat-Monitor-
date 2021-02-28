package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class ViewPatientData extends AppCompatActivity {
    DrawerLayout drawerLayout1;
    TextView title;
    static  int position=-1;
    Button view;
    ListView list_date;
    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_data);
        drawerLayout1=findViewById(R.id.drawer_layout1);
        title=findViewById(R.id.tit1);
        title.append("View Patient Data");
        view=findViewById(R.id.view2);
        t1=findViewById(R.id.name1);
        t2=findViewById(R.id.Insure);
        t3=findViewById(R.id.birthday1);
        t4=findViewById(R.id.Gender1);
        t5=findViewById(R.id.Phone_ed1);
        t6=findViewById(R.id.Email_ed1);
        t7=findViewById(R.id.Address1);
        t8=findViewById(R.id.disease);
        if(position!=-1) {
            t1.setText("\nPatient name : " + Doctor.patient_data_list[position][0]);
            if(!Doctor.patient_data_list[position][1].equals("")){
                t2.setText("\nHealth Insure : " + Doctor.patient_data_list[position][1]);
            }
            t3.setText("\nBirthday : " + Doctor.patient_data_list[position][2]);
            t4.setText("\nGender : " + Doctor.patient_data_list[position][3]);
            t5.setText("\nPhone Number : " + Doctor.patient_data_list[position][4]);
            t6.setText("\nEmail : " + Doctor.patient_data_list[position][5]);
            t7.setText("\nAddress : " + Doctor.patient_data_list[position][6]);
            t8.setText("");
            if (Doctor.disease_lst[position].length>0) {
                t8.setText("\nPatients' Diseases:\n");
                t8.setPadding(0,2,0,0);
                for (int i = 1; i <= Doctor.disease_lst[position].length; i++)
                    t8.append("\n\t     " + i + "." + Doctor.disease_lst[position][i-1]+"\n");
            }
            t8.append("\n*Heartbeat Rate Record : ");
            t1.setPadding(0,2,0,0);
            t2.setPadding(0,2,0,0);
            t3.setPadding(0,2,0,0);
            t4.setPadding(0,2,0,0);
            t5.setPadding(0,2,0,0);
            t6.setPadding(0,2,0,0);
            t7.setPadding(0,2,0,0);
            list_date = findViewById(R.id.bpm);
            String[] lst = new String[Doctor.BPM_list[position].length+1];
            lst[0] = "Date                                BPM";
            int row = 1;
            for (; row <= Doctor.BPM_list[position].length; row++) {
                if (Doctor.BPM_list[position][row-1] != null) {
                    lst[row] =row+" . "+ Doctor.BPM_list[position][row - 1].substring(0, 4) + "/" + Doctor.BPM_list[position][row - 1].substring(4, 6) + "/" + Doctor.BPM_list[position][row - 1].substring(6, 8) + "." + Doctor.BPM_list[position][row - 1].substring(8,10)+"           "+Doctor.BPM_list[position][row - 1].substring(10);
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lst);
            list_date.setAdapter(arrayAdapter);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doctor.redirectActivity(ViewPatientData.this,Doctor.class);
                /*String patient_name=Patient_user.getText().toString();
                if(patient_name.equals("")){
                    Patient_user.setError("you must  enter patients' full name ");
                }
                else {
                    int index=0;
                    for(;index<Doctor.rowP;index++) {
                        System.out.println(Doctor.patient_data_list[index][0]);
                        System.out.println(patient_name);
                        if (Doctor.patient_data_list[index][0].toLowerCase().equals(patient_name.toLowerCase())) {
                            position = index;
                            break;
                        }

                    }
                    System.out.println(position);
                    System.out.println(index);
                    if(index>=Doctor.rowP){
                    }
                    else if(position!=-1) {
                        Patient_user.setText(Doctor.patient_data_list[position][0]);
                        t1.setText("\nPatient name : " + Doctor.patient_data_list[position][0]);
                        t2.setText("\nHealth Insure : " + Doctor.patient_data_list[position][1]);
                        t3.setText("\nBirthday : " + Doctor.patient_data_list[position][2]);
                        t4.setText("\nGender : " + Doctor.patient_data_list[position][3]);
                        t5.setText("\nPhone Number : " + Doctor.patient_data_list[position][4]);
                        t6.setText("\nEmail : " + Doctor.patient_data_list[position][5]);
                        t7.setText("\nAddress : " + Doctor.patient_data_list[position][6]);
                        t8.setText("");
                        if (Doctor.disease_lst[position].length>0) {
                            t8.setText("\nPatients' Diseases:\n");
                            t8.setPadding(0,2,0,0);
                            for (int i = 1; i <= Doctor.disease_lst[position].length; i++)
                                t8.append("\n\t     " + i + "." + Doctor.disease_lst[position][i-1]+"\n");
                        }
                        t8.append("\n*Heartbeat Rate Record : ");
                        t1.setPadding(0,2,0,0);
                        t2.setPadding(0,2,0,0);
                        t3.setPadding(0,2,0,0);
                        t4.setPadding(0,2,0,0);
                        t5.setPadding(0,2,0,0);
                        t6.setPadding(0,2,0,0);
                        t7.setPadding(0,2,0,0);
                        list_date = findViewById(R.id.bpm);
                        String[] lst = new String[Doctor.BPM_list[position].length+2];
                        lst[0] = "Date                                BPM";
                        int row = 1;
                        for (; row <= Doctor.BPM_list[position].length; row++) {
                            if (Doctor.BPM_list[position][row-1] != null) {
                                lst[row] =row+" . "+ Doctor.BPM_list[position][row - 1].substring(0, 4) + "/" + Doctor.BPM_list[position][row - 1].substring(4, 6) + "/" + Doctor.BPM_list[position][row - 1].substring(6, 8) + "." + Doctor.BPM_list[position][row - 1].substring(8,10)+"           "+Doctor.BPM_list[position][row - 1].substring(10);
                            }
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ViewPatientData.this, android.R.layout.simple_list_item_1, lst);
                        list_date.setAdapter(arrayAdapter);
                    }
                }*/
            }
        });


    }
    public void  ClickMenuD(View view){
        Doctor.openDrawer(drawerLayout1);
    }

    public void ClickHome(View view){Doctor.redirectActivity(this, Doctor.class);}
    public void ClickView(View view){recreate();}
    public void ClickContactD(View view){Doctor.redirectActivity(this, ContactDoctor.class);}
    public void ClickAddPatient(View view){
        Doctor.redirectActivity(this, AddPatient.class);
    }
    public void ClickRemovePatient(View view){ Doctor.redirectActivity(this,RemovePatient.class);}
    public void ClickProfile(View view){Doctor.redirectActivity(this,ProfileD.class);}
    public void ClickLogOut(View view){
        Doctor.logout(this);
    }
    @Override
    protected void onPause(){
        super .onPause();
        Doctor.closeDrawer(drawerLayout1);
    }
}