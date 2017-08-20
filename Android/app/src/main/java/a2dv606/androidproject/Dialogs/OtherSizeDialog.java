package a2dv606.androidproject.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainWindow.DateHandler;
import a2dv606.androidproject.MainWindow.MainActivity;
import a2dv606.androidproject.R;
import a2dv606.androidproject.Settings.PreferenceKey;
import a2dv606.androidproject.Settings.PrefsHelper;

/**
 * Created by Abeer on 4/30/2017.
 */

public class OtherSizeDialog  extends Dialog implements View.OnClickListener,NumberPicker.OnValueChangeListener{
    private Context context;
    private NumberPicker numberPicker;
    private Button setButton;
    private int NumberPickerValue;
    private DrinkDataSource db;
    private  MediaPlayer mediaPlayer;
    private final String OTHER= "other";
    public OtherSizeDialog(Context context, DrinkDataSource db) {
        super(context);
        this.context=context;
        this.db= db;


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_picker_dialog);
        numberPicker=(NumberPicker)findViewById(R.id.numberPicker);
        setButton = (Button) findViewById(R.id.set_button);
        mediaPlayer= MediaPlayer.create(context, R.raw.splash_water);
        setButton.setOnClickListener(this);
        numberPicker.setOnClickListener(this);

        setNumberPickerFormat();
        setTitle("Add drink");
    }
    private void setNumberPickerFormat() {
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                value =value*5;
                return  value+" ml";
            }
        };
        numberPicker.setFormatter(formatter);
        numberPicker.setMaxValue(300);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.set_button:
                addFromNumberPiker();

        }


    }

    private void addFromNumberPiker() {
        int preBefore= db.getConsumedPercentage();
        db.createTimeLog(NumberPickerValue,OTHER, DateHandler.getCurrentDate(),DateHandler.getCurrentTime());
        db.updateConsumedWaterForTodayDateLog(NumberPickerValue);
        updateView(preBefore);
        playSound();
        dismiss();
    }

    private void playSound() {
        if (PrefsHelper.getSoundsPrefs(context))
            mediaPlayer.start();


    }

    private void updateView(int perBefore) {
        int perValue= db.getConsumedPercentage();
        MainActivity.circleProgress.setProgress(db.getConsumedPercentage());
        MainActivity.choosenAmountTv.setText(String.valueOf(db.geConsumedWaterForToadyDateLog()+" of "+
                PrefsHelper.getWaterNeedPrefs(context)+" ml"));

            if (perBefore<100&&perValue==100) {
                congratulationDialog c = new congratulationDialog(context);
                c.show();

            }

    }



    public boolean getCongDialogPrefs()
    {   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showDialog = preferences.getBoolean("show_dialog", true);
        return showDialog;
    }
    public  void setCongDialogPrefs(boolean value)
    {   SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("show_dialog", value);
        editor.commit();
    }



    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        this.NumberPickerValue= picker.getValue()*5;
    }
}
