package pt.ipp.estsp.oncohealth.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pt.ipp.estsp.oncohealth.R;
import pt.ipp.estsp.oncohealth.database.HealthTip;

/**
 * Created by Victor on 26/02/2016.
 */
public class HealthTipActivity extends Activity {
    private boolean viewAll = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tip);

        Intent i = getIntent();
        HealthTip healthTip = (HealthTip) i.getSerializableExtra("healthTip");

        if(healthTip==null){
            //TODO deal with this error
            //something is very, very wrong.
        }
        else {
            TextView name = (TextView) findViewById(R.id.health_tip_name);
            TextView shortDescription = (TextView) findViewById(R.id.health_tip_short_description);
            final TextView longDescription = (TextView) findViewById(R.id.health_tip_long_description);
            TextView more = (TextView) findViewById(R.id.health_tip_expand);

            name.setText(healthTip.getName());
            shortDescription.setText(healthTip.getShortText());
            longDescription.setText(healthTip.getFullText());

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpandCollapseAnimation.setHeightForWrapContent(HealthTipActivity.this,longDescription);
                    ExpandCollapseAnimation anim = null;
                    if(viewAll)
                        anim = new ExpandCollapseAnimation(longDescription,1000,1);
                    else
                        anim = new ExpandCollapseAnimation(longDescription,1000,0);

                    if(viewAll) ((TextView)v).setText("Ver mais...");
                    else ((TextView)v).setText("Ver menos...");
                    viewAll = !viewAll;
                    longDescription.startAnimation(anim);
                }
            });

        }

    }

    public void finish(View view) {
        this.finish();
    }
}
