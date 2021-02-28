package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class Contact extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView title;
    TextView Email,phone,address;
    Button send;
    EditText message;
    ListView listView;
    int row_s=0,row_r=0;
    boolean flag=false;
    ArrayList<messages> messagesArrayList =Patient.messagesArrayList;
    static String[][] send_message, receive_message;
    MessagesAdapter adapter;
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.lst1);
        setContentView(R.layout.activity_contact);
        drawerLayout=findViewById(R.id.drawer_layout);
        title=findViewById(R.id.tit);
        title.append("Contact");
        Email=findViewById(R.id.Email_ed);
        phone=findViewById(R.id.Phone_ed);
        address=findViewById(R.id.Address);
        message=findViewById(R.id.message);
        send=findViewById(R.id.send1);
        TextView view=findViewById(R.id.view);
        if(Patient.doctor_name.length()>2) {
            String name = Patient.doctor_data[0];
            String email = Patient.doctor_data[2];
            String Address = Patient.doctor_data[1];
            String PhoneNumber = Patient.doctor_data[3];
            view.setText("\nDoctor Information,\nName :         "+name);
            Email.setText("\nEmail :         "+email);
            phone.setText("\nPhone Number :    "+PhoneNumber);
            address.setText("\nLocation of clinic : "+Address+"\n");
        }
        @SuppressLint("CutPasteId") RecyclerView chat=findViewById(R.id.lst1);
        if(Patient.doctor_name.length()>2) {
            row_r=Patient.row_r;
            row_s = Patient.row_s;
            System.out.println("R:"+row_r);
            System.out.println("S:"+row_s);
            if (row_r > 0||row_s>0) {
                adapter = new MessagesAdapter(messagesArrayList);
                chat.setAdapter(adapter);
                chat.setLayoutManager(new LinearLayoutManager(this));
            }
            else {
                receive_message = new String[2][1];
                receive_message[0][0] = "you can send message to Dr."+Patient.doctor_data[0];
                receive_message[1][0] = "1";
                messagesArrayList = messages.create_message_list(receive_message[0],receive_message[1]);
                adapter = new MessagesAdapter(messagesArrayList);
                chat.setAdapter(adapter);
                chat.setLayoutManager(new LinearLayoutManager(this));
                flag=true;
            }
        }else{
            receive_message = new String[2][1];
            receive_message[0][0] = "you don't have doctor, please go to add doctor page and choose one of the doctors ";
            receive_message[1][0] = "1";
            messagesArrayList = messages.create_message_list(receive_message[0],receive_message[1]);
            adapter = new MessagesAdapter(messagesArrayList);
            chat.setAdapter(adapter);
            chat.setLayoutManager(new LinearLayoutManager(this));
        }
        send.setOnClickListener(v -> {
            String message_send=message.getText().toString();
            if(!message_send.equals("")) {
                if(!Patient.doctor_name.equals("")) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss", Locale.getDefault());
                    Date c = Calendar.getInstance().getTime();
                    String date = df.format(c);
                    Map<String, String> map = new HashMap<>();
                    map.put("Receiver", Patient.doctor_name);
                    map.put("Sender", Patient.Username);
                    map.put("Contain", message_send);
                    map.put("Status","w");
                    map.put("Date", date);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Messages");
                    reference.child(Patient.Username).child(date).setValue(map).addOnCompleteListener(task -> {
                        if (!flag) {
                            int size=adapter.getItemCount();
                            System.out.println("Current time => " + date);
                            System.out.println("size => " + size);
                            Patient.messagesArrayList.add(size,new messages(message_send,"2"));
                            adapter.notifyItemInserted(size);
                        }else {
                            send_message=new String[2][row_s+1];
                            send_message[0][0]=date;
                            send_message[1][0]=message_send;
                            row_s++;
                            messagesArrayList.remove(0);
                            messagesArrayList = messages.create_message_list(new String[]{message_send}, new String[]{"2"});
                            MessagesAdapter adapter = new MessagesAdapter(messagesArrayList);
                            chat.setAdapter(adapter);
                            chat.setLayoutManager(new LinearLayoutManager(this));
                            flag=false;
                        }
                    });

                }
            }
        });
    }
    public static class MessagesAdapter extends
            RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
        private final ArrayList<messages> messagesList;
        public MessagesAdapter(ArrayList<messages> messagesList0){
            messagesList=messagesList0;
            System.out.println("///////position => " + messagesList.size());
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View chat = inflater.inflate(R.layout.chat_received, parent, false);
            // Return a new holder instance
            ViewHolder holder = new ViewHolder(chat);
            return holder;

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                textView.setText(message1.getMessage());
                textView.setBackgroundResource(receiver);
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
    public void  ClickMenu(View view){
        Patient.openDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        Patient.redirectActivity(this,Patient.class);
    }
    public void ClickBPM(View view){
        Patient.redirectActivity(this,MonitorBPM.class);
    }
    public void ClickContact(View view){ recreate(); }
    public void ClickAddDoctor(View view){ Patient.redirectActivity(this,AddDoctor.class); }
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