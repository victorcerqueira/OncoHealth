package pt.ipp.estsp.oncohealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import pt.ipp.estsp.oncohealth.database.HealthTip;
import pt.ipp.estsp.oncohealth.pt.ipp.estsp.oncohealth.sync.InfoRetrieval;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        InfoRetrieval connection = new InfoRetrieval();

        try {
            List<HealthTip> tips = connection.execute(this).get();
            Log.d("OncoHealth", "Everything ok!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
