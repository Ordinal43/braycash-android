package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.ac.ukdw.braycash.R;

public class ListrikFragment extends Fragment {
    private static final String TAG = "ListrikFragment";

    private Context mContext;
    private TextView noSocket;
    private RecyclerView rcyElektronik;
    private MySocketAdapter mySocketAdapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listrik, container, false);

        mContext = getActivity();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid());

        noSocket = (TextView) view.findViewById(R.id.noSocket);
        noSocket.setVisibility(View.GONE);

        // ============ set up recycler view ===============
        rcyElektronik = (RecyclerView) view.findViewById(R.id.rcyElektronik);
        final List<Socket> listSocket = new ArrayList<Socket>();
        mySocketAdapter = new MySocketAdapter(listSocket, mContext);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcyElektronik.setLayoutManager(lm);
        rcyElektronik.setItemAnimator(new DefaultItemAnimator());
        rcyElektronik.setAdapter(mySocketAdapter);

        myRef.child("Device")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listSocket.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String id = snapshot.getKey();
                            String nama = snapshot.child("nama").getValue().toString();
                            String daya = "0";
                            String status = snapshot.child("status").getValue().toString();

                            listSocket.add(new Socket(id, nama, daya, status));
                        }

                        if(listSocket.isEmpty()) {
                            noSocket.setVisibility(View.VISIBLE);
                        }
                        mySocketAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, "Connection error!", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}
