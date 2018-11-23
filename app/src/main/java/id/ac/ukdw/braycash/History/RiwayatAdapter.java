package id.ac.ukdw.braycash.History;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.ac.ukdw.braycash.R;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.RiwayatHolder>{
    private List<Riwayat> listRiwayat; //untuk data dummy
    private Context mContext; //supaya tahu siapa activity yang memakai class RiwayatAdapter

    public RiwayatAdapter(List<Riwayat> listRiwayat, Context mContext){
        this.listRiwayat = listRiwayat;
        this.mContext = mContext;
    }

    //Fungsi ini berfungsi untuk mengconvert layout item_riwayat.xmlxml menjadi object di java
    @NonNull
    @Override
    public RiwayatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //digunakan untuk parameter di class RiwayatHolder
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_riwayat, viewGroup, false);

        return new RiwayatHolder(itemView);
    }
    //function ini berfungsi untuk memasang data riwayat ke layout item)riwayat.xml yang sudah dibuat
    @Override
    public void onBindViewHolder(@NonNull RiwayatHolder riwayatHolder, int i) {
        Riwayat riwayat = listRiwayat.get(i);
        riwayatHolder.txtTanggal.setText(riwayat.tanggal);
        riwayatHolder.txtNomor_Tujuan.setText(riwayat.nomor_tujuan);
        riwayatHolder.txtNominal.setText(riwayat.nominal);
    }
    //mendapatkan jumlah data riwayat yang dimiliki
    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }

    public class RiwayatHolder extends RecyclerView.ViewHolder{
        public TextView txtTanggal, txtNomor_Tujuan, txtNominal;

        public RiwayatHolder(View itemView){
            super(itemView);
            txtTanggal = itemView.findViewById(R.id.txtTanggal);
            txtNomor_Tujuan = itemView.findViewById(R.id.txtNomor_Tujuan);
            txtNominal = itemView.findViewById(R.id.txtNominal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
