package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.ac.ukdw.braycash.Login.LoginActivity;
import id.ac.ukdw.braycash.Model.Transaction;
import id.ac.ukdw.braycash.R;

public class ConfirmPinActivity extends AppCompatActivity {
    private static final String TAG = "ConfirmPinActivity";

    private Context mContext = ConfirmPinActivity.this;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private EditText pinInput;
    private Button btnConfirmPin;
    private ProgressBar mProgressBar;

    // for transaction process
    private String recipientId, recipientPhone;
    private Long mySaldo, recipientSaldo, amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmpin);

        mAuth  = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null) {
            // user is not signed in
            Log.d(TAG, "onStart: CURRENT USER NULL");
            Intent intent = new Intent(mContext, LoginActivity.class);
            /**
             * returns the user to login page
             */
            startActivity(intent);

        }

        initWidgets();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initWidgets() {

        recipientId = getIntent().getStringExtra("RECIPIENT_ID");
        recipientPhone = getIntent().getStringExtra("RECIPIENT_PHONE");
        mySaldo = Long.parseLong(getIntent().getStringExtra("MY_SALDO"));
        recipientSaldo = Long.parseLong(getIntent().getStringExtra("RECIPIENT_SALDO"));
        amount = Long.parseLong(getIntent().getStringExtra("TRANSFER_AMOUNT"));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid());

        pinInput = (EditText) findViewById(R.id.pinInput);

        btnConfirmPin = (Button) findViewById(R.id.btnConfirmPin);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        btnConfirmPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pin = pinInput.getText().toString();

                if(pin.length() != 6) {
                    Toast.makeText(
                            mContext,
                            "PIN must be 6 characters long!",
                            Toast.LENGTH_LONG).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    DatabaseReference pinRef = myRef.child("pin");

                    pinRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue().toString().equals(pin)) {

                                if(makePayment(amount)) {
                                    Intent intent = new Intent(mContext, TransactionSuccessActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } else {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext, "PIN Tidak sesuai", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });


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

}
