package a2dv606.androidproject.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainWindow.AlarmHelper;
import a2dv606.androidproject.Settings.PrefsHelper;

/**
 * Created by Abeer on 5/2/2017.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context pContext, Intent intent) {
        DrinkDataSource db= new DrinkDataSource(pContext);
        db.open();
       int waterNeed= PrefsHelper.getWaterNeedPrefs(pContext);
       db.createMissingDateLog(0,waterNeed);
       AlarmHelper.setDBAlarm(pContext);
    }
}


