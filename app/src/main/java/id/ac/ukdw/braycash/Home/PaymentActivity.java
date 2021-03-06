package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import id.ac.ukdw.braycash.R;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";

    private Context mContext;
    private Button btnPayment;
    private ImageView backBtn;
    private TextView recipientName, recipientNumber, currentSaldo;

    private EditText transferAmountText;

    private String recipientId, recipientPhone;
    private Long mySaldo, recipientSaldo;


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

        getRecipient();
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
                String amountString = transferAmountText.getText().toString();

                if(amountString.isEmpty()) {
                    Toast.makeText(
                            mContext,
                            "You must input a number",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Long transferAmount = Long.valueOf(amountString);
                if(transferAmount < 5000) {
                    Toast.makeText(
                            mContext,
                            "Minimum transaction is Rp 5.000",
                            Toast.LENGTH_SHORT).show();
                } else if(mySaldo < transferAmount) {
                    Toast.makeText(
                            mContext,
                            "Not enough balance!",
                            Toast.LENGTH_SHORT).show();
                } else if(transferAmount > 500000) {
                    Toast.makeText(
                            mContext,
                            "Maximum transaction is Rp 500.000",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(mContext, ConfirmPinTransactionActivity.class);
                    intent.putExtra("RECIPIENT_ID", recipientId);
                    intent.putExtra("RECIPIENT_PHONE", recipientPhone);
                    intent.putExtra("MY_SALDO", mySaldo.toString());
                    intent.putExtra("RECIPIENT_SALDO", recipientSaldo.toString());
                    intent.putExtra("TRANSFER_AMOUNT", transferAmount.toString());

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

        mContext = PaymentActivity.this;
        btnPayment = (Button) findViewById(R.id.btnPayment);
        backBtn = (ImageView) findViewById(R.id.backArrow);

        recipientName = (TextView) findViewById(R.id.recipientName);
        recipientNumber = (TextView) findViewById(R.id.recipientPhone);
        currentSaldo = (TextView) findViewById(R.id.currentSaldo);
        transferAmountText = (EditText) findViewById(R.id.transferAmount);

        recipientPhone = getIntent().getStringExtra("RECIPIENT_PHONE");

        recipientNumber.setText(recipientPhone);

    }

    private void getRecipient(){

        userRef.orderByChild("phone")
                .equalTo(recipientPhone)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        recipientId = dataSnapshot.getKey();
                        recipientSaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());

                        String name = dataSnapshot.child("name").getValue().toString();
                        recipientName.setText(name);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        recipientId = dataSnapshot.getKey();
                        recipientSaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());

                        String name = dataSnapshot.child("name").getValue().toString();
                        recipientName.setText(name);
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

    private void getMyBalance(){

        String myPhone = mAuth.getCurrentUser().getPhoneNumber();

        userRef.orderByChild("phone")
                .equalTo(myPhone)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        mySaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                        currentSaldo.setText("Rp " + mySaldo);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        mySaldo = Long.valueOf(dataSnapshot.child("saldo").getValue().toString());
                        currentSaldo.setText("Rp " + mySaldo);
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
