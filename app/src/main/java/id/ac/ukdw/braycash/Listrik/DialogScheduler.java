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

public class DialogScheduler extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    private TextView startDate, endDate, startTime, endTime;
    private Spinner spinnerAction;
    private String action;

    private DialogSchedulerListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DialogSchedulerListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_set_scheduler, null);

        builder.setView(view)
                .setTitle("Set Scheduler")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date1 = startDate.getText().toString() + " " + startTime.getText().toString();
                        String date2 = endDate.getText().toString() + " " + endTime.getText().toString();
                        listener.applyTexts(date1, date2, action);
                    }
                });

        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);
        startTime = view.findViewById(R.id.startTime);
        endTime = view.findViewById(R.id.endTime);

        setSpinner(view);
        setEditTexts();

        return builder.create();
    }

    private void setSpinner(View view) {
        spinnerAction = (Spinner) view.findViewById(R.id.spinnerAction);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.action, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAction.setAdapter(adapter);
        spinnerAction.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        action = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface DialogSchedulerListener {
        void applyTexts(String date1, String date2, String action);
    }

    private void setEditTexts() {

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }

        };

        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                endDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }

        };

        final TimePickerDialog.OnTimeSetListener timeStart = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);

                String myFormat = "HH:mm"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                startTime.setText(sdf.format(myCalendar.getTime())+":00");
            }
        };

        final TimePickerDialog.OnTimeSetListener timeEnd = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);

                String myFormat = "HH:mm"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                endTime.setText(sdf.format(myCalendar.getTime())+":00");
            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateEnd,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), timeStart,
                        myCalendar.get(Calendar.HOUR),
                        myCalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), timeEnd,
                        myCalendar.get(Calendar.HOUR),
                        myCalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });
    }

}
