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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.Model.User;
import id.ac.ukdw.braycash.R;

public class VerifyRegisterActivity extends AppCompatActivity {
    private static final String TAG = "VerifyLoginActivity";

    Context mContext;

    private EditText inputOTP;
    private Button btnVerify;
    private TextView resendOTP;
    private ProgressBar mProgressBar;

    private String phoneNumber;
    private String name;

    //firebase database stuff
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyregister);
        Log.d(TAG, "onCreate: VerifyLoginActivity start");

        initWidgets();

        sendVerificationCode();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textOTP = inputOTP.getText().toString();

                if(textOTP.equals("")){
                    Toast.makeText(mContext, "Please input a code", Toast.LENGTH_SHORT).show();
                } else {
                    verifyRegister(textOTP);
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode();

            }
        });

    }

    private void initWidgets() {
        // set firebase stuff
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mContext = VerifyRegisterActivity.this;
        inputOTP = (EditText) findViewById(R.id.input_otp);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        resendOTP = (TextView) findViewById(R.id.resend_otp);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");
        name = getIntent().getStringExtra("NAME");

        mProgressBar.setVisibility(View.GONE);
    }

    // ======================================= SIGN IN =====================================
    // check if otp is correct
    private void verifyRegister(String textOTP) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, textOTP);
        signInWithPhoneAuthCredential(credential);

    }
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // set the database root to the user
                            DatabaseReference userRef = mFirebaseDatabase.getReference("users");
                            // add new user data to the database
                            String uid = mAuth.getUid();


                            User newUser = new User(name, phoneNumber, "",new Long(0), "https://ssl.gstatic.com/images/branding/product/1x/avatar_square_grey_512dp.png");

                            userRef.child(uid).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(mContext, "Code is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // ================================ SENDING SMS ===========================
    private void sendVerificationCode(){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,        // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallback

    }

    private void resendVerificationCode() {
        Log.d(TAG, "sendVerificationCode: " + phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,        // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                mResendToken);      // ForceResendingToken from callbacks
    }

    // init function for verifying login
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Toast.makeText(mContext, "Phone registered!", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(mContext, "Invalid phone number", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
            Toast.makeText(mContext, "Code sent", Toast.LENGTH_SHORT).show();
            super.onCodeSent(verificationId, token);
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

}
