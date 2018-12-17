package id.ac.ukdw.braycash.Listrik;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.ac.ukdw.braycash.R;

public class DialogDeleteScheduler extends AppCompatDialogFragment {

    private DeleteSchedulerListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DeleteSchedulerListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_delete, null);

        builder.setView(view)
                .setTitle("Set Scheduler")
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.deleteScheduler();
                    }
                });


        return builder.create();
    }

    public interface DeleteSchedulerListener {
        void deleteScheduler();
    }

}
