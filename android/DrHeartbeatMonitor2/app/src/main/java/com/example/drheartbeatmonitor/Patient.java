package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Patient extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView title,txt;
    static String Username,fullName,ID_Device,HealthInsure,Birth,Gender,PhoneNumber,email,Address;
    static String[] lst;
    static String[] disease_lst;
    static String[] doctor_lst;
    static  String mac_address;
    static String[] doctor_data;
    static int row,row_d=0,row1,row_s,row_r;
    static int age ;
    static String[][] send_message, receive_message;
    static String doctor_name="";
    static ArrayList<messages> messagesArrayList =new ArrayList<>();
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        drawerLayout=findViewById(R.id.drawer_layout);
        title=findViewById(R.id.tit);
        txt=findViewById(R.id.txt);
        Username="";
        fullName="";
        ID_Device="";
        HealthInsure="";
        Birth="";
        Gender="";
        PhoneNumber="";
        email="";
        Address="";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Username = extras.getString("user");
            fullName = extras.getString("Full Name");
            ID_Device = extras.getString("ID_Device");
            HealthInsure = extras.getString("Health Insure");
            Birth = extras.getString("Birthday");
            Gender = extras.getString("Gender");
            PhoneNumber = extras.getString("Phone Number");
            email = extras.getString("Email");
            Address = extras.getString("Address");
            //The key argument here must match that used in the other activity
        }
        title.append(String.format("Welcome, %s", fullName));
        DatabaseReference BPM_Data_query = FirebaseDatabase.getInstance().getReference().child("BPM_Data").child(Username);
        BPM_Data_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Result will be holden Here
                row=0;
                if(Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))>0) {
                    lst = new String[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))+2];
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        lst[row] = String.valueOf(dsp.getKey()) + "   " + String.valueOf(dsp.getValue()); //add result into array list
                        System.out.println(lst[row]);
                        row++;
                        System.out.println(row);
                    }
                }
                else {
                    lst=new String[2];
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference doctor_query = FirebaseDatabase.getInstance().getReference().child("Doctor_patient").child(Username);
        doctor_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Result will be holden Here
                if(snapshot.exists()) {
                    doctor_name=snapshot.child("Doctor").getValue().toString();
                    System.out.println(doctor_name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference Disease_query = FirebaseDatabase.getInstance().getReference().child("Patient").child(Username).child("your_disease");
        Disease_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Result will be holden Here
                row_d=0;
                int y=Integer.parseInt(Birth.substring(0,4));
                int m=Integer.parseInt(Birth.substring(5,7));
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                String formattedDate = df.format(c);
                age=Integer.parseInt(formattedDate.substring(0,4))-y;
                if(m>Integer.parseInt(formattedDate.substring(5,7)))age--;
                if(Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))>0) {
                    disease_lst = new String[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))+1];
                    disease_lst[row_d++]="Your Disease : ";
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        disease_lst[row_d] = (String.valueOf(dsp.getValue())); //add result into array list
                        row_d++;
                    }
                    if (row_d > 1) {
                        txt.append(disease_lst[0]+"\n");
                        for (int i = 1; i < row_d ; i++) {
                            txt.append(i + "." + disease_lst[i] + "\n");
                        }
                        txt.append(String.format("Your age=%d, so your safe heartbeat ", age));
                        if (age >= 18 && age <= 25) {
                            txt.append("between 60-74 in normal situation\n");
                        } else if (age >= 26 && age <= 35) {
                            txt.append("between 60-75 in normal situation\n");
                        } else if (age >= 36 && age <= 45) {
                            txt.append("between 60-76 in normal situation\n");
                        } else if (age >= 46 && age <= 55) {
                            txt.append("between 60-77 in normal situation\n");
                        } else if (age >= 56 && age <= 65) {
                            txt.append("between 60-76 in normal situation\n");
                        } else if (age >= 66) {
                            txt.append("between 60-74 in normal situation\n");
                        } else {
                            txt.append("between 60-74 in normal situation\n");
                        }
                    }
                }else{
                    txt.append(String.format("Your age=%d, so your safe heartbeat ", age));
                    if(age>=18&&age<=25){
                        txt.append("between 56-81 in normal situation\n");
                    }else if(age>=26&&age<=35){
                        txt.append("between 55-81 in normal situation\n");
                    }else if(age>=36&&age<=45){
                        txt.append("between 57-82 in normal situation\n");
                    }else if(age>=46&&age<=55){
                        txt.append("between 58-83 in normal situation\n");
                    }else if(age>=56&&age<=65){
                        txt.append("between 57-81 in normal situation\n");
                    }else if(age>=66){
                        txt.append("between 56-79 in normal situation\n");
                    }else {
                        txt.append("between 60-74 in normal situation\n");
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference mac_address_query = FirebaseDatabase.getInstance().getReference().child("Devices").child(ID_Device);
        mac_address_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Result will be holden Here
                mac_address=snapshot.child("MAC_Address").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference Doctor_query = FirebaseDatabase.getInstance().getReference().child("Doctor");
        Doctor_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Result will be holden Here
                row1=0;
                doctor_lst= new String[Integer.parseInt(String.valueOf(snapshot.getChildrenCount()+1))];
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    if(doctor_name.equals(dsp.child("Username").getValue().toString()))
                    {
                        doctor_data=new String[4];
                        doctor_data[0]=dsp.child("Full Name").getValue().toString();
                        doctor_data[1]=dsp.child("Address").getValue().toString();
                        doctor_data[2]=dsp.child("Email").getValue().toString();
                        doctor_data[3]=dsp.child("Phone Number").getValue().toString();
                        doctor_lst[row1] = String.valueOf(row1 + 1) + " . " + dsp.child("Username").getValue().toString() + "\nHis Full Name : " + dsp.child("Full Name").getValue().toString() + "\nLocation of clinic : " + dsp.child("Address").getValue().toString(); //add result into array list
                    }
                    else {
                        doctor_lst[row1] = String.valueOf(row1 + 1) + " . " + dsp.child("Username").getValue().toString() + "\nHis Full Name : " + dsp.child("Full Name").getValue().toString() + "\nLocation of clinic : " + dsp.child("Address").getValue().toString(); //add result into array list
                    }
                    System.out.println(doctor_lst[row1]);
                    row1++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        DatabaseReference message_query = FirebaseDatabase.getInstance().getReference().child("Messages").child(Username);
        message_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Result will be holden Here
                row_s=0;
                row_r=0;
                if(Integer.parseInt(String.valueOf(snapshot.getChildrenCount()))>0) {
                    int no=Integer.parseInt(String.valueOf(snapshot.getChildrenCount() + 1));
                    send_message = new String[2][no];
                    receive_message = new String[2][no];
                    int i=0;
                    while (messagesArrayList.size()>0) {
                        messagesArrayList.remove(0);
                    }
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        String d=dsp.getKey();
                        if (dsp.child("Sender").getValue().toString().equals(Username)) {
                            send_message[0][row_s] = dsp.child("Date").getValue().toString();
                            send_message[1][row_s] = dsp.child("Contain").getValue().toString();
                            messagesArrayList.add(i, new messages(send_message[1][row_s], "2"));
                            System.out.println(i+".....)"+messagesArrayList.get(i).getS_r());
                            i++;
                            row_s++;
                        }
                        if (dsp.child("Receiver").getValue().toString().equals(Username)) {
                            receive_message[0][row_r] = dsp.child("Date").getValue().toString();
                            receive_message[1][row_r] = dsp.child("Contain").getValue().toString();
                            System.out.println(row_r+"r"+ dsp.child("Contain").getValue().toString());
                            messagesArrayList.add(i, new messages(receive_message[1][row_r], "1"));
                            System.out.println(i+".....)"+messagesArrayList.get(i).getMessage());
                            i++;
                            row_r++;
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
      if(drawerLayout.isDrawerOpen(GravityCompat.START)){
          drawerLayout.closeDrawer(GravityCompat.START);
      }
    }
    public void ClickHome(View view){
        recreate();
    }
    public void ClickBPM(View view){
        redirectActivity(this,MonitorBPM.class);
    }
    public void ClickContact(View view){
        redirectActivity(this, Contact.class);
    }
    public void ClickProfile(View view){
        redirectActivity(this,Profile.class);
    }
    public void ClickAddDoctor(View view){ Patient.redirectActivity(this,AddDoctor.class); }
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
        intent.putExtra("ID_Device", ID_Device);
        intent.putExtra("Health Insure", HealthInsure);
        intent.putExtra("Birthday", Birth);
        intent.putExtra("Gender", Gender);
        intent.putExtra("Phone Number", PhoneNumber);
        intent.putExtra("Email", email);
        intent.putExtra("Address", Address);
        intent.putExtra("mac_address", mac_address);
        activity.startActivity(intent);
    }
    @Override
    protected void onPause(){
        super .onPause();
        closeDrawer(drawerLayout);
    }
}