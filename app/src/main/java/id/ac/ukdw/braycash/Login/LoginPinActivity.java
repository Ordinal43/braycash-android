package id.ac.ukdw.braycash.Login;

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


import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.R;

public class LoginPinActivity extends AppCompatActivity {
    private static final String TAG = "LoginPinActivity";

    private Context mContext = LoginPinActivity.this;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private EditText pinInput;
    private Button btnConfirmPin;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpin);

        mAuth  = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null) {
            // user is not signed in
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        initWidgets();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initWidgets() {

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

                                Intent intent = new Intent(mContext, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("PIN_CONFIRMED", "confirmed");
                                startActivity(intent);

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


}
