package pt.ipp.estsp.oncohealth;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import java.util.List;

import pt.ipp.estsp.oncohealth.UI.HealthTipAdapter;
import pt.ipp.estsp.oncohealth.UI.ServerUnavailableDialogFragment;
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
            if(tips == null){
                Log.d("OncoHealth", "Connection to server not available. Please check your internet connection. If problem persists, contact server admin.");
                DialogFragment dialog = new ServerUnavailableDialogFragment();
                dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
            }
            else{
                GridView gv = (GridView) findViewById(R.id.tips_gridview);
                gv.setAdapter(new HealthTipAdapter(this, tips));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
