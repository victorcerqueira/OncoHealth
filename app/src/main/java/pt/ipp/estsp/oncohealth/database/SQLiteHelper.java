package pt.ipp.estsp.oncohealth.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper class to create and maintain Health Tips table
 * @author Victor
 * @version 0.1
 * @see SQLiteOpenHelper
 * @since 0.1
 */
public class SQLiteHelper extends SQLiteOpenHelper{
    /** Name of HealthTip table */
    public static final String HEALTH_TIP_TABLE = "health_tips";
    /** Name of HealthTip id column */
    public static final String HEALTH_TIP_COLUMN_ID = "_id";
    /** Name of HealthTip name column */
    public static final String HEALTH_TIP_COLUMN_NAME = "name";
    /** Name of HealthTip short text column */
    public static final String HEALTH_TIP_COLUMN_SHORT_TEXT = "short_text";
    /** Name of HealthTip full text column */
    public static final String HEALTH_TIP_COLUMN_FULL_TEXT = "full_text";
    /** Name of HealthTip image column */
    public static final String HEALTH_TIP_COLUMN_IMAGE = "image";

    /** Name of Routine table */
    public static final String ROUTINE_TABLE = "routines";
    /** Name of Routine id column */
    public static final String ROUTINE_COLUMN_ID = "_id";
    /** Name of Routine interval column */
    public static final String ROUTINE_COLUMN_INTERVAL = "interval";
    /** Name of Routine interval repetitions */
    public static final String ROUTINE_COLUMN_REPETITIONS = "repetitions";
    /** Name of Routine images column */
    public static final String ROUTINE_COLUMN_IMAGE = "image";
    /** Name of Routine description column */
    public static final String ROUTINE_COLUMN_DESCRIPTION = "description";
    /** Name of Routine is_done column */
    public static final String ROUTINE_COLUMN_IS_DONE = "is_done";

    /** Name of database */
    private static final String DATABASE_NAME = "onco_health.db";
    /** Database version */
    private static final int DATABASE_VERSION = 2;

    /**
     * HealthTip table creation sql statement
     */
    private static final String HEALTH_TIP_DATABASE_CREATE = "create table "
            + HEALTH_TIP_TABLE + " (" + HEALTH_TIP_COLUMN_ID
            + " integer primary key, " + HEALTH_TIP_COLUMN_NAME
            + " text, " + HEALTH_TIP_COLUMN_SHORT_TEXT
            + " text, " + HEALTH_TIP_COLUMN_FULL_TEXT
            + " text, " + HEALTH_TIP_COLUMN_IMAGE
            + " blob);";
    /**
     * Routine table creation sql statement
     */
    private static final String ROUTINE_DATABASE_CREATE = "create table "
            + ROUTINE_TABLE + " (" + ROUTINE_COLUMN_ID
            + " integer primary key, " + ROUTINE_COLUMN_INTERVAL
            + " integer, " + ROUTINE_COLUMN_REPETITIONS
            + " integer, " + ROUTINE_COLUMN_IMAGE
            + " blob, " + ROUTINE_COLUMN_DESCRIPTION
            + " text, " + ROUTINE_COLUMN_IS_DONE
            + " integer);";

    /**
     * Calls {@link SQLiteOpenHelper} super constructor
     * @param context The current context
     */
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the table health_tips by running the SQL statement stored as {@link SQLiteHelper#HEALTH_TIP_DATABASE_CREATE}
     * @param database SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(HEALTH_TIP_DATABASE_CREATE);
        database.execSQL(ROUTINE_DATABASE_CREATE);
    }

    /**
     * Sample code when upgrading the database, just dropping the table and creating a new one
     * @param db SQLiteDatabase
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + HEALTH_TIP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ROUTINE_TABLE);
        onCreate(db);
    }
}
