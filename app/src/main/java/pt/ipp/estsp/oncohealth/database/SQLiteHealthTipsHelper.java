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
public class SQLiteHealthTipsHelper extends SQLiteOpenHelper{
    /** Name of HealthTip table */
    public static final String TABLE = "health_tips";
    /** Name of HealthTip id column */
    public static final String COLUMN_ID = "_id";
    /** Name of HealthTip name column */
    public static final String COLUMN_NAME = "name";
    /** Name of HealthTip short text column */
    public static final String COLUMN_SHORT_TEXT = "short_text";
    /** Name of HealthTip full text column */
    public static final String COLUMN_FULL_TEXT = "full_text";
    /** Name of HealthTip image column */
    public static final String COLUMN_IMAGE = "image";

    /** Name of database */
    private static final String DATABASE_NAME = "onco_health.db";
    /** Database version (versions handling not implemented, expected to always be 1) */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table creation sql statement
     */
    private static final String DATABASE_CREATE = "create table "
            + TABLE + " (" + COLUMN_ID
            + " integer primary key, " + COLUMN_NAME
            + " text, " + COLUMN_SHORT_TEXT
            + " text, " + COLUMN_FULL_TEXT
            + " text, " + COLUMN_IMAGE
            + " blob);";

    /**
     * Calls {@link SQLiteOpenHelper} super constructor
     * @param context The current context
     */
    public SQLiteHealthTipsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the table health_tips by running the SQL statement stored as {@link SQLiteHealthTipsHelper#DATABASE_CREATE}
     * @param database SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    /**
     * Sample code when upgrading the database, just dropping the table and creating a new one
     * @param db SQLiteDatabase
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHealthTipsHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
