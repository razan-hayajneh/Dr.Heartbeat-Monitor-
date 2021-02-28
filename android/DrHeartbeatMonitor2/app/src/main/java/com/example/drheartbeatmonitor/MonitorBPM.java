package com.example.drheartbeatmonitor;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MonitorBPM extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView title;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    int[] BPM = new int[50];
    int c = 0, c1 = 0;
    boolean flag = false;
    ListView list_date;
    int BPM_avg = 0;
    int row;
    String[] lst = new String[Patient.row+2];
    char range = 'm';
    TextView avg;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String Username = Patient.Username;
        setContentView(R.layout.activity_monitor_b_p_m);
        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.tit);
        title.append("BPM Monitor");
        avg = findViewById(R.id.avg);
        TextView gra1 = findViewById(R.id.gra1);
        GraphView graph2 = findViewById(R.id.graph2);
        graph2.setTitle("Current Heartbeat rate");
        graph2.getGridLabelRenderer().setVerticalAxisTitle("BPM values");
        graph2.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
        graph2.getGridLabelRenderer().setHorizontalAxisTitleColor(250);
        graph2.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph2.getGridLabelRenderer().setVerticalLabelsVisible(true);
        graph2.getViewport().setMinY(60.0);
        graph2.getViewport().setMaxY(80.0);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(51.0);
        graph2.getViewport().setBackgroundColor(255);
        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setScrollable(true);
        if (Patient.row == 0) {
            gra1.append("\nThere\'s no old data!!");
        }
        else {
            list_date = findViewById(R.id.list_date);
            lst[0] = "Date                                BPM";
            row = 1;
            for (; row <= Patient.row; row++) {
                if (Patient.lst[row-1] != null) {
                    lst[row] = Patient.lst[row - 1].substring(0, 4) + "/" + Patient.lst[row - 1].substring(4, 6) + "/" + Patient.lst[row - 1].substring(6, 8) + "." + Patient.lst[row - 1].substring(8,10)+"           "+Patient.lst[row - 1].substring(10);
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lst);
            list_date.setAdapter(arrayAdapter);
        }
        ///"98:D3:32:30:82:85"
        String mac_address = Patient.mac_address;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(mac_address);
        btAdapter.enable();
        BluetoothDevice hc05 = btAdapter.getRemoteDevice(mac_address);
        System.out.println(hc05.getName());
        if(!hc05.equals(null)) {
            BluetoothSocket btSocket = null;
            int counter = 0;
            do {
                try {
                    btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                    System.out.println(hc05.createRfcommSocketToServiceRecord(mUUID));
                    btSocket.connect();
                    System.out.println("Connected successfully");
                    flag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            } while (!btSocket.isConnected() && counter < 2);
            if (counter < 2) {
                if (btSocket.isConnected()) {
                    InputStream inputStream = null;
                    try {
                        inputStream = btSocket.getInputStream();
                        inputStream.skip(inputStream.available());
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    while (c < BPM.length) {
                        String readBPM = "";
                        byte b = 0;
                        try {
                            b = (byte) inputStream.read();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        while ((char) b != '#') {
                            readBPM = readBPM + "" + (char) b;
                            try {
                                b = (byte) inputStream.read();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!readBPM.equals("")) {
                            System.out.println(Integer.parseInt(readBPM));
                            BPM[c] = Integer.parseInt(readBPM);
                            c++;
                        }
                    }
                    if (c > 0) {
                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(c1, BPM[c1])
                        });
                        BPM_avg += BPM[c1];
                        int i = 1;
                        c1 = 1;
                        for (; c1 < c-1; c1++, i++) {
                            BPM_avg += BPM[c1];
                            DataPoint dataPoint = new DataPoint(c1, BPM[c1]);
                            series1.appendData(dataPoint, true, 50);
                        }
                        BPM_avg /= i;
                        int max=0;
                        int min=150;
                        for (int value : BPM) {
                            if (value > max) max = value;
                            else if (value < min) min = value;
                        }
                        graph2.getViewport().setMinY(min-1);
                        graph2.getViewport().setMaxY(max+1);
                        graph2.addSeries(series1);
                        String date = new SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(new Date());
                        int co = 0;
                        while (co < Patient.row) {
                            System.out.println(Patient.lst[co]);
                            if (date.equals(Patient.lst[co].substring(0, 10))) {
                                break;
                            }
                            co++;
                        }
                        int index = co;
                        System.out.println(Patient.row);
                        System.out.println(row);
                        if (index < Patient.row) {
                            BPM_avg += Integer.parseInt(Objects.requireNonNull(Patient.lst[index].substring(13)));
                            BPM_avg /= 2;
                            Patient.lst[index] = date + "  " + String.valueOf(BPM_avg);
                            lst[index + 1] = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8) + "." + date.substring(8) + "             " + String.valueOf(BPM_avg);
                        } else {
                            Patient.lst[Patient.row] = date + "  " + String.valueOf(BPM_avg);
                            lst[row] = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8) + "." + date.substring(8) + "             " + String.valueOf(BPM_avg);
                            Patient.row++;
                            row++;
                        }
                        if ( BPM_avg> 100) {
                            range = 'h';
                            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm", Locale.getDefault());
                            Date c = Calendar.getInstance().getTime();
                            String date1 = df.format(c);
                            Map<String, String> map = new HashMap<>();
                            map.put("Doctor", Patient.doctor_name);
                            map.put("Patient", Patient.Username);
                            map.put("BPM_avg", String.valueOf(BPM_avg));
                            map.put("Reason", "rapid");
                            map.put("Status","w");
                            map.put("Date", date1);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Danger");
                            reference.child(Patient.Username).child(date).setValue(map).addOnCompleteListener(task -> {
                            });
                        } else if (BPM_avg < 60) {
                            range = 'l';
                            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss", Locale.getDefault());
                            Date c = Calendar.getInstance().getTime();
                            String date1 = df.format(c);
                            Map<String, String> map = new HashMap<>();
                            map.put("Doctor", Patient.doctor_name);
                            map.put("Patient", Patient.Username);
                            map.put("BPM_avg", String.valueOf(BPM_avg));
                            map.put("Reason", "slow");
                            map.put("Status","w");
                            map.put("Date", date1);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Danger");
                            reference.child(Patient.Username).child(date).setValue(map).addOnCompleteListener(task -> {
                            });
                        } else {
                            range = 'm';
                        }
                        avg.setText(String.format("BPM Average=%d", BPM_avg));
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("BPM_Data");
                        reference.child(Username).child(date).setValue(String.valueOf(BPM_avg)).addOnCompleteListener(task -> Toast.makeText(MonitorBPM.this, "", Toast.LENGTH_LONG).show());
                    }
                }
                try {
                    btSocket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            else {
                showMessage("Device","Device is off or out of range");
            }

        }
        else {

            showMessage("Bluetooth","Can't find device between your paired bluetooth devices\npair it then try again");
        }
    }
    public void ClickMenu(View view) {
        Patient.openDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        Patient.redirectActivity(this, Patient.class);
    }

    public void ClickBPM(View view) {
        recreate();
    }

    public void ClickContact(View view) {
        Patient.redirectActivity(this, Contact.class);
    }
    public void ClickAddDoctor(View view){ Patient.redirectActivity(this,AddDoctor.class); }
    public void ClickProfile(View view) {
        Patient.redirectActivity(this, Profile.class);
    }

    public void ClickLogOut(View view) {
        Patient.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Patient.closeDrawer(drawerLayout);
    }
    private void showMessage(String title, String data) {
        AlertDialog.Builder builder= new AlertDialog.Builder(MonitorBPM.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(data);
        builder.show();
    }
}