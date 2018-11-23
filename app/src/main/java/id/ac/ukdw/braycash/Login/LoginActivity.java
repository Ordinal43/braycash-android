package id.ac.ukdw.braycash.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.ac.ukdw.braycash.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set firebase auth object
        mAuth = FirebaseAuth.getInstance();

        mContext = LoginActivity.this;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPhone = (EditText) findViewById(R.id.input_phone);

        mProgressBar.setVisibility(View.GONE);

        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "onStart: setup firebase auth");
        FirebaseUser currentUser = mAuth.getCurrentUser();
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
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhone.getText().toString();

                if(isStringNull(phone)) {
                    Toast.makeText(mContext, "You must enter a registered phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    //mProgressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(mContext, VerifyLoginActivity.class);
                    startActivity(intent);

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
}
