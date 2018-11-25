package id.ac.ukdw.braycash.History;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
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

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private Context mContext;
    private RecyclerView rcyRiwayat;
    private RiwayatAdapter riwayatAdapter;
    private static final int ACTTIVITY_NUM = 2;

    // firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mContext = HistoryActivity.this;

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
        rcyRiwayat = findViewById(R.id.rcyRiwayat);
        final List<Riwayat> listRiwayat = new ArrayList<Riwayat>();
        riwayatAdapter = new RiwayatAdapter(listRiwayat, mContext);

        //menggabungkan antara recyclerView dengan riwayatAdapter
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcyRiwayat.setLayoutManager(lm);
        rcyRiwayat.setItemAnimator(new DefaultItemAnimator());
        rcyRiwayat.setAdapter(riwayatAdapter);

        myRef.child("payments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String date = snapshot.child("date").getValue().toString();
                            String phone = snapshot.child("phone").getValue().toString();
                            String amount = snapshot.child("amount").getValue().toString();

                            listRiwayat.add(new Riwayat(date, phone, amount));
                        }
                        Collections.reverse(listRiwayat);
                        riwayatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}