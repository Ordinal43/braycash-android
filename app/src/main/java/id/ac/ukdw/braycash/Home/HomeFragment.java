package id.ac.ukdw.braycash.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import id.ac.ukdw.braycash.Profile.ProfileActivity;
import id.ac.ukdw.braycash.R;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private Context mContext;
    private Button btnTopup;
    private LinearLayout menuTransfer, menuPayment, menuId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getActivity();
        btnTopup = (Button) view.findViewById(R.id.btnTopup);
        menuTransfer = (LinearLayout) view.findViewById(R.id.menuTransfer);
        menuPayment = (LinearLayout) view.findViewById(R.id.menuPayment);
        menuId = (LinearLayout) view.findViewById(R.id.myID);
        
        menuTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TransferActivity.class);
                startActivity(intent);
            }
        });
        
        menuPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ScanActivity.class);
                startActivity(intent);
            }

        });

        menuId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopupActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
