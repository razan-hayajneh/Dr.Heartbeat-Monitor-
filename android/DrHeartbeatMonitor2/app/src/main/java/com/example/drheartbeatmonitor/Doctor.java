package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Doctor extends AppCompatActivity {
    DrawerLayout drawerLayout1;
    TextView title;
    static String Username,fullName,PhoneNumber,email,Address;
    static String[][] added_patient;
    static int row1=0;
    static String[] patient;
    static int rowP=0;
    static String[][] BPM_list;
    static String[][] patient_data_list;
    static String[][] disease_lst;
    private int row_r=0;
    private int row_s=0;
    TextView pp;
    ListView patient1;
    static ArrayAdapter<String> arrayAdapterP;
    static String[][][] messagesArrayList;
    TextView note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        drawerLayout1=findViewById(R.id.drawer_layout1);
        title=findViewById(R.id.tit1);
        Username="";
        fullName="";
        PhoneNumber="";
        email="";
        Address="";
        patient1=findViewById(R.id.Patient1);
        arrayAdapterP = new ArrayAdapter<>(Doctor.this, android.R.layout.simple_list_item_1);
        patient1.setAdapter(arrayAdapterP);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Username = extras.getString("user");
            fullName = extras.getString("Full Name");
            PhoneNumber = extras.getString("Phone Number");
            email = extras.getString("Email");
            Address = extras.getString("Address");
            //The key argument here must match that used in the other activity
        }
        pp=findViewById(R.id.patient_list);
        note=findViewById(R.id.note);
        title.append(String.format("Welcome,Dr.%s", fullName));
        Query Username_query = FirebaseDatabase.getInstance().getReference().child("AddDoctor").orderByChild("Doctor").equalTo(Username);
        Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    row1=0;
                    if(Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))>0) {
                        added_patient = new String[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))][2];
                        note.append("You have"+Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))+" new patient(s), go to add patient page  \n\n");
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            added_patient[row1][0] = dsp.child("Patient").getValue().toString() ; //add result into array list
                            Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").child(added_patient[row1][0]);
                            int finalI1 = row1;
                            Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        if(snapshot.child("Full Name").exists()){
                                            added_patient[finalI1][1] = snapshot.child("Full Name").getValue().toString();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            row1++;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query P_query = FirebaseDatabase.getInstance().getReference().child("Doctor_patient").orderByChild("Doctor").equalTo(Doctor.Username);
        P_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    rowP=0;
                    if(Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))>0) {
                        int no=Integer.parseInt(String.valueOf(snapshot.getChildrenCount())+2);
                        patient = new String[no];
                        messagesArrayList = new String[no+2][][];
                        BPM_list=new String[no][];
                        disease_lst=new String[no][];
                        patient_data_list=new String[no][7];
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            patient[rowP] = dsp.child("Patient").getValue().toString() ; //add result into array list
                            System.out.println(patient[rowP]);
                            rowP++;
                        }
                        for (int i=0;i<rowP;i++) {
                            DatabaseReference BPM_Data_query = FirebaseDatabase.getInstance().getReference().child("BPM_Data").child(patient[i]);
                            int finalI = i;
                            BPM_Data_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // Result will be holden Here
                                    int row = 0;
                                    if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0) {
                                        BPM_list[finalI] = new String[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))];
                                        for (DataSnapshot dsp : snapshot.getChildren()) {
                                            BPM_list[finalI][row] = String.valueOf(dsp.getKey()) + "   " + String.valueOf(dsp.getValue()); //add result into array list
                                            row++;
                                        }
                                    } else
                                        BPM_list[finalI] = new String[0];
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            Query Username_query = FirebaseDatabase.getInstance().getReference().child("Patient").child(patient[i]);
                            int finalI1 = i;
                            Username_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        patient_data_list[finalI1][0] = snapshot.child("Full Name").getValue().toString();
                                        patient_data_list[finalI1][1] = "";
                                        if(snapshot.child("Health Insure").exists()){
                                            patient_data_list[finalI1][1] = snapshot.child("Health Insure").getValue().toString();
                                        }
                                        patient_data_list[finalI1][2] = snapshot.child("Birthday").getValue().toString();
                                        patient_data_list[finalI1][3] = snapshot.child("Gender").getValue().toString();
                                        patient_data_list[finalI1][4] = snapshot.child("Phone Number").getValue().toString();
                                        patient_data_list[finalI1][5] = snapshot.child("Email").getValue().toString();
                                        patient_data_list[finalI1][6] = snapshot.child("Address").getValue().toString();
                                        if (Integer.parseInt(String.valueOf(snapshot.child("your_disease").getChildrenCount())) > 0) {
                                            disease_lst[finalI1] = new String[Integer.parseInt(String.valueOf(snapshot.child("your_disease").getChildrenCount()))];
                                            int row_d = 0;
                                            for (DataSnapshot dsp : snapshot.child("your_disease").getChildren()) {
                                                disease_lst[finalI1][row_d] = (String.valueOf(dsp.getValue())); //add result into array list
                                                row_d++;
                                            }
                                        } else {
                                            disease_lst[finalI1] = new String[0];
                                        }
                                        int finalI3 = finalI1;
                                        DatabaseReference Danger_query = FirebaseDatabase.getInstance().getReference().child("Danger").child(patient[finalI3]);
                                        Danger_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0) {
                                                    int x = 0;
                                                    String[][] arrayList = new String[3][Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))];
                                                    for (DataSnapshot dsp : snapshot.getChildren()) {
                                                        arrayList[0][x] = dsp.child("Date").getValue().toString();
                                                        arrayList[1][x] = dsp.child("BPM_avg").getValue().toString();
                                                        arrayList[2][x] = dsp.child("Reason").getValue().toString();
                                                        if(dsp.child("Status").getValue().toString().equals("w")){
                                                            note.append(patient_data_list[finalI3][0]+"s' heartbeat "+arrayList[2][x]+" as BPM reached to "+arrayList[1][x]+"  on"+arrayList[0][x]);
                                                            Danger_query.child(arrayList[0][x]).child("Status").setValue("r").addOnCompleteListener(task8 -> {});
                                                        }
                                                        x++;
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
                            DatabaseReference message_query = FirebaseDatabase.getInstance().getReference().child("Messages").child(patient[i]);
                            int finalI2 = i;
                            message_query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // Result will be holden Here
                                    // row_s = 0;
                                    // row_r=0;
                                    int count=0;
                                    if (Integer.parseInt(String.valueOf(snapshot.getChildrenCount())) > 0) {
                                        int x = 0;
                                        messagesArrayList[finalI2] = new String[3][Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))];
                                        for (DataSnapshot dsp : snapshot.getChildren()) {
                                            if (dsp.child("Sender").getValue().toString().equals(Username)) {
                                                messagesArrayList[finalI2][0][x] = "2";
                                                messagesArrayList[finalI2][1][x] = dsp.child("Date").getValue().toString();
                                                messagesArrayList[finalI2][2][x] = dsp.child("Contain").getValue().toString();
                                                x++;
                                            } else if (dsp.child("Receiver").getValue().toString().equals(Username)) {
                                                messagesArrayList[finalI2][0][x] = "1";
                                                messagesArrayList[finalI2][1][x] = dsp.child("Date").getValue().toString();
                                                messagesArrayList[finalI2][2][x] = dsp.child("Contain").getValue().toString();
                                                if(dsp.child("Status").getValue().toString().equals("w")){
                                                    count++;
                                                    message_query.child(messagesArrayList[finalI2][1][x]).child("Status").setValue("r");
                                                }
                                                x++;
                                            }
                                        }
                                        if(count>0) {
                                            arrayAdapterP.add(patient_data_list[finalI2][0] + "\t" +count+"message(s)");
                                        }else {
                                            arrayAdapterP.add(patient_data_list[finalI2][0]);
                                        }
                                    /*if(x==Integer.parseInt(String.valueOf(snapshot.getChildrenCount())))
                                    {

                                    }*/
                                    } else {
                                        arrayAdapterP.add(patient_data_list[finalI2][0]);
                                        messagesArrayList[finalI2] = new String[3][1];
                                        messagesArrayList[finalI2][0][0] = "x";
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        System.out.println(".5444 "+patient_data_list.length);
                        if(rowP>0){
                            pp.append("Your Patient : ");
                        }

                    }
                }
                else {
                    patient = new String[0];
                    messagesArrayList = new String[0][0][0];
                    BPM_list=new String[0][0];
                    disease_lst=new String[0][0];
                    patient_data_list=new String[0][0];
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        patient1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View_patient(Doctor.this,patient[position],position);
            }
        });
    }
    public void ClickMenuD(View view){
        openDrawer(drawerLayout1);
    }

    public static void openDrawer(DrawerLayout drawerLayout1) {
        drawerLayout1.openDrawer(GravityCompat.START);
    }
    public  void View_patient(Activity activity, String User, int index){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("View Patient");
        builder.setMessage("You want to view "+User+"s\' information and BPM Recorded or Contact ?");
        builder.setPositiveButton("View Patient Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewPatientData.position=index;
                redirectActivity(Doctor.this,ViewPatientData.class);
            }
        });
        builder.setNegativeButton("Contact", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactDoctor.position=index;
                redirectActivity(Doctor.this,ContactDoctor.class);
            }
        });
        builder.show();
    }
    public static void closeDrawer(DrawerLayout drawerLayout1){
        if(drawerLayout1.isDrawerOpen(GravityCompat.START)){
            drawerLayout1.closeDrawer(GravityCompat.START);
        }
    }
/*    private String[][] date_compare(String[][] dateRec, int row_s, String[][] dateSend, int row_r) {
        if(row_r ==0){
            String [][] list=new String[3][row_s];
            int i=0;
            for (; i< row_s; i++){
                list[0][i]="2";
                list[1][i]=dateSend[0][i];
                list[2][i]=dateSend[1][i];
            }
            return list;
        }else if(row_s ==0) {
            String[][] list = new String[3][row_r];
            int i=0;
            for (; i< row_r; i++){
                list[0][i]="1";
                list[1][i]=dateRec[0][i];
                list[2][i]=dateRec[1][i];
            }
            return list;
        } else {
            String[][] list = new String[3][row_s + row_r];
            int i = 0;
            for (; i < row_r; i++) {
                list[0][i] = "1";
                list[1][i] = dateRec[0][i];
                list[2][i] = dateRec[1][i];
            }
            for (; i < row_r + row_s; i++) {
                list[0][i] = "2";
                list[1][i] = dateSend[0][i-row_r];
                list[2][i] = dateSend[1][i-row_r];
            }
            for (i = 0; i < list[0].length-1&&!list[1][i].equals(null); i++) {
                for (int j = i + 1; j < list[0].length&&!list[1][i].equals(null); j++) {
                    if (Objects.equals(list[1][i], list[1][j])) {
                        if (!list[0][j].equals(list[0][i])) {
                            if (list[0][j].equals("2")) {
                                list[0][j] = list[0][i];
                                list[0][i] = "2";
                                String tem = list[1][j];
                                list[1][j] = list[1][i];
                                list[1][i] = tem;
                                String tem1 = list[2][j];
                                list[2][j] = list[2][i];
                                list[1][i] = tem1;
                            }
                        }
                    } else {
                        int y = Integer.parseInt(list[1][i].substring(0, 4));
                        int y1 = Integer.parseInt(list[1][j].substring(0, 4));
                        if (y == y1) {
                            int M = Integer.parseInt(list[1][i].substring(5, 7));
                            int M1 = Integer.parseInt(list[1][j].substring(5, 7));
                            if (M == M1) {
                                int d = Integer.parseInt(list[1][i].substring(8, 10));
                                int d1 = Integer.parseInt(list[1][j].substring(8, 10));
                                if (d == d1) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                } else if (d1 < d) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                }
                            } else if (M1 < M) {
                                int d = Integer.parseInt(list[1][i].substring(8, 10));
                                int d1 = Integer.parseInt(list[1][j].substring(8, 10));
                                if (d == d1) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                } else if (d1 < d) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (y1 < y) {
                            int M = Integer.parseInt(list[1][i].substring(5, 7));
                            int M1 = Integer.parseInt(list[1][j].substring(5, 7));
                            if (M == M1) {
                                int d = Integer.parseInt(list[1][i].substring(8, 10));
                                int d1 = Integer.parseInt(list[1][j].substring(8, 10));
                                if (d == d1) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                } else if (d1 < d) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                }
                            } else if (M1 < M) {
                                int d = Integer.parseInt(list[1][i].substring(8, 10));
                                int d1 = Integer.parseInt(list[1][j].substring(8, 10));
                                if (d == d1) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                } else if (d1 < d) {
                                    int h = Integer.parseInt(list[1][i].substring(11, 13));
                                    int h1 = Integer.parseInt(list[1][j].substring(11, 13));
                                    if (h == h1) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    } else if (h1 < h) {
                                        int m = Integer.parseInt(list[1][i].substring(14, 16));
                                        int m1 = Integer.parseInt(list[1][j].substring(14, 16));
                                        if (m == m1) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        } else if (m1 < m) {
                                            int s = Integer.parseInt(list[1][i].substring(17, 19));
                                            int s1 = Integer.parseInt(list[1][j].substring(17, 19));
                                            if (s1 < s) {
                                                String tem = list[0][j];
                                                list[0][j] = list[0][i];
                                                list[0][i] = tem;
                                                String tem1 = list[1][j];
                                                list[1][j] = list[1][i];
                                                list[1][i] = tem1;
                                                String tem2 = list[2][j];
                                                list[2][j] = list[2][i];
                                                list[1][i] = tem2;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return list;
        }

    }*/

    public void ClickHome(View view){
        recreate();
    }
    public void ClickView(View view){
        redirectActivity(this,ViewPatientData.class);
    }
    public void ClickContactD(View view){
        redirectActivity(this, ContactDoctor.class);
    }
    public void ClickAddPatient(View view){
        redirectActivity(this, AddPatient.class);
    }
    public void ClickRemovePatient(View view){
        redirectActivity(this, RemovePatient.class);
    }
    public void ClickProfile(View view){
        redirectActivity(this,ProfileD.class);
    }
    public void ClickLogOut(View view){
        logout(this);
    }
    public static void logout(Activity activity){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("LogOut");
        builder.setMessage("Are You Sure You Want To Log Out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public static void redirectActivity(Activity activity,Class clas) {
        Intent intent = new Intent(activity, clas);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("user",Username);
        intent.putExtra("Full Name", fullName);
        intent.putExtra("Phone Number", PhoneNumber);
        intent.putExtra("Email", email);
        intent.putExtra("Address", Address);
        activity.startActivity(intent);
    }
    @Override
    protected void onPause(){
        super .onPause();
        closeDrawer(drawerLayout1);
    }
}