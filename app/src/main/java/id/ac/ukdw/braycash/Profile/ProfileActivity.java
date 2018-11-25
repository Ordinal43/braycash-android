package id.ac.ukdw.braycash.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import id.ac.ukdw.braycash.R;
import id.ac.ukdw.braycash.Utils.BottomNavigationViewHelper;
import id.ac.ukdw.braycash.Utils.UniversalImageLoader;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private Context mContext = ProfileActivity.this;
    private static final int ACTTIVITY_NUM = 3;

    private ImageView profilePhoto, myQRCode, myBarCode, backBtn;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: starting...");

        setupToolbar();
        setupActivityWidgets();
        setImages();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * set up all activity widgets
     */
    private void setupActivityWidgets() {
        mAuth = FirebaseAuth.getInstance();

        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);


        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
        myQRCode = (ImageView) findViewById(R.id.myQRCode);
        backBtn = (ImageView) findViewById(R.id.backArrow);
    }

    /**
     * Set up static profile image
     */
    private void setImages() {
        String profileImgURL = "pm1.narvii.com/6767/b7b73269eba2d87cad4d1d8b44946561a096782av2_hq.jpg";

        try {
            String myPhoneNumber = mAuth.getCurrentUser().getPhoneNumber();
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(myPhoneNumber, BarcodeFormat.QR_CODE, 500, 500);
            myQRCode.setImageBitmap(bitmap);
        } catch(Exception e) {
            Toast.makeText(mContext, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
        }

        UniversalImageLoader.setImage(profileImgURL, profilePhoto, mProgressBar, "https://");
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

