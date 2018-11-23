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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.R;

public class VerifyLoginActivity extends AppCompatActivity {
    private static final String TAG = "VerifyLoginActivity";

    Context mContext;

    private FirebaseAuth mAuth;


    private EditText inputOTP;
    private Button btnVerify;
    private TextView resendOTP;
    private ProgressBar mProgressBar;

    private String phoneNumber;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: " + phoneNumber);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifylogin);
        Log.d(TAG, "onCreate: VerifyLoginActivity start");

        // set firebase auth object
        mAuth = FirebaseAuth.getInstance();

        initWidgets();

        sendVerificationCode();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textOTP = inputOTP.getText().toString();

                if(textOTP.equals("")){
                    Toast.makeText(mContext, "Please input a code", Toast.LENGTH_SHORT);
                } else {
                    verifySignIn(textOTP);
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Verification code resend", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "onStart: setup firebase auth");
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    // ======================================= SIGN IN =====================================
    // check if otp is correct
    private void verifySignIn(String textOTP) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, textOTP);
        signInWithPhoneAuthCredential(credential);

    }
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //signs the user in with the credentials
                            mAuth.signInWithCredential(credential)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Intent intent = new Intent(mContext, HomeActivity.class);

                                            // Erase all previous intents
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


    private void initWidgets() {
        mContext = VerifyLoginActivity.this;
        inputOTP = (EditText) findViewById(R.id.input_otp);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        resendOTP = (TextView) findViewById(R.id.resend_otp);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.GONE);
        phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");

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

    // init function for verifying login
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Toast.makeText(mContext, "Phone already verified", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(mContext, "Invalid phone number", Toast.LENGTH_SHORT).show();
            finish();
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
