package a2dv606.androidproject.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainWindow.DateHandler;
import a2dv606.androidproject.MainWindow.MainActivity;
import a2dv606.androidproject.Settings.PrefsHelper;
import a2dv606.androidproject.R;

/**
 * Created by Abeer on 4/30/2017.
 */

public class AddDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private final String GLASS="glass";
    private final String BOTTLE="bottle";
    private int glassSize;
    private int bottleSize;
    private DrinkDataSource db;
    private MediaPlayer mediaPlayer;

    private Button otherSize,cancel, glassButton,bottleButton;

    public AddDialog(Context context, DrinkDataSource db){
        super(context);
        this.context = context;
        this.db= db;


    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_drink_dialog);
        mediaPlayer= MediaPlayer.create(context, R.raw.splash_water);
        otherSize = (Button) findViewById(R.id.other_size_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        bottleButton= ( Button)findViewById(R.id.water_bottle_button);
        glassButton=  (Button) findViewById(R.id.water_glass_button);
        bottleButton.setOnClickListener(this);
        glassButton.setOnClickListener(this);
        cancel.setOnClickListener(this);
        otherSize.setOnClickListener(this);
        setTitle("Select drink");
        loadContainerSizePrefs();

    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id){
             case R.id.water_bottle_button:
                addBottle();
                break;
            case R.id.water_glass_button:
                addGlass();
                break;
            case R.id.cancel_button:
              dismiss();
                break;
            case R.id.other_size_button:
               showOtherSizeDialog();
                break;

        }
        dismiss();
    }

    private void showOtherSizeDialog() {
        OtherSizeDialog otherSizeDialog = new OtherSizeDialog(context, db);
        otherSizeDialog.show();
        dismiss();
    }

    private void playSound() {
        if (PrefsHelper.getSoundsPrefs(context))
            mediaPlayer.start();


    }

    private void addGlass(){
       int perBefore= db.getConsumedPercentage();
        db.createTimeLog(glassSize,GLASS, DateHandler.getCurrentDate(),DateHandler.getCurrentTime());
        db.updateConsumedWaterForTodayDateLog(glassSize);


        updateView(perBefore);
        playSound();
        dismiss();
    }

    private void addBottle() {
        int perBefore= db.getConsumedPercentage();
        db.createTimeLog(bottleSize,BOTTLE,DateHandler.getCurrentDate(),DateHandler.getCurrentTime());
        db.updateConsumedWaterForTodayDateLog(bottleSize);
        updateView(perBefore);
        playSound();
        dismiss();
    }

    private void updateView(int perBefore) {
        int perValue= db.getConsumedPercentage();
        MainActivity.circleProgress.setProgress(perValue);
        MainActivity.choosenAmountTv.setText(String.valueOf(db.geConsumedWaterForToadyDateLog()+" out of "+
                PrefsHelper.getWaterNeedPrefs(context)+" ml"));

            if (perValue==100&&perBefore<100) {
                congratulationDialog c = new congratulationDialog(context);
                c.show();

            }

    }




    private void loadContainerSizePrefs() {
       String glassSizeStr= PrefsHelper.getGlassSizePrefs(context);
        String bottleSizeStr =PrefsHelper.getBottleSizePrefs(context);
        this.glassSize = Integer.valueOf(glassSizeStr);
        this.bottleSize = Integer.valueOf(bottleSizeStr);
        glassButton.setText(glassSizeStr+ " ml");
        bottleButton.setText(bottleSizeStr+ " ml");
    }

}