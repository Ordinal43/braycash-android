package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import id.ac.ukdw.braycash.Listrik.ListrikFragment;
import id.ac.ukdw.braycash.Login.LoginActivity;
import id.ac.ukdw.braycash.R;
import id.ac.ukdw.braycash.Utils.BottomNavigationViewHelper;
import id.ac.ukdw.braycash.Utils.SectionsPagerAdapter;
import id.ac.ukdw.braycash.Utils.UniversalImageLoader;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Context mContext = HomeActivity.this;
    private static final int ACTTIVITY_NUM = 0;

    // firebase auth object
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: starting...");

        // set firebase auth object
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            initImageLoader();
            setupBottomNavigationView();
            setupViewPager();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "onStart: setup firebase auth");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
            // user is not signed in
            Log.d(TAG, "onStart: CURERENT USER NULL");
            Intent intent = new Intent(mContext, LoginActivity.class);
            /**
             * returns the user to login page
             */
            startActivity(intent);
        }
    }

    /**
     * TEMPORARY STATIC METHOD FOR DISPLAYING IMAGES
     */
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * for adding tabs Fragments
     */
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new ListrikFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_braycash);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_electric);
    }

    /**
     * BottomNavigationViewSetup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up bottomNav");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);

        // set up custom animation library
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
