package com.android.yijiang.kzx.widget.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;

public class RaisedRoundedCornerImageView extends RoundedCornerImageView {
	private int[] COLORS = new int[]{
		0xffffffff,
		0xff000000,
		0xffffffff
	};
	public RaisedRoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RaisedRoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
        
    
    
    private void init() {
    	
		//drawable.setSize(realWidth, realHeight);
	}

	protected void drawMore(Canvas canvas, RectF bounds){
		init();
		Rect rect = new Rect(0, 0, realWidth, realHeight);
        RectF rectF = new RectF(rect);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(getGradientShader());
        paint.setStrokeWidth(2);
        canvas.drawRoundRect(rectF, this.getCornerRadius(), this.getCornerRadius(), paint);
        
        
        rect = new Rect(0, 0, realWidth, realHeight);
        rectF = new RectF(rect);
        paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(0x000000);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        
        canvas.drawRoundRect(rectF, this.getCornerRadius(), this.getCornerRadius(), paint);
		//drawable.draw(canvas);
    }

	private Shader getShineShader() {
		ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {

            private int color1 = Color.parseColor("#44ffffff");
            private int color2 = Color.parseColor("#01ffffff");

            @Override
            public Shader resize(int width, int height) {
                int[] colors = new int[] { color1, color2};
                float[] positions = new float[] { 0, 1 };

                return new LinearGradient(0, 0, 0, height, colors, positions, Shader.TileMode.REPEAT);
            }
        };
		return sf.resize(realWidth - 4, realHeight/2 - 4);
	}

	private Shader getGradientShader() {
		ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {

            private int color1 = Color.parseColor("#55ffffff");
            private int color2 = Color.parseColor("#22ffffff");
            private int color3 = Color.parseColor("#00ffffff");
            private int color4 = Color.parseColor("#22ffffff");
            private int color5 = Color.parseColor("#33ffffff");

            @Override
            public Shader resize(int width, int height) {
                int[] colors = new int[] { color1, color2, color3, color4, color5 };
                float[] positions = new float[] { 0, 0.05f, 0.5f, 0.95f, 1 };

                return new LinearGradient(0, 0, 0, height, colors, positions, Shader.TileMode.REPEAT);
            }
        };
		return sf.resize(realWidth, realHeight);
	}
}