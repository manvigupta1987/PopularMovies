package com.entertainment.manvi.MyMovies.Utils;

import android.content.Context;
import android.util.AttributeSet;

public class AspectRationImageView extends android.support.v7.widget.AppCompatImageView {

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
