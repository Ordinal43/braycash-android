package id.ac.ukdw.braycash.Notif;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import id.ac.ukdw.braycash.R;
import id.ac.ukdw.braycash.Utils.BottomNavigationViewHelper;

public class NotifActivity extends AppCompatActivity {

    private static final String TAG = "NotifActivity";
    private Context mContext;
    private RecyclerView rcyNotif;
    private NotificationAdapter notifAdapter;
    private static final int ACTTIVITY_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        mContext = NotifActivity.this;
        setupBottomNavigationView();

        // ============ set up recycler view ===============
        rcyNotif = (RecyclerView) findViewById(R.id.rcyNotif);
        List<Notification> listNotif = new ArrayList<Notification>();
        notifAdapter = new NotificationAdapter(listNotif, mContext);

        //menggabungkan antara recyclerView dengan riwayatAdapter
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcyNotif.setLayoutManager(lm);
        rcyNotif.setItemAnimator(new DefaultItemAnimator());
        rcyNotif.setAdapter(notifAdapter);

        //membuat data dummy
        listNotif.add(new Notification("12 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("11 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("10 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("9 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("8 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("7 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("6 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("5 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("4 November 2018", "087881810168", "50.000,00"));
        listNotif.add(new Notification("3 November 2018", "087881810168", "50.000,00"));


        //untuk mengupdate tampilan
        notifAdapter.notifyDataSetChanged();
    }

//    BottomNavigationViewSetup
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
