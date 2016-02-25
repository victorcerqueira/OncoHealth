package pt.ipp.estsp.oncohealth.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Victor on 25/02/2016.
 */
public class HealthTipImageView extends ImageView {

    public HealthTipImageView(Context context) {
        super(context);
    }

    public HealthTipImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HealthTipImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}
