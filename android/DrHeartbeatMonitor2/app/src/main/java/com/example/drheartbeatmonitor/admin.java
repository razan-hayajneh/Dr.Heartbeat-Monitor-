package com.example.drheartbeatmonitor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class admin extends AppCompatActivity {
    EditText id_dev,mac_address;
    Button add;
    Button display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admim);
        id_dev = (EditText) findViewById(R.id.id_dev);
        mac_address=findViewById(R.id.mac_address);
        add = (Button) findViewById(R.id.add);
        display = (Button) findViewById(R.id.Display);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query id_query = FirebaseDatabase.getInstance().getReference().child("Devices");
                id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            showMessage("Error", "NO DATA FOUND IN DATABASE");
                            return;
                        } else {
                            Iterable<DataSnapshot> data = snapshot.getChildren();
                            String d="";
                            int c=1;
                            for(DataSnapshot dataSnapshot:data)
                            {
                                d+=c+"."+dataSnapshot.child("ID_Device").getValue().toString()+" : "+dataSnapshot.child("MAC_Address").getValue().toString()+"\n";
                                c++;
                            }
                            showMessage("Devices in Database", d);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_d = id_dev.getText().toString();
                String mac = mac_address.getText().toString();

                Map<String,String> newDev=new HashMap<>();
                newDev.put("ID_Device",id_d);
                newDev.put("MAC_Address",mac);
                if (id_dev.getText().toString().equals(""))
                    Toast.makeText(admin.this, "ENTER device number", Toast.LENGTH_LONG).show();
                else {
                    Query id_query = FirebaseDatabase.getInstance().getReference().child("Devices").orderByChild("ID_Device").equalTo(id_d);
                    id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount()==0)
                            {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Devices");
                                reference.child(id_d).setValue(newDev).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(admin.this, "Successfully adding  :) ", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(admin.this, "Error happened, try again... ", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(admin.this, "This device existed in my database\nif this new device enter correct id ", Toast.LENGTH_LONG).show();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
    }
    private void showMessage(String title, String data) {
        AlertDialog.Builder builder= new AlertDialog.Builder(admin.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(data);
        builder.show();
    }
}