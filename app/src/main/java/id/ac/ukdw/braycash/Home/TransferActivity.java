package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import id.ac.ukdw.braycash.R;

public class TransferActivity extends AppCompatActivity {
    private static final String TAG = "TransferActivity";

    private Context mContext;
    private ImageView backBtn;
    private Button btnTransfer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        mContext = TransferActivity.this;
        backBtn = (ImageView) findViewById(R.id.backArrow);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(mContext, ConfirmTransferActivity.class);
                startActivity(intent);
            }
        });
    }
}
