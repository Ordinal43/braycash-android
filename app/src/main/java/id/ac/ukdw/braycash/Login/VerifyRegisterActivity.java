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

import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.R;

public class VerifyRegisterActivity extends AppCompatActivity {
    private static final String TAG = "VerifyLoginActivity";

    Context mContext;
    private EditText inputOTP;
    private Button btnVerify;
    private TextView resendOTP;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyregister);
        Log.d(TAG, "onCreate: VerifyLoginActivity start");

        initWidgets();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Verification code resend", Toast.LENGTH_SHORT);
            }
        });
    }

    private void initWidgets() {
        mContext = VerifyRegisterActivity.this;
        inputOTP = (EditText) findViewById(R.id.input_otp);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        resendOTP = (TextView) findViewById(R.id.resend_otp);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.GONE);
    }
}
