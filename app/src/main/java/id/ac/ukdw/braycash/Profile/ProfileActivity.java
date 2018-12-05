package id.ac.ukdw.braycash.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import id.ac.ukdw.braycash.Home.HomeActivity;
import id.ac.ukdw.braycash.R;
import id.ac.ukdw.braycash.Utils.BottomNavigationViewHelper;
import id.ac.ukdw.braycash.Utils.UniversalImageLoader;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private Context mContext = ProfileActivity.this;
    private static final int ACTTIVITY_NUM = 3;

    private ImageView profilePhoto, myQRCode, backBtn;
    private TextView phoneNumber;
    private TextView displayName;
    private ProgressBar mProgressBar;

    // firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: starting...");

        setupToolbar();
        setupActivityWidgets();
        setBarcode();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra("PIN_CONFIRMED", "confirmed");
                startActivity(intent);
            }
        });
    }

    /**
     * set up all activity widgets
     */
    private void setupActivityWidgets() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = mFirebaseDatabase.getReference("users/" + mAuth.getUid());

        displayName = (TextView) findViewById(R.id.displayName);
        phoneNumber = (TextView) findViewById(R.id.phone_number);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot);
                String name = dataSnapshot.child("name").getValue().toString();
                String profileImgURL = dataSnapshot.child("profilePhoto").getValue().toString();

                displayName.setText(name);
                UniversalImageLoader.setImage(profileImgURL, profilePhoto, mProgressBar, "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        phoneNumber.setText(mAuth.getCurrentUser().getPhoneNumber());

        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);

        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
        myQRCode = (ImageView) findViewById(R.id.myQRCode);
        backBtn = (ImageView) findViewById(R.id.backArrow);
    }

    /**
     * Set up static profile image
     */
    private void setBarcode() {

        try {
            String myPhoneNumber = mAuth.getCurrentUser().getPhoneNumber();
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(myPhoneNumber, BarcodeFormat.QR_CODE, 500, 500);
            myQRCode.setImageBitmap(bitmap);
        } catch(Exception e) {
            Toast.makeText(mContext, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: go to account settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

}

