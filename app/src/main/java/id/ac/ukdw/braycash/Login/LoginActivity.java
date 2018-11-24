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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mPhone;
    private Button btnLogin;

    // firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = LoginActivity.this;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPhone = (EditText) findViewById(R.id.input_phone);

        mProgressBar.setVisibility(View.GONE);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        init();
    }

    // ======================================== firebase ========================================

    private boolean isStringNull(String string) {
        if(string.equals("")){
            return true;
        } else {
            return false;
        }
    }

    private void init() {

        mPhone.requestFocus();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mPhone.getText().toString();

                if(isStringNull(phoneNumber)) {
                    Toast.makeText(mContext, "You must enter a registered phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    checkNumberExists();
                }

            }
        });

        TextView linkSignUp = (TextView) findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkNumberExists(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneNumber = mPhone.getText().toString();

                boolean exist = false;

                for (DataSnapshot users: dataSnapshot.getChildren()) {
                    for(DataSnapshot user: users.getChildren()) {
                        String phone = (String) user.child("phone").getValue();
                        // check if phone is already registered
                        if(phoneNumber.equals(phone)) {
                            exist = true;
                            break;
                        }
                    }
                }

                if(!exist) {
                    Toast.makeText(mContext, "Number not registered!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, VerifyLoginActivity.class);
                    intent.putExtra("PHONE_NUMBER", phoneNumber);
                    startActivity(intent);
                }
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
