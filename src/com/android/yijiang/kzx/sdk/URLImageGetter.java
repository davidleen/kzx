package com.android.yijiang.kzx.sdk;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.widget.TextView;

public class URLImageGetter implements ImageGetter {
    Context context;
    TextView textView;

    public URLImageGetter(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }
    
    @Override
    public Drawable getDrawable(String paramString) {
        final URLDrawable urlDrawable = new URLDrawable(context);
        
        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(paramString);
        return urlDrawable;
    }
    
    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;
        
        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }
        
        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.drawable = result;
                
                URLImageGetter.this.textView.requestLayout();
            }
        }
        
        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }
        
        public Drawable fetchDrawable(String url) {
            try {
                InputStream is = fetch(url);
                
                Rect bounds = SystemInfoUtils.getDefaultImageBounds(context);
                Bitmap bitmapOrg = BitmapFactory.decodeStream(is);
                if(bitmapOrg!=null){
            	   Bitmap bitmap = Bitmap.createScaledBitmap(bitmapOrg, bounds.right, bounds.bottom, true);
                   BitmapDrawable drawable = new BitmapDrawable(bitmap);
                   drawable.setBounds(new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()));
                  
                   return drawable;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        
        private InputStream fetch(String url) throws ClientProtocolException, IOException {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            
            HttpResponse response = client.execute(request);
            return response.getEntity().getContent();
        }
    }
}