package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import id.ac.ukdw.braycash.R;

public class ListrikFragment extends Fragment {
    private Context mContext;
    private RecyclerView rcyElektronik;
    private MySocketAdapter mySocketAdapter;
    private Button btnAddSocket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listrik, container, false);

        mContext = getActivity();

        btnAddSocket = (Button) view.findViewById(R.id.btnAddSocket);

        // ============ set up recycler view ===============
        rcyElektronik = (RecyclerView) view.findViewById(R.id.rcyElektronik);
        List<Socket> listSocket = new ArrayList<Socket>();
        mySocketAdapter = new MySocketAdapter(listSocket, mContext);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcyElektronik.setLayoutManager(lm);
        rcyElektronik.setItemAnimator(new DefaultItemAnimator());
        rcyElektronik.setAdapter(mySocketAdapter);

        listSocket.add(new Socket("1", "Kulkas", "150", "ON"));
        listSocket.add(new Socket("2", "Televisi", "100", "ON"));
        listSocket.add(new Socket("3", "Lampu Kamar", "5", "ON"));
        listSocket.add(new Socket("4", "Lampu WC", "5", "ON"));
        listSocket.add(new Socket("5", "Lampu teras", "10", "OFF"));
        listSocket.add(new Socket("6", "AC", "100", "OFF"));
        listSocket.add(new Socket("7", "Kipas Angin", "30", "ON"));

        mySocketAdapter.notifyDataSetChanged();

        btnAddSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddSocketActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
