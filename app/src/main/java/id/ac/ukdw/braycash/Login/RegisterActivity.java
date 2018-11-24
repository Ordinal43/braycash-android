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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    
    private Context mContext;
    private EditText mPhone, mUsername;
    private Button btnRegister;
    private ProgressBar mProgressBar;

    // firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

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

                    checkNumberExists();
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

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mProgressBar.setVisibility(View.GONE);
    }

    private void checkNumberExists(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneNumber = mPhone.getText().toString();
                String username = mUsername.getText().toString();
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

                if(exist) {
                    Toast.makeText(mContext, "Number already registered", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, VerifyRegisterActivity.class);
                    intent.putExtra("PHONE_NUMBER", phoneNumber);
                    intent.putExtra("NAME", username);
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
