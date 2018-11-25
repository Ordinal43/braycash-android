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

import id.ac.ukdw.braycash.Login.VerifyRegisterActivity;
import id.ac.ukdw.braycash.R;

public class ScanPaymentActivity extends AppCompatActivity {

    private static final String TAG = "ScanPaymentActivity";

    private Context mContext;
    private Button btnPayment;
    private ImageView backBtn;
    private TextView recipientName, recipientNumber, currentSaldo;

    private EditText transferAmountText;

    private String phoneNumberText;
    private Long saldo;


    // firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanpayment);

        initWidgets();

        getRecipientName();
        getMyBalance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if balance is enough
                Long transferAmount = Long.valueOf(transferAmountText.getText().toString());
                if(saldo < transferAmount) {
                    Toast.makeText(
                            mContext,
                            "Not enough balance!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    finish();
                    Intent intent = new Intent(mContext, TransactionSuccessActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void initWidgets() {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userRef = mFirebaseDatabase.getReference("users");

        mContext = ScanPaymentActivity.this;
        btnPayment = (Button) findViewById(R.id.btnPayment);
        backBtn = (ImageView) findViewById(R.id.backArrow);

        recipientName = (TextView) findViewById(R.id.recipientName);
        recipientNumber = (TextView) findViewById(R.id.recipientPhone);
        currentSaldo = (TextView) findViewById(R.id.currentSaldo);
        transferAmountText = (EditText) findViewById(R.id.transferAmount);


        phoneNumberText = getIntent().getStringExtra("RECIPIENT_PHONE");

        recipientNumber.setText(phoneNumberText);

    }

    private void getRecipientName(){

        String recipientPhone = recipientNumber.getText().toString();

        userRef.orderByChild("phone")
                .equalTo(recipientPhone)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        recipientName.setText(name);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        recipientName.setText(name);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    private void getMyBalance(){

        String myPhone = mAuth.getCurrentUser().getPhoneNumber();

        userRef.orderByChild("phone")
                .equalTo(myPhone)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        saldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                        currentSaldo.setText("Rp " + saldo);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        saldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                        currentSaldo.setText("Rp " + saldo);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
