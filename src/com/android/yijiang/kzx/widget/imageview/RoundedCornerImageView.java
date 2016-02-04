package com.android.yijiang.kzx.widget.imageview;

import com.android.yijiang.kzx.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class RoundedCornerImageView extends ImageView {
	private static final float DEFAULT_CORNER_RADIUS = 20;
    protected int realWidth;
    protected int realHeight;
	private float roundPx;
	private Resources resources;
	private BitmapShader shader;
	private Paint paint;

    public RoundedCornerImageView(Context context) {
        super(context);
        resources = context.getResources();
        roundPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_RADIUS, resources.getDisplayMetrics());
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        resources = context.getResources();
        roundPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_RADIUS, resources.getDisplayMetrics());
        int[] textSizeAttr = new int[] { android.R.attr.layout_width, android.R.attr.layout_height, R.attr.corner_radius };
        TypedArray a = context.obtainStyledAttributes(attrs, textSizeAttr);
        realWidth = a.getDimensionPixelSize(0, -1);
        realHeight = a.getDimensionPixelSize(1, -1);
        roundPx = a.getDimension(2, roundPx);
        a.recycle();
        createFramedPhoto(Math.min(getWidth(), getHeight()));
        invalidate();
    }

    public void setLayoutParams(LayoutParams params) {
    	super.setLayoutParams(params);
    	if(params.width > 0) realWidth = params.width;
    	if(params.height > 0) realHeight = params.height;
    }

	public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        createFramedPhoto(Math.min(getWidth(), getHeight()));
        this.invalidate();
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        createFramedPhoto(Math.min(getWidth(), getHeight()));
        this.invalidate();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        createFramedPhoto(Math.min(getWidth(), getHeight()));
        this.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (shader == null) {
            createFramedPhoto(Math.min(getWidth(), getHeight()));
        }
        
        final Rect rect = new Rect(0, 0, realWidth, realHeight);
        final RectF rectF = new RectF(rect);
        drawBefore(canvas, rectF);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        drawMore(canvas, rectF);
    }

    private void createFramedPhoto(int size) {
        int width;
        int height;
        if (realWidth == 0 && realHeight == 0) {
            this.measure(0, 0);

            width = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.UNSPECIFIED);
            height = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), MeasureSpec.UNSPECIFIED);
        } else {
            width = realWidth;
            height = realHeight;
        }

        Bitmap bitmap = null;
        Drawable drawable = this.getDrawable();

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)drawable).getBitmap();
        } else if (drawable instanceof TransitionDrawable) {
            TransitionDrawable transitionDrawable = (TransitionDrawable)drawable;
            bitmap = ((BitmapDrawable)transitionDrawable.getDrawable(transitionDrawable.getNumberOfLayers() - 1)).getBitmap();
        }

        if (bitmap != null) {
            //bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.postScale((float)realWidth/(float)bitmap.getWidth(), (float)realHeight/(float)bitmap.getHeight());
            shader.setLocalMatrix(matrix);
            paint = new Paint();
            paint.setShader(shader);
        }else{
        	paint = new Paint();
            paint.setARGB(0, 245, 90, 70);
        }
    }

    protected void drawBefore(Canvas canvas, RectF bounds) { }

	protected void drawMore(Canvas canvas, RectF bounds) { }
	
	public float getCornerRadius(){
		return roundPx;
	}
	
	public void recycle(){
		
	}
}