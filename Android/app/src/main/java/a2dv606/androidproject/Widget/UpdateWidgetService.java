package a2dv606.androidproject.Widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainWindow.DateHandler;
import a2dv606.androidproject.MainWindow.MainActivity;
import a2dv606.androidproject.R;
import a2dv606.androidproject.Settings.PrefsHelper;

public class UpdateWidgetService extends Service {
    public static String SERVICE_INTENT = "service_intent";
    public static final int WIDGET_INIT = 0;
    public static final int WIDGET_UPDATE = 1;
    int a = 1;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("on start command");
        int command = intent.getIntExtra(SERVICE_INTENT, -1);
        if (intent == null) {
            return START_STICKY;
        }

        switch (command) {
            case WIDGET_INIT: {
                int widget_id = intent.getIntExtra("widget_id", -1);
                int[] widgetIds = {widget_id};
                Intent widget_intent = new Intent(getApplicationContext(), WidgetProvider.class);
                widget_intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                widget_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
                sendBroadcast(widget_intent);
                break;
            }

            case WIDGET_UPDATE: {
                int appWidgetId = intent.getIntExtra("widget_id", -1);

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout1);
                setupViews(views);
                setupOnWidgetClick(views,appWidgetId);

                break;
            }
        }
        stopSelf();
        return START_STICKY;


    }


    private void setupOnWidgetClick(RemoteViews views , int widgetId) {
        Intent intent = new Intent(UpdateWidgetService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateWidgetService.this, widgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.layout, pendingIntent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateWidgetService.this);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    private void setupViews(RemoteViews views) {
        DrinkDataSource db = new DrinkDataSource(getApplicationContext());
        db.open();
        String prc= String.valueOf( db.getConsumedPercentage())+"%";
        views.setTextViewText(R.id.drank_per_textview, prc);
        views.setTextViewText(R.id.drank_textview,String.valueOf(db.geConsumedWaterForToadyDateLog())+" out of "+
        String.valueOf(PrefsHelper.getWaterNeedPrefs(getApplicationContext())));

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

