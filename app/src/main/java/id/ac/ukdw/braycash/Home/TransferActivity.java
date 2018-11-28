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

import id.ac.ukdw.braycash.R;

public class TransferActivity extends AppCompatActivity {
    private static final String TAG = "TransferActivity";

    private Context mContext;
    private ImageView backBtn;
    private EditText phoneNumber;
    private EditText transferAmount;
    private Button btnTransfer;
    private TextView currentSaldo;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference userRef;

    private Long mySaldo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        initWidgets();
        getMyBalance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = phoneNumber.getText().toString();
                final String transfer = transferAmount.getText().toString();

                if(phone.isEmpty() || transfer.isEmpty()) {
                    Toast.makeText(mContext, "All fields must not be empty", Toast.LENGTH_SHORT).show();
                } else {

                    if(phone.equals(mAuth.getCurrentUser().getPhoneNumber())) {
                        Toast.makeText(mContext, "You cannot transfer to your own account", Toast.LENGTH_SHORT).show();
                    } else {
                        userRef.orderByChild("phone")
                                .equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){
                                    Toast.makeText(mContext, "Number doesn't exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference myRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid());

                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                        String recipientId = snapshot.getKey();
                                        String amountString = transferAmount.getText().toString();

                                        Long transferAmountLong = Long.valueOf(amountString);
                                        if(transferAmountLong < 5000) {
                                            Toast.makeText(
                                                    mContext,
                                                    "Minimum transaction is Rp 5000",
                                                    Toast.LENGTH_SHORT).show();
                                        } else if(mySaldo < transferAmountLong) {
                                            Toast.makeText(
                                                    mContext,
                                                    "Not enough balance!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {

                                            Intent intent = new Intent(mContext, ConfirmTransferActivity.class);
                                            intent.putExtra("RECIPIENT_ID", recipientId);
                                            intent.putExtra("TRANSFER_AMOUNT", amountString);
                                            intent.putExtra("MY_SALDO", mySaldo.toString());
                                            intent.putExtra("RECIPIENT_PHONE", phoneNumber.getText().toString());

                                            startActivity(intent);

                                        }


                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(mContext, "You cannot transfer to your own account", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void initWidgets() {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userRef = mFirebaseDatabase.getReference("users");

        mContext = TransferActivity.this;
        backBtn = (ImageView) findViewById(R.id.backArrow);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        transferAmount = (EditText) findViewById(R.id.transferAmount);
        currentSaldo = (TextView) findViewById(R.id.currentSaldo);

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
