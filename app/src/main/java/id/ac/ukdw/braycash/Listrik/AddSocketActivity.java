package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.List;

import id.ac.ukdw.braycash.R;
import id.ac.ukdw.braycash.mqtt.Mqtt;

public class AddSocketActivity extends AppCompatActivity {
    private Context mContext;
    private Mqtt mqtt;

    private ImageView backBtn;
    private RecyclerView rcySocket;
    private MySocketAdapter mySocketAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_socket);

        mContext = AddSocketActivity.this;
        mAuth = FirebaseAuth.getInstance();

        mqtt = new Mqtt("ricky7171", "sukukata123", mAuth.getUid());
        try {
            mqtt.konek(mContext);
            Toast.makeText(mContext, "Connected!", Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            Toast.makeText(mContext, "Failed to connect", Toast.LENGTH_SHORT).show();
        }
//        mqtt.subscribe("main");

        // ============ set up recycler view ===============
        rcySocket = (RecyclerView) findViewById(R.id.rcySocket);
        List<Socket> listSocket = new ArrayList<Socket>();
        mySocketAdapter = new MySocketAdapter(listSocket, mContext);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcySocket.setLayoutManager(lm);
        rcySocket.setItemAnimator(new DefaultItemAnimator());
        rcySocket.setAdapter(mySocketAdapter);

        listSocket.add(new Socket("1", "Kulkas", "150", "ON"));
        listSocket.add(new Socket("2", "Televisi", "100", "ON"));
        listSocket.add(new Socket("3", "Lampu Kamar", "5", "ON"));
        listSocket.add(new Socket("4", "Lampu WC", "5", "ON"));

        mySocketAdapter.notifyDataSetChanged();

        backBtn = (ImageView) findViewById(R.id.backArrow);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
