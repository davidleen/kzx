package com.android.yijiang.kzx.sdk;

import com.android.yijiang.kzx.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;
    
    public URLDrawable(Context context) {
        this.setBounds(SystemInfoUtils.getDefaultImageBounds(context));
        
        drawable = context.getResources().getDrawable(R.drawable.default_bg);
        drawable.setBounds(SystemInfoUtils.getDefaultImageBounds(context));
    }
    
    @Override
    public void draw(Canvas canvas) {
        Log.d("test", "this=" + this.getBounds());
        if (drawable != null) {
            Log.d("test", "draw=" + drawable.getBounds());
            drawable.draw(canvas);
        }
    }
    
}
