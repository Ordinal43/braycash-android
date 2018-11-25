package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import id.ac.ukdw.braycash.Profile.ProfileActivity;
import id.ac.ukdw.braycash.R;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference userRef;

    private Context mContext;
    final HomeFragment fragment = this;
    private Button btnTopup;
    private LinearLayout menuTransfer, menuPayment, menuId;
    private TextView amountSaldo;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getActivity();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userRef = mFirebaseDatabase.getReference("users");

        getBalance();

        amountSaldo = (TextView) view.findViewById(R.id.amountSaldo);
        btnTopup = (Button) view.findViewById(R.id.btnTopup);
        menuTransfer = (LinearLayout) view.findViewById(R.id.menuTransfer);
        menuPayment = (LinearLayout) view.findViewById(R.id.menuPayment);
        menuId = (LinearLayout) view.findViewById(R.id.myID);
        
        menuTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TransferActivity.class);
                startActivity(intent);
            }
        });

        menuPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setOrientationLocked(false);
                integrator.forSupportFragment(fragment).initiateScan();
            }

        });

        menuId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopupActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String myNumber = mAuth.getCurrentUser().getPhoneNumber();
                String recipientNumber = result.getContents();

                if(recipientNumber.equals(myNumber)) {
                    Toast.makeText(mContext, "You cannot make payment to your own account", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(mContext, PaymentActivity.class);
                    intent.putExtra("RECIPIENT_PHONE", recipientNumber);
                    startActivity(intent);
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getBalance(){

        String myPhone = mAuth.getCurrentUser().getPhoneNumber();

        userRef.orderByChild("phone")
                .equalTo(myPhone)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Long mySaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                        amountSaldo.setText("" + mySaldo);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Long mySaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                        amountSaldo.setText("" + mySaldo);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(
                                mContext,
                                "Data not found!",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Toast.makeText(
                                mContext,
                                "Data not found!",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(
                                mContext,
                                "There was an error. Check your connection and try again",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
