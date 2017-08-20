package a2dv606.androidproject.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by f on 2017-03-28.
 */
public class DrinkDbHelper  extends SQLiteOpenHelper {
    public static final String Date_TABLE_NAME = "dateTable";
    public static final String TIME_TABLE_NAME = "TimeTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME_ID = "_id";
    public static final String COLUMN_WATER_NEED = "WaterNeed";
    public static final String COLUMN_WATER_DRUNK="drunkWater";
    public static final String COLUMN_WATER_DRUNK_ONCE="drunkWaterOnce";
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_TIME_DATE="date";
    public static final String COLUMN_TIME="time";
    public static final String COLUMN_TYP="containerTyp";

    public static final String DATABASE_NAME = "drinkTime00.db";
    public static final int DATABASE_VERSION = 1;



    private static final String CREATE_DATE_TABLE = "create table " +Date_TABLE_NAME
            + " (" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_WATER_NEED + " integer, "
            + COLUMN_WATER_DRUNK + " integer, "
            + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
            ");";

    private static final String CREATE_TIME_TABLE = "create table " +TIME_TABLE_NAME
            + " (" + COLUMN_TIME_ID + " integer primary key autoincrement, "
            + COLUMN_WATER_DRUNK_ONCE + " integer, "
            + COLUMN_TYP + " TEXT, "
            + COLUMN_TIME_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP , "
            + COLUMN_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    public  DrinkDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATE_TABLE);
        db.execSQL(CREATE_TIME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w( DrinkDbHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Date_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME);
        onCreate(db);
    }
}

