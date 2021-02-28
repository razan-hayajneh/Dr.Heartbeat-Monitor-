package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.drheartbeatmonitor.R.drawable.receiver;
import static com.example.drheartbeatmonitor.R.drawable.sender;

public class ContactDoctor extends AppCompatActivity {
    DrawerLayout drawerLayout1;
    TextView title;
    Button send,view;
    ArrayList<messages> messagesArrayList=new ArrayList<>();
    EditText message;
    TextView Patient_user;
    MessagesAdapter adapter;
    int row=0;
    static  int position=-1;
    private boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_doctor);
        drawerLayout1=findViewById(R.id.drawer_layout1);
        title=findViewById(R.id.tit1);
        title.append("Contact");
        send=findViewById(R.id.send1);
        view=findViewById(R.id.view1);
        Patient_user=findViewById(R.id.Patient_user);
        message=findViewById(R.id.message1);
        row=Doctor.rowP;
        @SuppressLint("CutPasteId") RecyclerView chat=findViewById(R.id.lst2);
        if(position!=-1) {
            Patient_user.setText(Doctor.patient_data_list[position][0]);
            if(!Doctor.messagesArrayList[position][0][0].equals("x")) {
                for (int n = 0; n < Doctor.messagesArrayList[position][0].length; n++) {
                    messagesArrayList.add(n, new messages(Doctor.messagesArrayList[position][2][n], Doctor.messagesArrayList[position][0][n]));
                }
                if (messagesArrayList.size() > 0) {

                    adapter = new MessagesAdapter(messagesArrayList);
                    chat.setAdapter(adapter);
                    chat.setLayoutManager(new LinearLayoutManager(this));
                }
            }else {
                String[][] receive_message = new String[2][1];
                receive_message[0][0] = "you can send message to " + Doctor.patient_data_list[position][0];
                receive_message[1][0] = "1";
                messagesArrayList = messages.create_message_list(receive_message[0], receive_message[1]);
                adapter = new MessagesAdapter(messagesArrayList);
                chat.setAdapter(adapter);
                chat.setLayoutManager(new LinearLayoutManager(this));
                flag = true;
            }
        }
        send.setOnClickListener(v -> {
            String message_send = message.getText().toString();
            if(position!=-1) {
                if (!message_send.equals("")) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss", Locale.getDefault());
                    Date c = Calendar.getInstance().getTime();
                    String date = df.format(c);
                    Map<String, String> map = new HashMap<>();
                    map.put("Receiver", Doctor.patient[position]);
                    map.put("Sender", Doctor.Username);
                    map.put("Contain", message_send);
                    map.put("Status","w");
                    map.put("Date", date);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Messages");
                    reference.child(Doctor.patient[position]).child(date).setValue(map).addOnCompleteListener(task -> {
                        if (!flag) {
                            int size = adapter.getItemCount();
                            System.out.println("Current time => " + date);
                            System.out.println("size => " + size);
                            messagesArrayList.add(size, new messages(message_send, "2"));
                            adapter.notifyItemInserted(size);
                        } else {
                            messagesArrayList.remove(0);
                            messagesArrayList = messages.create_message_list(new String[]{message_send}, new String[]{"2"});
                            MessagesAdapter adapter = new MessagesAdapter(messagesArrayList);
                            chat.setAdapter(adapter);
                            chat.setLayoutManager(new LinearLayoutManager(this));
                            flag = false;
                        }
                    });
                }
            }
        });
        view.setOnClickListener(v -> {
            Doctor.redirectActivity(ContactDoctor.this,Doctor.class);
            /*String patient_name=Patient_user.getText().toString();
            if(patient_name.equals("")){
                Patient_user.setError("you must  enter the patients' full name ");
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
                    Patient_user.setError("you must  enter patients' full name ");
                }
                else if (!Doctor.messagesArrayList[position][0][0].equals("x")) {
                    while (messagesArrayList.size()!=0) messagesArrayList.remove(0);
                    for (int n = 0; n < Doctor.messagesArrayList[position][0].length; n++) {
                        messagesArrayList.add(n, new messages(Doctor.messagesArrayList[position][2][n], Doctor.messagesArrayList[position][0][n]));
                    }
                    if (messagesArrayList.size() > 0) {
                        adapter = new MessagesAdapter(messagesArrayList);
                        chat.setAdapter(adapter);
                        chat.setLayoutManager(new LinearLayoutManager(ContactDoctor.this));
                    }
                } else {
                    String[][] receive_message = new String[2][1];
                    receive_message[0][0] = "You can send a message to " + Doctor.patient_data_list[index][0];
                    receive_message[1][0] = "1";
                    messagesArrayList = messages.create_message_list(receive_message[0], receive_message[1]);
                    adapter = new MessagesAdapter(messagesArrayList);
                    chat.setAdapter(adapter);
                    chat.setLayoutManager(new LinearLayoutManager(ContactDoctor.this));
                    flag = true;
                }
            }*/
        });
    }

    public static class MessagesAdapter extends
            RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
        private final ArrayList<messages> messagesList;
        public MessagesAdapter(ArrayList<messages> messagesList0){
            messagesList=messagesList0;
        }
        @NonNull
        @Override
        public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View chat = inflater.inflate(R.layout.chat_received, parent, false);
            // Return a new holder instance
            MessagesAdapter.ViewHolder holder = new MessagesAdapter.ViewHolder(chat);
            return holder;

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
            messages message1 = messagesList.get(position);
            System.out.println("position => " + position);
            // Set item views based on your views and data model
            if(message1.getS_r().equals("2")) {
                TextView textView = holder.sent;
                textView.setBackgroundResource(sender);
                textView.setText(message1.getMessage());
                TextView textView1=holder.received;
                textView1.setBackground(null);
                textView1.setText("");
                System.out.println("s_"+message1.getMessage());
            }
            if(message1.getS_r().equals("1")) {
                TextView textView = holder.received;
                textView.setBackgroundResource(receiver);
                textView.setText(message1.getMessage());
                TextView textView1=holder.sent;
                textView1.setBackground(null);
                textView1.setText("");
                System.out.println("r_"+message1.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return messagesList.size();
        }

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView received,sent;
            public Drawable r,s;
            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                received =  itemView.findViewById(R.id.received);
                sent =  itemView.findViewById(R.id.sent);
            }

        }
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
    public void ClickContactD(View view){recreate();}
    public void ClickAddPatient(View view){
        Doctor.redirectActivity(this, AddPatient.class);
    }
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