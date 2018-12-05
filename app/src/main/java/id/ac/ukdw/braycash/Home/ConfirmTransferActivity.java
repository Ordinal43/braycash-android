package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.ac.ukdw.braycash.Model.Transaction;
import id.ac.ukdw.braycash.R;

public class ConfirmTransferActivity extends AppCompatActivity {

    private Context mContext;
    private TextView confirmName;
    private TextView confirmPhone;
    private TextView confirmAmount;
    private ImageView backBtn;
    private Button btnConfirmTransfer;

    private String transferAmount;
    private String recipientId, recipientPhone;
    private Long mySaldo, recipientSaldo;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference recipientRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmtransfer);

        initWidgets();

        getRecipient();

        recipientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                confirmName.setText(dataSnapshot.child("name").getValue().toString());
                confirmPhone.setText(dataSnapshot.child("phone").getValue().toString());
                confirmAmount.setText(transferAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConfirmPinTransactionActivity.class);
                intent.putExtra("RECIPIENT_ID", recipientId);
                intent.putExtra("RECIPIENT_PHONE", recipientPhone);
                intent.putExtra("MY_SALDO", mySaldo.toString());
                intent.putExtra("RECIPIENT_SALDO", recipientSaldo.toString());
                intent.putExtra("TRANSFER_AMOUNT", transferAmount);
                startActivity(intent);
            }
        });

    }


    private void initWidgets() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        mContext = ConfirmTransferActivity.this;
        confirmName = (TextView) findViewById(R.id.confirm_name);
        confirmPhone = (TextView) findViewById(R.id.confirm_phone_number);
        confirmAmount = (TextView) findViewById(R.id.confirmTransferAmount);
        backBtn = (ImageView) findViewById(R.id.backArrow);
        btnConfirmTransfer = (Button) findViewById(R.id.btnConfirmTransfer);

        recipientId = getIntent().getStringExtra("RECIPIENT_ID");
        transferAmount = getIntent().getStringExtra("TRANSFER_AMOUNT");
        mySaldo = Long.valueOf(getIntent().getStringExtra("MY_SALDO"));
        recipientPhone = getIntent().getStringExtra("RECIPIENT_PHONE");

        recipientRef = mFirebaseDatabase.getReference("users/" + recipientId);
    }

    private void getRecipient(){
        DatabaseReference userRef = mFirebaseDatabase.getReference("users");

        userRef.orderByChild("phone")
                .equalTo(recipientPhone)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        recipientId = dataSnapshot.getKey();
                        recipientSaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        recipientId = dataSnapshot.getKey();
                        recipientSaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(
                                mContext,
                                "Data not found!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Toast.makeText(
                                mContext,
                                "Data not found!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(
                                mContext,
                                "There was an error. Check your connection and try again",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

}
