package com.example.manvi.movieappstage1.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by manvi on 18/9/17.
 */

public class AspectRationImageView extends ImageView {

    private static final float ASPECT_RATIO = 1.5f;

    public AspectRationImageView(Context context) {
        super(context);
    }

    public AspectRationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width * ASPECT_RATIO);
        setMeasuredDimension(width, height);
    }
}
