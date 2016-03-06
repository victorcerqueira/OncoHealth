package pt.ipp.estsp.oncohealth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.ipp.estsp.oncohealth.sync.ServerConnection;

/**
 * Class that works like a data access object to the HealthTips database.
 * After instantiated, should call {@link RoutineDataSource#open()} before any other calls.
 * Should call {@link RoutineDataSource#close()} once not needed anymore.
 * Also works like an interface to the ServerConnection, handling any server communication errors.
 * @author Victor
 * @version 0.1
 * @see SQLiteHelper
 * @see ServerConnection
 * @since 0.1
 */
public class RoutineDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    /** List of all columns name */
    private static String[] allColumns = {SQLiteHelper.ROUTINE_COLUMN_ID,
            SQLiteHelper.ROUTINE_COLUMN_INTERVAL,SQLiteHelper.ROUTINE_COLUMN_REPETITIONS,
            SQLiteHelper.ROUTINE_COLUMN_IMAGE,SQLiteHelper.ROUTINE_COLUMN_DESCRIPTION,
            SQLiteHelper.ROUTINE_COLUMN_IS_DONE};

    /**
     * Constructor
     */
    public RoutineDataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Opens the connection to the database. If the call fails will throw
     * a SQLException. Once called should be followed by corresponding
     * {@link RoutineDataSource#close()} call.
     * @throws SQLException
     */
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the connection to the database. Should be called after a
     * successful {@link RoutineDataSource#open()} call.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Try to connect to the server and retrieve the JSON file. If connection
     * fails throws an IOException.
     * When retrieving the JSON, tries to add corresponding health tip to the database.
     * If the insertion fails just continue to the new one. The health tips should be
     * retrieved after this call using {@link RoutineDataSource#getAllHealthTips()}.
     * @throws IOException
     */
    public void populateDatabase() throws IOException {
        ServerConnection server = new ServerConnection();
        JSONObject json = server.getJSON();
        try {
            JSONArray jsonHealthTips = json.getJSONArray("HealthTips");
            for(int i=0; i<jsonHealthTips.length(); i++){
                addHealthTip(jsonHealthTips.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        server.disconnect();
    }

    /**
     * Receive a JSON object, parse it and add it to the database.
     * If the id is already on the database, replace the row with new values.
     * If parsing or insertion fails, return {@code null}, if it succeeds
     * returns the corresponding HealthTip object.
     * Should only be called after a successful {@link RoutineDataSource#open()} call.
     * @param ht The JSONObject corresponding to the HealthTip to parse
     * @return HealthTip if successful, {@code null} otherwise
     */
    @Nullable
    private HealthTip addHealthTip(JSONObject ht){
        HealthTip healthTip = new HealthTip();
        try {
            long id = ht.getLong("id");

            if(getHealthTip(id)!=null) { //if id already in database delete the row
                String[] args = {id+""};
                database.delete(SQLiteHelper.HEALTH_TIP_TABLE, SQLiteHelper.HEALTH_TIP_COLUMN_ID + " = ?", args);
            }

            healthTip.setId(id);
            healthTip.setName(ht.getString("name"));
            healthTip.setShortText(ht.getString("shortText"));
            healthTip.setFullText(ht.getString("fullText"));
            //TODO handle image

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.HEALTH_TIP_COLUMN_ID,healthTip.getId());
            values.put(SQLiteHelper.HEALTH_TIP_COLUMN_NAME,healthTip.getName());
            values.put(SQLiteHelper.HEALTH_TIP_COLUMN_SHORT_TEXT,healthTip.getShortText());
            values.put(SQLiteHelper.HEALTH_TIP_COLUMN_FULL_TEXT,healthTip.getFullText());
            //TODO handle image

            if(database.insert(SQLiteHelper.HEALTH_TIP_TABLE, null, values)<0)
                return null;

            return healthTip;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the Health Tip with the given id from the database and returns it.
     * Should only be called after a successful {@link RoutineDataSource#open()} call.
     * @param id ID of the HealthTip to retrieve
     * @return HealthTip if successful, {@code null} if doesn't exist;
     */
    public HealthTip getHealthTip(long id){
        Cursor cursor = database.query(SQLiteHelper.HEALTH_TIP_TABLE,allColumns,
                SQLiteHelper.HEALTH_TIP_COLUMN_ID +"="+id,null,null,null,null);

        if(!cursor.moveToFirst()) return null;
        return cursorToHealthTip(cursor);
    }

    /**
     * Retrieves a {@link List} of {@link HealthTip}s with all the healthTips on the database.
     * Should only be called after a successful {@link RoutineDataSource#open()} call.
     * @return List of HealthTips
     */
    public List<HealthTip> getAllHealthTips(){
        List<HealthTip> healthTips = new ArrayList<>();
        Cursor cursor = database.query(SQLiteHelper.HEALTH_TIP_TABLE,allColumns,
                null,null,null, SQLiteHelper.HEALTH_TIP_COLUMN_ID + " DESC",null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HealthTip ht = cursorToHealthTip(cursor);
            if(ht != null)
                healthTips.add(ht);
            cursor.moveToNext();
        }
        cursor.close();

        return healthTips;
    }

    /**
     * Retrieves a {@link List} of {@link HealthTip}s with the n most recent
     * healthTips on the database (assuming id as ordering factor).
     * Should only be called after a successful {@link RoutineDataSource#open()} call.
     * @param n number of health tips to retrieve
     * @return List of HealthTips with n elements or {@code null} if there are less
     * than n tips available
     */
    public List<HealthTip> getNHealthTips(int n){
        List<HealthTip> healthTips = new ArrayList<>();
        Cursor cursor = database.query(SQLiteHelper.HEALTH_TIP_TABLE,allColumns,
                null,null,null,null, SQLiteHelper.HEALTH_TIP_COLUMN_ID + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast() && n>0) {
            HealthTip ht = cursorToHealthTip(cursor);
            if(ht != null)
                healthTips.add(ht);
            cursor.moveToNext();
            n--;
        }
        cursor.close();

        if(n>0) return null;

        return healthTips;
    }

    /**
     * Receive a {@link Cursor} and returns a {@link HealthTip} with the
     * corresponding information.
     * @param cursor Cursor to the database entry of the HealthTip
     * @return HealthTip if successful, {@code null} otherwise.
     */
    @Nullable
    private static HealthTip cursorToHealthTip(Cursor cursor) {
        HealthTip ht = new HealthTip();
        if(cursor.getColumnCount() < 4) return null;
        ht.setId(cursor.getLong(0));
        ht.setName(cursor.getString(1));
        ht.setShortText(cursor.getString(2));
        ht.setFullText(cursor.getString(3));
        //TODO handle image

        return ht;
    }
}
