package a2dv606.androidproject.MainWindow;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import android.widget.TimePicker;



/**
 * Created by f on 2017-03-31.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    OnTimePickedListener mCallback;
    Integer mLayoutId = null;
    public interface OnTimePickedListener {
        public void onTimePicked(int textId, int hour, int minute);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnTimePickedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTimePickedListener.");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallback = (OnTimePickedListener)getActivity();

        Bundle bundle = this.getArguments();
        mLayoutId = bundle.getInt("layoutId");
        int hour = bundle.getInt("hour");
        int minute = bundle.getInt("minute");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }


    public void onTimeSet(TimePicker view, int hour, int minute) {
        if(mCallback != null)
        {
            mCallback.onTimePicked(mLayoutId, hour, minute);
        }
    }
}

