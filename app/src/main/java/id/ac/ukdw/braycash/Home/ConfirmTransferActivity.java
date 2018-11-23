package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import id.ac.ukdw.braycash.R;

public class ConfirmTransferActivity extends AppCompatActivity {

    private Context mContext;
    private TextView confirmPhone;
    private TextView confirmAmount;
    private ImageView backBtn;
    private Button btnConfirmTransfer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmtransfer);

        mContext = ConfirmTransferActivity.this;
        confirmPhone = (TextView) findViewById(R.id.confirm_phone_number);
        confirmAmount = (TextView) findViewById(R.id.confirmTransferAmount);
        backBtn = (ImageView) findViewById(R.id.backArrow);
        btnConfirmTransfer = (Button) findViewById(R.id.btnConfirmTransfer);

        confirmPhone.setText("087838766032");
        confirmAmount.setText("75000");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(mContext, TransactionSuccessActivity.class);
                startActivity(intent);
            }
        });

    }
}
