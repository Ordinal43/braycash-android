package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.ac.ukdw.braycash.R;

public class SocketDetailsActivity extends AppCompatActivity
        implements DialogScheduler.DialogSchedulerListener, DialogDeleteScheduler.DeleteSchedulerListener, DialogChangeSocketName.ChangeSocketNameListener
{
    private static final String TAG = "SocketDetailsActivity";

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference deviceRef;

    private ImageView backBtn;
    private GraphView graph;
    private TextView socketName;
    private TextView socketId;
    private Switch switchSocket;
    private TextView switchSocketStatus;

    private TextView txtScheduleStart, txtScheduleEnd, txtAction;
    private Button btnOpenDialog;
    private Button btnDeleteSchedule;

    private boolean isSwitchSet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_details);

        initWidgets();
    }

    private void initWidgets() {
        mContext = SocketDetailsActivity.this;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        backBtn = (ImageView) findViewById(R.id.backArrow);
        socketName = (TextView) findViewById(R.id.socketName);
        socketId = (TextView) findViewById(R.id.socketId);
        switchSocket = (Switch) findViewById(R.id.switchSocket);
        switchSocketStatus = (TextView) findViewById(R.id.switchSocketStatus);

        txtScheduleStart = (TextView) findViewById(R.id.txtScheduleStart);
        txtScheduleEnd = (TextView) findViewById(R.id.txtScheduleEnd);
        txtAction = (TextView) findViewById(R.id.txtAction);

        btnOpenDialog = (Button) findViewById(R.id.btnOpenDialog);
        btnDeleteSchedule = (Button) findViewById(R.id.btnDeleteSchedule);

        socketName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangeSocketName();
            }
        });

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        btnDeleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().hasExtra("SOCKET_ID")) {


            String id = getIntent().getStringExtra("SOCKET_ID");
            socketId.setText(id);
            deviceRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid() + "/Device/" + id);
            deviceRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String nama = dataSnapshot.child("nama").getValue().toString();
                    String daya = "0";
                    String status = dataSnapshot.child("status").getValue().toString();
                    String startSchedule = dataSnapshot.child("schedule_start").getValue().toString();
                    String endSchedule = dataSnapshot.child("schedule_end").getValue().toString();
                    String actionSchedule = dataSnapshot.child("schedule_action").getValue().toString();

                    if(status.equals("on")) {
                        switchSocket.setChecked(true);
                        switchSocketStatus.setText("ON");
                    } else {
                        switchSocket.setChecked(false);
                        switchSocketStatus.setText("OFF");
                    }

                    isSwitchSet = true;
                    socketName.setText(nama);
                    txtAction.setText(actionSchedule);
                    txtScheduleStart.setText(startSchedule);
                    txtScheduleEnd.setText(endSchedule);

                    LinearLayout linSchedule = (LinearLayout) findViewById(R.id.linSchedule);
                    if(actionSchedule.equals("")) {
                        linSchedule.setVisibility(View.GONE);
                    } else {
                        linSchedule.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mContext, "Connection error!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        initGraph();

        switchSocket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // only run after the firebase retrieval is finished (isSwitchSet becomes TRUE)
                if(isSwitchSet) {
                    if(isChecked) {
                        switchSocketStatus.setText("ON");
                        deviceRef.child("status").setValue(new String("on"));
                    } else {
                        switchSocketStatus.setText("OFF");
                        deviceRef.child("status").setValue(new String("off"));
                    }
                }
            }
        });
    }

    private void initGraph() {
        graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0.3),
                new DataPoint(1, 0.5),
                new DataPoint(2, 0.7),
                new DataPoint(3, 1),
                new DataPoint(4, 2)
        });
        graph.addSeries(series);
    }

    public void openChangeSocketName() {
        DialogChangeSocketName dcs = new DialogChangeSocketName();
        Bundle args = new Bundle();
        args.putString("SOCKET_NAME", socketName.getText().toString());
        dcs.setArguments(args);

        dcs.show(getSupportFragmentManager(), "change socket name dialog");
    }

    public void openDialog() {
        DialogScheduler ds = new DialogScheduler();
        ds.show(getSupportFragmentManager(), "scheduler dialog");
    }

    public void openDeleteDialog() {
        DialogDeleteScheduler dds = new DialogDeleteScheduler();
        dds.show(getSupportFragmentManager(), "delete scheduler dialog");
    }

    @Override
    public void applyTexts(String date1, String date2, String action) {
        // check dates
        boolean isValid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateStart, dateEnd;

        try {
            dateStart = sdf.parse(date1);
            dateEnd = sdf.parse(date2);

            if(dateStart.after(new Date()) ) {
                Log.d(TAG, "applyTexts: startdate valid");
                if(dateEnd.after(dateStart)) {
                    Log.d(TAG, "applyTexts: all valid");
                    isValid = true;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(isValid) {
            deviceRef.child("schedule_start").setValue(date1);
            deviceRef.child("schedule_end").setValue(date2);
            deviceRef.child("schedule_action").setValue(action);
            Toast.makeText(mContext, "Schedule set", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Date invalid!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void deleteScheduler() {
        deviceRef.child("schedule_start").setValue("");
        deviceRef.child("schedule_end").setValue("");
        deviceRef.child("schedule_action").setValue("");

        Toast.makeText(mContext, "Schedule deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setName(String name) {
        if(name.equals("")) {
            Toast.makeText(mContext, "Name is empty!", Toast.LENGTH_SHORT).show();
        } else {
            deviceRef.child("nama").setValue(name);
            Toast.makeText(mContext, "Name set", Toast.LENGTH_SHORT).show();
        }
    }
}
