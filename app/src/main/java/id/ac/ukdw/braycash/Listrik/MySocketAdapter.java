package id.ac.ukdw.braycash.Listrik;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.ac.ukdw.braycash.R;

public class MySocketAdapter extends RecyclerView.Adapter<MySocketAdapter.ElektronikHolder> {

    private List<Socket> listSocket;
    private Context mContext;

    public MySocketAdapter(List<Socket> listSocket, Context mContext) {
        this.listSocket = listSocket;
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
        final Socket socket = listSocket.get(i);
        elektronikHolder.txtElectronicId.setText(socket.getId());
        elektronikHolder.txtElectronicName.setText(socket.getNama());
        elektronikHolder.txtElectronicWattage.setText(socket.getDaya());
        elektronikHolder.txtElectronicStatus.setText(socket.getStatus());

        elektronikHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SocketDetailsActivity.class);
                intent.putExtra("SOCKET_ID", socket.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSocket.size();
    }

    public class ElektronikHolder extends RecyclerView.ViewHolder {
        TextView txtElectronicId, txtElectronicName, txtElectronicWattage, txtElectronicStatus;
        LinearLayout parentLayout;

        public ElektronikHolder(@NonNull View itemView) {
            super(itemView);
            txtElectronicId = itemView.findViewById(R.id.electronicId);
            txtElectronicName = itemView.findViewById(R.id.electronicName);
            txtElectronicWattage = itemView.findViewById(R.id.electronicWattage);
            txtElectronicStatus = itemView.findViewById(R.id.electronicStatus);
            parentLayout = itemView.findViewById(R.id.parentLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // maybe set an intent here with the params you want
                }
            });
        }
    }
}