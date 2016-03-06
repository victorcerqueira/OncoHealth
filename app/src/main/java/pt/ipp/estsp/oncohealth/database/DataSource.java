package pt.ipp.estsp.oncohealth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.ipp.estsp.oncohealth.sync.ServerConnection;

/**
 * Class that works like a data access object to the database.
 * After instantiated, should call {@link DataSource#open()} before any other calls.
 * Should call {@link DataSource#close()} once not needed anymore.
 * Also works like an interface to the ServerConnection, handling any server communication errors.
 * @author Victor
 * @version 0.2
 * @see SQLiteHelper
 * @see ServerConnection
 * @since 0.1
 */
public class DataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    /** List of all HealthTips columns name */
    private static String[] allColumnsHT = {SQLiteHelper.HEALTH_TIP_COLUMN_ID,
            SQLiteHelper.HEALTH_TIP_COLUMN_NAME, SQLiteHelper.HEALTH_TIP_COLUMN_SHORT_TEXT,
            SQLiteHelper.HEALTH_TIP_COLUMN_FULL_TEXT, SQLiteHelper.HEALTH_TIP_COLUMN_IMAGE};
    /** List of all Routines columns name */
    private static String[] allColumnsRoutine = {SQLiteHelper.ROUTINE_COLUMN_ID,
            SQLiteHelper.ROUTINE_COLUMN_INTERVAL,SQLiteHelper.ROUTINE_COLUMN_REPETITIONS,
            SQLiteHelper.ROUTINE_COLUMN_IMAGE,SQLiteHelper.ROUTINE_COLUMN_DESCRIPTION,
            SQLiteHelper.ROUTINE_COLUMN_IS_DONE};

    /**
     * Constructor
     */
    public DataSource(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Opens the connection to the database. If the call fails will throw
     * a SQLException. Once called should be followed by corresponding
     * {@link DataSource#close()} call.
     * @throws SQLException
     */
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the connection to the database. Should be called after a
     * successful {@link DataSource#open()} call.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Try to connect to the server and retrieve the JSON file. If connection
     * fails throws an IOException.
     * When retrieving the JSON, tries to add corresponding health tip to the database.
     * If the insertion fails just continue to the new one. The health tips should be
     * retrieved after this call using {@link DataSource#getAllHealthTips()}.
     * @throws IOException
     */
    public void populateDatabase() throws IOException {
        ServerConnection server = new ServerConnection();
        JSONObject json = server.getJSON();
        if(json==null){
            server.disconnect();
            return;
        }
        try {
            JSONArray jsonHealthTips = json.getJSONArray("HealthTips");
            for(int i=0; i<jsonHealthTips.length(); i++){
                addHealthTip(jsonHealthTips.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonRoutines = json.getJSONArray("Routines");
            for(int i=0; i<jsonRoutines.length(); i++){
                addRoutine(jsonRoutines.getJSONObject(i));
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
     * returns the corresponding Routine object.
     * Should only be called after a successful {@link DataSource#open()} call.
     * @param rt The JSONObject corresponding to the Routine to parse
     * @return Routine if successful, {@code null} otherwise
     */
    private Routine addRoutine(JSONObject rt) {
        Routine routine = new Routine();
        try {
            long id = rt.getLong("id");

            if(getRoutine(id)!=null) { //if id already in database delete the row
                String[] args = {id+""};
                database.delete(SQLiteHelper.ROUTINE_TABLE, SQLiteHelper.ROUTINE_COLUMN_ID + " = ?", args);
            }

            routine.setId(id);
            routine.setRepetitions(rt.getInt("repetitions"));
            routine.setInterval(rt.getInt("interval"));
            routine.setDescription(rt.getString("description"));
            routine.setIsDone(rt.getBoolean("isDone"));
            //TODO handle image

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.ROUTINE_COLUMN_ID,routine.getId());
            values.put(SQLiteHelper.ROUTINE_COLUMN_REPETITIONS, routine.getRepetitions());
            values.put(SQLiteHelper.ROUTINE_COLUMN_INTERVAL,routine.getInterval());
            values.put(SQLiteHelper.ROUTINE_COLUMN_DESCRIPTION,routine.getDescription());
            values.put(SQLiteHelper.ROUTINE_COLUMN_IS_DONE,routine.isDone()?1:0);
            //TODO handle image

            if(database.insert(SQLiteHelper.ROUTINE_TABLE, null, values)<0)
                return null;

            return routine;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Receive a JSON object, parse it and add it to the database.
     * If the id is already on the database, replace the row with new values.
     * If parsing or insertion fails, return {@code null}, if it succeeds
     * returns the corresponding HealthTip object.
     * Should only be called after a successful {@link DataSource#open()} call.
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
     * Should only be called after a successful {@link DataSource#open()} call.
     * @param id ID of the HealthTip to retrieve
     * @return HealthTip if successful, {@code null} if doesn't exist;
     */
    public HealthTip getHealthTip(long id){
        Cursor cursor = database.query(SQLiteHelper.HEALTH_TIP_TABLE, allColumnsHT,
                SQLiteHelper.HEALTH_TIP_COLUMN_ID +"="+id,null,null,null,null);

        if(!cursor.moveToFirst()) return null;
        return cursorToHealthTip(cursor);
    }

    /**
     * Retrieves the Routine with the given id from the database and returns it.
     * Should only be called after a successful {@link DataSource#open()} call.
     * @param id ID of the Routinep to retrieve
     * @return Routine if successful, {@code null} if doesn't exist;
     */
    public Routine getRoutine(long id){
        Cursor cursor = database.query(SQLiteHelper.ROUTINE_TABLE, allColumnsRoutine,
                SQLiteHelper.ROUTINE_COLUMN_ID +"="+id,null,null,null,null);

        if(!cursor.moveToFirst()) return null;
        Routine r = cursorToRoutine(cursor);
        return r;
    }

    /**
     * Retrieves a {@link List} of {@link HealthTip}s with all the healthTips on the database.
     * Should only be called after a successful {@link DataSource#open()} call.
     * @return List of HealthTips
     */
    public List<HealthTip> getAllHealthTips(){
        List<HealthTip> healthTips = new ArrayList<>();
        Cursor cursor = database.query(SQLiteHelper.HEALTH_TIP_TABLE, allColumnsHT,
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
     * Should only be called after a successful {@link DataSource#open()} call.
     * @param n number of health tips to retrieve
     * @return List of HealthTips with n elements or {@code null} if there are less
     * than n tips available
     */
    public List<HealthTip> getNHealthTips(int n){
        List<HealthTip> healthTips = new ArrayList<>();
        Cursor cursor = database.query(SQLiteHelper.HEALTH_TIP_TABLE, allColumnsHT,
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
     * Retrieves the most recent {@link Routine} from the database (assuming id as ordering factor).
     * Should only be called after a successful {@link DataSource#open()} call.
     * @return Routine if successfull or {@code null} if there isn't any routine available
     */
    public Routine getLastRoutine() {
        Cursor cursor = database.query(SQLiteHelper.ROUTINE_TABLE, allColumnsRoutine,
                null, null, null, null, SQLiteHelper.ROUTINE_COLUMN_ID + " DESC");

        if(!cursor.moveToFirst()) return null;
        Routine routine = cursorToRoutine(cursor);
        cursor.close();

        return routine;
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

    /**
     * Receive a {@link Cursor} and returns a {@link Routine} with the
     * corresponding information.
     * @param cursor Cursor to the database entry of the Routine
     * @return Routine if successful, {@code null} otherwise.
     */
    @Nullable
    private static Routine cursorToRoutine(Cursor cursor) {
        Routine routine = new Routine();
        if(cursor.getCount() < 1 || cursor.getColumnCount() < 5) return null;
        routine.setId(cursor.getLong(0));
        routine.setInterval(cursor.getInt(1));
        routine.setRepetitions(cursor.getInt(2));
        //TODO handle image
        routine.setDescription(cursor.getString(4));
        routine.setIsDone(cursor.getInt(5)==1?true:false);

        return routine;
    }


}
