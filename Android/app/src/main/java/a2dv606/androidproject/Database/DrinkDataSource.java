package a2dv606.androidproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import a2dv606.androidproject.MainWindow.DateHandler;
import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.Model.TimeLog;

/**
 * Created by f on 2017-03-28.
 */
public class DrinkDataSource {
    private SQLiteDatabase database;
    private DrinkDbHelper dbHelper;

    private String[] allDateColumns = {DrinkDbHelper.COLUMN_ID,
            DrinkDbHelper.COLUMN_WATER_NEED, DrinkDbHelper.COLUMN_WATER_DRUNK ,DrinkDbHelper.COLUMN_DATE};

    private String[] allTimeColumns = {DrinkDbHelper.COLUMN_TIME_ID,
            DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE,DrinkDbHelper.COLUMN_TYP ,DrinkDbHelper.COLUMN_TIME_DATE,DrinkDbHelper.COLUMN_TIME};

    public DrinkDataSource(Context context) {
        dbHelper = new DrinkDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


   public DateLog createDateLog(int amount,int n,String d) {
     if(! isCurrentDateExist(d)){
        ContentValues values = new ContentValues();
        values.put(DrinkDbHelper.COLUMN_WATER_DRUNK, amount);
        values.put(DrinkDbHelper.COLUMN_WATER_NEED, n);
        values.put(DrinkDbHelper.COLUMN_DATE, d);
        long insertId = database.insert(DrinkDbHelper.Date_TABLE_NAME, null, values);
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                allDateColumns, DrinkDbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DateLog newDateLog = cursorToDataLog(cursor);
        cursor.close();
        return newDateLog;
    }
       return null;
    }
    public static List<String> getDaysBetweenDates(String startDate, String endDate)
    {
        List<String> dates = new ArrayList<String>();
        Calendar calendar = new GregorianCalendar();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date sDate = format.parse(startDate);
            Date eDate = format.parse(endDate);

        calendar.setTime(sDate);

        while (calendar.getTime().before(eDate))
        {
            Date result = calendar.getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String reportDate = df.format(result);
            dates.add(reportDate);
            calendar.add(Calendar.DATE, 1);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }
    public String getLastDay() {
        String date = null;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,new String []{
                DrinkDbHelper.COLUMN_DATE } , null, null, null,null, DrinkDbHelper.COLUMN_DATE + " DESC", " 1");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(DrinkDbHelper.COLUMN_DATE));
        }
        cursor.close();
        return date;
    }
    public void createMissingDateLog(int amount,int n) {

        List<DateLog> dateLog = new ArrayList<DateLog>();
        String day=null;
        ContentValues values = new ContentValues();
        Cursor cursor=null;
        String startDate = getLastDay();
        String endDate = DateHandler.getCurrentDate();
        List days = getDaysBetweenDates(startDate, endDate);
        for (int i = 1; i < days.size(); i++) {
            day = (String) days.get(i);
            System.out.println("days " + day);

            values.put(DrinkDbHelper.COLUMN_WATER_DRUNK, amount);
            values.put(DrinkDbHelper.COLUMN_WATER_NEED, n);
            values.put(DrinkDbHelper.COLUMN_DATE, day);

            long insertId = database.insert(DrinkDbHelper.Date_TABLE_NAME, null, values);
            cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                    allDateColumns, DrinkDbHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
        }
        if(day!=null&&!isCurrentDateExist(day)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DateLog newDateLog = cursorToDataLog(cursor);
                dateLog.add(newDateLog);
            }
            cursor.close();

        }

        }

    private boolean isCurrentDateExist(String d) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = fmt.parse(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fmt = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr =fmt.format(date);
            Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,new String []{DrinkDbHelper.COLUMN_DATE},
                    DrinkDbHelper.COLUMN_TIME_DATE + " like '%"+dateStr+"%' ", null,null,null,null,null);
            if(cursor.getCount()>0)
            return true;
            else
            return false;
    }

    public void close() {
        dbHelper.close();
    }

    public int updateConsumedWaterInDateLog( int removedAmount ,String date) {
        int TotalDrank = getConsumedAmount(date) + removedAmount;
        if(TotalDrank<0)
            TotalDrank=0;
        System.out.println("total consumed "+TotalDrank);
        ContentValues cv = new ContentValues();
        cv.put(DrinkDbHelper.COLUMN_WATER_DRUNK, TotalDrank);
        return database.update(DrinkDbHelper.Date_TABLE_NAME, cv,DrinkDbHelper.COLUMN_DATE + " like '%"+date+"%' ",null);
    }

    public boolean updateConsumedWaterForTodayDateLog( int amount ) {
       int consumed = geConsumedWaterForToadyDateLog() + amount;
        ContentValues cv = new ContentValues();
        cv.put(DrinkDbHelper.COLUMN_WATER_DRUNK, consumed);
        return database.update(DrinkDbHelper.Date_TABLE_NAME, cv,DrinkDbHelper.COLUMN_ID + "= (SELECT  MAX(" + DrinkDbHelper.COLUMN_ID + " ) from "+DrinkDbHelper.Date_TABLE_NAME+" )", null)>0;
    }

    public boolean updateWaterNeedForTodayDateLog(int waterNeed ) {
        System.out.println("water need" +waterNeed);

        ContentValues cv = new ContentValues();
        cv.put(DrinkDbHelper.COLUMN_WATER_NEED, waterNeed);
        return database.update(DrinkDbHelper.Date_TABLE_NAME, cv,DrinkDbHelper.COLUMN_ID + "= (SELECT  MAX(" + DrinkDbHelper.COLUMN_ID + " ) from "+DrinkDbHelper.Date_TABLE_NAME+" )", null)>0;
    }
    public int geConsumedWaterForToadyDateLog() {
        int waterAmount = 0;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                new String[]{DrinkDbHelper.COLUMN_ID,
                        DrinkDbHelper.COLUMN_WATER_DRUNK, DrinkDbHelper.COLUMN_WATER_NEED}, null, null, null, null, DrinkDbHelper.COLUMN_ID + " DESC", " 1");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            waterAmount = cursor.getInt(cursor.getColumnIndex(DrinkDbHelper.COLUMN_WATER_DRUNK));
        }
        cursor.close();
        return waterAmount;
    }

    public int getConsumedAmount(String date) {
        int consumed = 0;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                new String[]{DrinkDbHelper.COLUMN_ID,
                        DrinkDbHelper.COLUMN_WATER_DRUNK, DrinkDbHelper.COLUMN_WATER_NEED}, DrinkDbHelper.COLUMN_DATE + " like '%"+date+"%' ", null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            consumed = cursor.getInt(cursor.getColumnIndex(DrinkDbHelper.COLUMN_WATER_DRUNK));
            System.out.println("consumed "+consumed);
        }
        cursor.close();
        return consumed;
    }

    public int getConsumedPercentage() {
        int  waterNeed=0;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                new String[]{DrinkDbHelper.COLUMN_ID,
                        DrinkDbHelper.COLUMN_WATER_NEED}, null, null, null, null, DrinkDbHelper.COLUMN_ID + " DESC", " 1");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            waterNeed = cursor.getInt(cursor.getColumnIndex( DrinkDbHelper.COLUMN_WATER_NEED));
        }
        cursor.close();
        if(waterNeed==0)
            return 0;
         int value= geConsumedWaterForToadyDateLog()*100/waterNeed;
           if (value>100)
               return 100;
        return value;
    }

    public List<DateLog> getAllDateLogs() {
        List<DateLog> dateLog = new ArrayList<DateLog>();
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                allDateColumns  , null, null, null, null,DrinkDbHelper.COLUMN_ID+" DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DateLog task = cursorToDataLog(cursor);
            System.out.println("here "+task.getWaterNeed());
            dateLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return dateLog;
    }

    private DateLog cursorToDataLog(Cursor cursor) {
        DateLog date = new DateLog();
        date.setID(cursor.getLong(0));
        date.setWaterNeed(cursor.getInt(1));
        date.setWaterDrunk(cursor.getInt(2));
        date.setDate(cursor.getString(3));
        return date;
    }

    public TimeLog createTimeLog (int amount,String typ,String date,String time) {
        ContentValues values = new ContentValues();
        values.put(DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE, amount);
        values.put(DrinkDbHelper.COLUMN_TYP, typ);
        values.put(DrinkDbHelper.COLUMN_TIME_DATE, date);
        values.put(DrinkDbHelper.COLUMN_TIME, time);
        long insertId = database.insert(DrinkDbHelper.TIME_TABLE_NAME, null, values);
        Cursor cursor = database.query(DrinkDbHelper.TIME_TABLE_NAME,
                allTimeColumns, DrinkDbHelper.COLUMN_TIME_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TimeLog newTime = cursorToTimeLog(cursor);
        cursor.close();
        return newTime;
    }
    private TimeLog cursorToTimeLog(Cursor cursor) {
        TimeLog time = new TimeLog();
        time.setID(cursor.getLong(0));
        time.setAmount(cursor.getInt(1));
        time.setContainerTyp(cursor.getString(2));
        time.setDate(cursor.getString(3));
        time.setTime(cursor.getString(4));
        return time;
    }

    public List<TimeLog> getAllTimeLogs(String sortBy,String date) {
        List<TimeLog> timeLog = new ArrayList<TimeLog>();
        Cursor cursor = database.query(DrinkDbHelper.TIME_TABLE_NAME,
                allTimeColumns,DrinkDbHelper.COLUMN_TIME_DATE + " like '%"+date+"%' ", null,null,null,sortBy,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeLog task = cursorToTimeLog(cursor);
            timeLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return timeLog;
    }

    public void deleteTimeLog(TimeLog data) {
        Long id = data.getID();
        database.delete(DrinkDbHelper.TIME_TABLE_NAME,
                DrinkDbHelper.COLUMN_TIME_ID + "=" + id, null);
    }

    public boolean updateTimeLog(int ID, int drankAmount,String containerTyp) {
        ContentValues cv = new ContentValues();
        cv.put(DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE, drankAmount);
        cv.put(DrinkDbHelper.COLUMN_TYP, containerTyp);
        return database.update(DrinkDbHelper.TIME_TABLE_NAME, cv,
                DrinkDbHelper.COLUMN_TIME_ID + "=" + ID, null) > 0;
    }



    public String getYear() {
        String date = null;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,new String []{
                DrinkDbHelper.COLUMN_DATE },  DrinkDbHelper.COLUMN_DATE +" BETWEEN datetime('now', 'start of year') AND datetime('now', 'localtime')", null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(DrinkDbHelper.COLUMN_DATE));
        }
        cursor.close();
        return date;
    }

    public String getMonth() {
        String date = null;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,new String []{
                DrinkDbHelper.COLUMN_DATE },  DrinkDbHelper.COLUMN_DATE +" BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')", null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(DrinkDbHelper.COLUMN_DATE));
        }
        cursor.close();
        return date;

    }
    public String getWeek() {
        String date = null;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,new String []{
                DrinkDbHelper.COLUMN_DATE },  DrinkDbHelper.COLUMN_DATE +" BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')", null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(DrinkDbHelper.COLUMN_DATE));
        }
        cursor.close();
        return date;

    }

    public ArrayList<TimeLog>getDrinkByDay(){
        ArrayList<TimeLog> timeLog = new ArrayList<TimeLog>();
        Cursor cursor = database.query(DrinkDbHelper.TIME_TABLE_NAME,
           allTimeColumns , DrinkDbHelper.COLUMN_TIME_DATE + " BETWEEN date('now', 'start of day') AND datetime('now', 'localtime')", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TimeLog task = cursorToTimeLog(cursor);
            timeLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return timeLog;

    }


    public List<DateLog> getDrinkByWeek() {
        ArrayList<DateLog> dateLog = new ArrayList<DateLog>();
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                allDateColumns , DrinkDbHelper.COLUMN_DATE + " BETWEEN datetime('now', '-7 days') AND datetime('now', 'localtime')", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DateLog task = cursorToDataLog(cursor);
            dateLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return dateLog;
    }


    public String getCurrentDay() {
        String date = null;
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,new String []{
                DrinkDbHelper.COLUMN_DATE },  DrinkDbHelper.COLUMN_DATE + " BETWEEN datetime('now', 'start of day') AND datetime('now', 'localtime')", null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(DrinkDbHelper.COLUMN_DATE));

        }
        cursor.close();
        return date;
    }


    public List<DateLog> getDrinkByMonth() {
        ArrayList<DateLog> dateLog = new ArrayList<DateLog>();
        Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,
                allDateColumns , DrinkDbHelper.COLUMN_DATE + " BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime')", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DateLog task = cursorToDataLog(cursor);
            dateLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return dateLog;

    }

   public List<DateLog> getDrinkByYear() {
        ArrayList<DateLog> dateLog = new ArrayList<DateLog>();
       Cursor cursor = database.query(DrinkDbHelper.Date_TABLE_NAME,new String[]{DrinkDbHelper.COLUMN_ID,
               DrinkDbHelper.COLUMN_WATER_NEED, "SUM("+DrinkDbHelper.COLUMN_WATER_DRUNK+")"
               ,DrinkDbHelper.COLUMN_DATE},DrinkDbHelper.COLUMN_DATE+ " BETWEEN datetime('now', 'start of year') AND datetime('now', 'localtime')", null, " strftime('%m',"
               +DrinkDbHelper.COLUMN_DATE+")", null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DateLog task = cursorToDataLog(cursor);
            dateLog.add(task);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return dateLog;
    }

    public String  sortByTimeAsc(){return DrinkDbHelper.COLUMN_TIME + " ASC ";}
    public String  sortByTimeDesc(){return DrinkDbHelper.COLUMN_TIME + " DESC ";}
    public String sortByAmountDesc(){return DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE + " DESC ";}
    public String sortByAmountAsc(){ return DrinkDbHelper.COLUMN_WATER_DRUNK_ONCE + " ASC" ;}

}