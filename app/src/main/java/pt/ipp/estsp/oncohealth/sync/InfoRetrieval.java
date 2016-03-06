package pt.ipp.estsp.oncohealth.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import pt.ipp.estsp.oncohealth.database.HealthTip;
import pt.ipp.estsp.oncohealth.database.DataSource;
import pt.ipp.estsp.oncohealth.database.Routine;

/**
 * Created by Victor on 23/02/2016.
 */
public class InfoRetrieval extends AsyncTask<Context, Void, Pair<List<HealthTip>,Routine>> {

    @Override
    protected Pair<List<HealthTip>,Routine> doInBackground(Context... params) {
        int numTips = 4;

        DataSource dataSource = new DataSource(params[0]);

        try {
            dataSource.open();
            dataSource.populateDatabase();

            List<HealthTip> tips = dataSource.getNHealthTips(numTips);

            Routine routine = dataSource.getLastRoutine();

            dataSource.close();

            return new Pair<List<HealthTip>,Routine>(tips,routine);

        } catch (IOException e) {
            Log.d("OncoHealth", "Something went wrong - IOException");
        } catch (SQLException e2){
            Log.d("OncoHealth","Something went wrong - SQLException");
        }

        return null;
    }
}
