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
    private Long mySaldo, transferAmountLong, recipientSaldo;

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

                if(makePayment(transferAmountLong)) {
                    Intent intent = new Intent(mContext, TransactionSuccessActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean makePayment(Long transferAmount) {
        //set dateformat
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String dateString = df.format(Calendar.getInstance().getTime());

        // find reference for current account
        String uid = mAuth.getUid();
        String myPhone = mAuth.getCurrentUser().getPhoneNumber();

        /**
         * get the reference to the current user and recipient
         */
        DatabaseReference paymentRef = mFirebaseDatabase
                .getReference("users/" + uid);
        DatabaseReference receivedRef = mFirebaseDatabase
                .getReference("users/" + recipientId);

        /**
         * set the new balance for both users
         */
        paymentRef.child("saldo").setValue(mySaldo - transferAmount);
        receivedRef.child("saldo").setValue(recipientSaldo + transferAmount);

        /**
         * add new transaction record for payment and receiving
         */
        String idPayment = paymentRef.push().getKey();
        paymentRef.child("payments").child(idPayment).setValue(new Transaction(
                transferAmount,
                recipientPhone,
                dateString
        ));

        String idReceived = receivedRef.push().getKey();
        receivedRef.child("received").child(idReceived).setValue(new Transaction(
                transferAmount,
                myPhone,
                dateString
        ));


        return true;
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

        transferAmountLong = Long.valueOf(transferAmount);

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
