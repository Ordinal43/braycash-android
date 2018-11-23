package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.ac.ukdw.braycash.R;

public class ElektronikAdapter extends RecyclerView.Adapter<ElektronikAdapter.ElektronikHolder> {

    private List<Elektronik> listElektronik;
    private Context mContext;

    public ElektronikAdapter(List<Elektronik> listElektronik, Context mContext) {
        this.listElektronik = listElektronik;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ElektronikHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_elektronik, viewGroup, false);

        return new ElektronikHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ElektronikHolder elektronikHolder, int i) {
        Elektronik elektronik = listElektronik.get(i);
        elektronikHolder.txtElectronicId.setText(elektronik.getId());
        elektronikHolder.txtElectronicName.setText(elektronik.getNama());
        elektronikHolder.txtElectronicWattage.setText(elektronik.getDaya());
        elektronikHolder.txtElectronicStatus.setText(elektronik.getStatus());
    }

    @Override
    public int getItemCount() {
        return listElektronik.size();
    }

    public class ElektronikHolder extends RecyclerView.ViewHolder {
        public TextView txtElectronicId, txtElectronicName, txtElectronicWattage, txtElectronicStatus;

        public ElektronikHolder(@NonNull View itemView) {
            super(itemView);
            txtElectronicId = itemView.findViewById(R.id.electronicId);
            txtElectronicName = itemView.findViewById(R.id.electronicName);
            txtElectronicWattage = itemView.findViewById(R.id.electronicWattage);
            txtElectronicStatus = itemView.findViewById(R.id.electronicStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // maybe set an intent here with the params you want
                }
            });
        }
    }
}
