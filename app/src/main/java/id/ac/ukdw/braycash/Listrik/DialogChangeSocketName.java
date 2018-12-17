package id.ac.ukdw.braycash.Listrik;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import id.ac.ukdw.braycash.R;

public class DialogChangeSocketName extends AppCompatDialogFragment {
    private EditText socketName;

    private ChangeSocketNameListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ChangeSocketNameListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_set_socket_name, null);

        builder.setView(view)
                .setTitle("Set Socket Name")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = socketName.getText().toString();
                        listener.setName(name);
                    }
                });

        socketName = (EditText) view.findViewById(R.id.socketName);

        String name = getArguments().getString("SOCKET_NAME");

        socketName.setText(name);

        return builder.create();
    }

    public interface ChangeSocketNameListener {
        void setName(String name);
    }

}
