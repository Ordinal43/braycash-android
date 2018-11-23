package id.ac.ukdw.braycash.Notif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.ac.ukdw.braycash.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifHolder>{
    private List<Notification> listNotification; //untuk data dummy
    private Context mContext; //supaya tahu siapa activity yang memakai class RiwayatAdapter

    public NotificationAdapter(List<Notification> listNotification, Context mContext){
        this.listNotification = listNotification;
        this.mContext = mContext;
    }

    //Fungsi ini berfungsi untuk mengconvert layout item_riwayat.xmlxml menjadi object di java
    @NonNull
    @Override
    public NotifHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //digunakan untuk parameter di class RiwayatHolder
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notif, viewGroup, false);

        return new NotifHolder(itemView);
    }
    //function ini berfungsi untuk memasang data riwayat ke layout item)riwayat.xml yang sudah dibuat
    @Override
    public void onBindViewHolder(@NonNull NotifHolder notifHolder, int i) {
        Notification notification = listNotification.get(i);
        notifHolder.txtTanggal.setText(notification.tanggal);
        notifHolder.txtNomorSumber.setText(notification.nomor_tujuan);
        notifHolder.txtNominal.setText(notification.nominal);
    }

    //mendapatkan jumlah data riwayat yang dimiliki
    @Override
    public int getItemCount() {
        return listNotification.size();
    }

    public class NotifHolder extends RecyclerView.ViewHolder{
        public TextView txtTanggal, txtNomorSumber, txtNominal;

        public NotifHolder(View itemView){
            super(itemView);
            txtTanggal = itemView.findViewById(R.id.txtTanggal);
            txtNomorSumber = itemView.findViewById(R.id.txtNomorSumber);
            txtNominal = itemView.findViewById(R.id.txtNominal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
