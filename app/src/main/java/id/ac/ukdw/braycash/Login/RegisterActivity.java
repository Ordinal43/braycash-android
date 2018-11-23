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
import android.widget.Toast;

import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    
    private Context mContext;
    private EditText mPhone, mUsername;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    private boolean isStringNull(String string) {
        if(string.equals("")){
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidgets();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhone.getText().toString();
                String username = mUsername.getText().toString();

                if(isStringNull(phone) || isStringNull(username)) {
                    Toast.makeText(mContext, "All fields must not be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    //mProgressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(mContext, VerifyRegisterActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initWidgets() {
        mContext = RegisterActivity.this;
        mPhone = (EditText) findViewById(R.id.input_phone);
        mUsername = (EditText) findViewById(R.id.input_username);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.GONE);
    }
}
