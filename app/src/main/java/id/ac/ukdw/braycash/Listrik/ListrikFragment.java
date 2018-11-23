package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import id.ac.ukdw.braycash.R;

public class ListrikFragment extends Fragment {
    private Context mContext;
    private RecyclerView rcyElektronik;
    private ElektronikAdapter elektronikAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listrik, container, false);

        mContext = getActivity();

        // ============ set up recycler view ===============
        rcyElektronik = (RecyclerView) view.findViewById(R.id.rcyElektronik);
        List<Elektronik> listElektronik = new ArrayList<Elektronik>();
        elektronikAdapter = new ElektronikAdapter(listElektronik, mContext);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        rcyElektronik.setLayoutManager(lm);
        rcyElektronik.setItemAnimator(new DefaultItemAnimator());
        rcyElektronik.setAdapter(elektronikAdapter);

        listElektronik.add(new Elektronik("1", "Kulkas", "150", "ON"));
        listElektronik.add(new Elektronik("2", "Televisi", "100", "ON"));
        listElektronik.add(new Elektronik("3", "Lampu Kamar", "5", "ON"));
        listElektronik.add(new Elektronik("4", "Lampu WC", "5", "ON"));
        listElektronik.add(new Elektronik("5", "Lampu teras", "10", "OFF"));
        listElektronik.add(new Elektronik("6", "AC", "100", "OFF"));
        listElektronik.add(new Elektronik("7", "Kipas Angin", "30", "ON"));

        elektronikAdapter.notifyDataSetChanged();

        return view;
    }
}
