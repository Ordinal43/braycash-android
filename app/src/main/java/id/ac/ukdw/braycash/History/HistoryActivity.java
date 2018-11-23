package id.ac.ukdw.braycash.History;

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

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private Context mContext;
    private RecyclerView rcyRiwayat;
    private RiwayatAdapter riwayatAdapter;
    private static final int ACTTIVITY_NUM = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mContext = HistoryActivity.this;

        setupBottomNavigationView();

        // ============ set up recycler view ===============
        rcyRiwayat = findViewById(R.id.rcyRiwayat);
        List<Riwayat> listRiwayat = new ArrayList<Riwayat>();
        riwayatAdapter = new RiwayatAdapter(listRiwayat, mContext);

        //menggabungkan antara recyclerView dengan riwayatAdapter
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcyRiwayat.setLayoutManager(lm);
        rcyRiwayat.setItemAnimator(new DefaultItemAnimator());
        rcyRiwayat.setAdapter(riwayatAdapter);

        //membuat data dummy
        listRiwayat.add(new Riwayat("12 November 2018", "087881810168", "50.000,00"));
        listRiwayat.add(new Riwayat("11 November 2018", "087812345678", "25.000,00"));
        listRiwayat.add(new Riwayat("10 November 2018", "087898765432", "20.000,00"));
        listRiwayat.add(new Riwayat("10 November 2018", "087898765432", "20.000,00"));
        listRiwayat.add(new Riwayat("9 November 2018", "087898765432", "20.000,00"));
        listRiwayat.add(new Riwayat("8 November 2018", "087898765432", "20.000,00"));
        listRiwayat.add(new Riwayat("7 November 2018", "087898765432", "20.000,00"));
        listRiwayat.add(new Riwayat("6 November 2018", "087898765432", "20.000,00"));
        listRiwayat.add(new Riwayat("5 November 2018", "087898765432", "20.000,00"));

        //untuk mengupdate tampilan
        riwayatAdapter.notifyDataSetChanged();
    }

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