package pt.ipp.estsp.oncohealth.UI;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import pt.ipp.estsp.oncohealth.R;
import pt.ipp.estsp.oncohealth.UI.HealthTipAdapter;
import pt.ipp.estsp.oncohealth.UI.ServerUnavailableDialogFragment;
import pt.ipp.estsp.oncohealth.database.HealthTip;
import pt.ipp.estsp.oncohealth.sync.InfoRetrieval;

public class LandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        InfoRetrieval connection = new InfoRetrieval();

        try {
            final List<HealthTip> tips = connection.execute(this).get();
            if(tips == null){
                Log.d("OncoHealth", "Connection to server not available. Please check your internet connection. If problem persists, contact server admin.");
                DialogFragment dialog = new ServerUnavailableDialogFragment();
                dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
            }
            else{
                GridView gv = (GridView) findViewById(R.id.tips_gridview);
                gv.setAdapter(new HealthTipAdapter(this, tips));
                gv.setOnItemClickListener(new GridView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        final Intent intent = new Intent(getApplicationContext(), HealthTipActivity.class);
                        intent.putExtra("healthTip", tips.get(pos));
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
