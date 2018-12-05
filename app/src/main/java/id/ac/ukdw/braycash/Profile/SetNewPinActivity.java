package id.ac.ukdw.braycash.Profile;

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
import id.ac.ukdw.braycash.Login.LoginActivity;
import id.ac.ukdw.braycash.R;

public class SetNewPinActivity extends AppCompatActivity {
    private static final String TAG = "SetNewPinActivity";

    private Context mContext = SetNewPinActivity.this;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private EditText inputPin;
    private Button btnSetPin;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setnewpin);

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

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid());

        inputPin = (EditText) findViewById(R.id.inputPin);

        btnSetPin = (Button) findViewById(R.id.btnSetPin);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        btnSetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = inputPin.getText().toString();

                if(pin.length() != 6) {
                    Toast.makeText(
                            mContext,
                            "PIN must be 6 characters long!",
                            Toast.LENGTH_LONG).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    DatabaseReference pinRef = myRef.child("pin");
                    pinRef.setValue(pin);

                    pinRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Toast.makeText(mContext, "New PIN set!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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
