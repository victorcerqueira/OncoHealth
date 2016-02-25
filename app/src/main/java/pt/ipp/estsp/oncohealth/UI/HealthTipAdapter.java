package pt.ipp.estsp.oncohealth.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pt.ipp.estsp.oncohealth.R;
import pt.ipp.estsp.oncohealth.database.HealthTip;

/**
 * Created by Victor on 25/02/2016.
 */
public class HealthTipAdapter extends BaseAdapter {

    private List<HealthTip> healthTips;
    private Context context;
    private static LayoutInflater inflater=null;

    public HealthTipAdapter(Context c, List<HealthTip> tips){
        healthTips = tips;
        context = c;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}

    @Override
    public int getCount() {
        return healthTips.size();
    }

    @Override
    public Object getItem(int position) {
        return healthTips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = inflater.inflate(R.layout.healthtip_item, null);

        TextView name = (TextView)item.findViewById(R.id.tip_name);
        name.setText(healthTips.get(position).getName());

        return item;
    }
}
