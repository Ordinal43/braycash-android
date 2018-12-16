package id.ac.ukdw.braycash.Notif;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.ac.ukdw.braycash.R;
import id.ac.ukdw.braycash.Utils.BottomNavigationViewHelper;

public class NotifActivity extends AppCompatActivity {

    private static final String TAG = "NotifActivity";
    private Context mContext;
    private RecyclerView rcyNotif;
    private NotificationAdapter notifAdapter;
    private static final int ACTTIVITY_NUM = 1;

    // firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        mContext = NotifActivity.this;

        initWidgets();
        setupBottomNavigationView();
        initRecycler();
    }

    private void initWidgets() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid());
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up bottomNav");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);

        // set up custom animation library
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void initRecycler() {
        // ============ set up recycler view ===============
        rcyNotif = (RecyclerView) findViewById(R.id.rcyNotif);
        final List<Notification> listNotif = new ArrayList<Notification>();
        notifAdapter = new NotificationAdapter(listNotif, mContext);

        // menggabungkan antara recyclerView dengan riwayatAdapter
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcyNotif.setLayoutManager(lm);
        rcyNotif.setItemAnimator(new DefaultItemAnimator());
        rcyNotif.setAdapter(notifAdapter);

        myRef.child("received")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String date = snapshot.child("date").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();
                    String amount = snapshot.child("amount").getValue().toString();

                    listNotif.add(new Notification(date, phone, amount));
                }
                Collections.reverse(listNotif);
                notifAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Connection error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
