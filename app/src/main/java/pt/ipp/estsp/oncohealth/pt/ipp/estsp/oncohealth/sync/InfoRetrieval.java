package pt.ipp.estsp.oncohealth.pt.ipp.estsp.oncohealth.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import pt.ipp.estsp.oncohealth.database.HealthTip;
import pt.ipp.estsp.oncohealth.database.HealthTipDataSource;

/**
 * Created by Victor on 23/02/2016.
 */
public class InfoRetrieval extends AsyncTask<Context, Void, List<HealthTip>> {

    @Override
    protected List<HealthTip> doInBackground(Context... params) {
        HealthTipDataSource dataSource = new HealthTipDataSource(params[0]);

        try {
            dataSource.open();
            dataSource.populateDatabase();
            dataSource.close();
        } catch (IOException e) {
            Log.d("OncoHealth", "Something went wrong - IOException");
        } catch (SQLException e2){
            Log.d("OncoHealth","Something went wrong - SQLException");
        }
        List<HealthTip> tips = dataSource.getAllHealthTips();
        Log.d("OncoHealth" , "Retrieved " + tips.size() + "tips.");
        return tips;
    }
}