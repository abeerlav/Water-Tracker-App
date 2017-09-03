package a2dv606.androidproject.MainWindow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

import a2dv606.androidproject.BroadcastReceivers.DateLogBroadcastReceiver;
import a2dv606.androidproject.BroadcastReceivers.StopNotificationBroadcastReceiver;
import a2dv606.androidproject.Notifications.NotificationReciever;
import a2dv606.androidproject.Settings.PreferenceKey;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Abeer on 5/2/2017.
 */

public class AlarmHelper {


    public static void setDBAlarm(Context context) {
        boolean alarmDown =(PendingIntent.getBroadcast(context, 90,
                new Intent(context,DateLogBroadcastReceiver.class),
                PendingIntent.FLAG_NO_CREATE) == null);
        if(alarmDown) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Intent mIntent = new Intent(context,DateLogBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.
                    getBroadcast(context,90,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,pendingIntent);

    }}
    public static void setNotificationsAlarm(Context mContext) {


        boolean isAlarmUp = (PendingIntent.getBroadcast(mContext, 101, new Intent(mContext, NotificationReciever.class)
                , PendingIntent.FLAG_UPDATE_CURRENT)!= null);
              if(isAlarmUp) {
                 stopNotificationsAlarm(mContext);
                }
                  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                  String from = prefs.getString(PreferenceKey.FROM_KEY, "8:0");
                  int interval = prefs.getInt(PreferenceKey.PREF_INTERVAL, 2);
                  String[] values = from.split(":");
                  String hr = values[0];
                  String mt = values[1];

                  Calendar calendar = Calendar.getInstance();
                  Calendar now = Calendar.getInstance();

                  calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
                  calendar.set(Calendar.MINUTE, Integer.valueOf(mt));
                  calendar.set(Calendar.SECOND, 0);
                  Intent nIntent = new Intent(mContext, NotificationReciever.class);
                  PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 101, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                  AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                  if (now.after(calendar)) {
                      alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis()+600000, 1000 * 60 * 60 * interval, pendingIntent);
                  } else {
                      alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * interval, pendingIntent);
                  }

              }





    public static void stopNotificationsAlarm(Context mContext){
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(ALARM_SERVICE);
        Intent mIntent = new Intent(mContext,NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(mContext,101,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmManager!=null)
        {  alarmManager.cancel(pendingIntent);}

    }

    public static void setCancelNotificationAlarm(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String to = prefs.getString(PreferenceKey.TO_KEY, "20:0");
        String[] values = to.split(":");
        String hr = values[0];
        String mt = values[1];
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
        cal.set(Calendar.MINUTE, Integer.valueOf(mt));
        cal.set(Calendar.SECOND, 0);
        Intent nIntent = new Intent(context, StopNotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 102, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);


    }

}

