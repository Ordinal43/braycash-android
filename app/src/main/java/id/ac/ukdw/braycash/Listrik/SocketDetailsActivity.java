package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.ukdw.braycash.R;

public class SocketDetailsActivity extends AppCompatActivity {
    private static final String TAG = "SocketDetailsActivity";

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference deviceRef;

    private TextView socketName;
    private TextView socketId;

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

        socketName = (TextView) findViewById(R.id.socketName);
        socketId = (TextView) findViewById(R.id.socketId);

        if(getIntent().hasExtra("SOCKET_ID")) {
            String id = getIntent().getStringExtra("SOCKET_ID");
            socketId.setText("ID: " + id);
            deviceRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid() + "/Device/" + id);
            deviceRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String nama = dataSnapshot.child("nama").getValue().toString();
                    String daya = "0";
                    String status = dataSnapshot.child("status").getValue().toString();
                    socketName.setText(nama);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mContext, "Connection error!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }


}
