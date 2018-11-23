package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import id.ac.ukdw.braycash.R;

public class ScanActivity extends AppCompatActivity {
    private static final String TAG = "ScanActivity";

    private Context mContext;
    private ImageView backBtn;
    private RelativeLayout confirmScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mContext = ScanActivity.this;
        backBtn = (ImageView) findViewById(R.id.backArrow);
        confirmScan = (RelativeLayout) findViewById(R.id.confirmScan);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(mContext, ScanPaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}
