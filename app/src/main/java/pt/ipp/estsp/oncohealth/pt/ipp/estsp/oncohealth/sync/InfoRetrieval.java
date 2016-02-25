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
        int numTips = 4;

        HealthTipDataSource dataSource = new HealthTipDataSource(params[0]);

        try {
            dataSource.open();
            dataSource.populateDatabase();

            List<HealthTip> tips = dataSource.getNHealthTips(numTips);

            dataSource.close();

            if(tips != null)
                return tips;

        } catch (IOException e) {
            Log.d("OncoHealth", "Something went wrong - IOException");
        } catch (SQLException e2){
            Log.d("OncoHealth","Something went wrong - SQLException");
        }

        return null;
    }
}
