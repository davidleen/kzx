package com.android.yijiang.kzx.sdk;

/**
 *这是分享文字加图片的类
 */

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class ShareToSNS {

	private String SavePath;
	private String image_name = "/Scinan_Screen.png";

	public String GetandSaveCurrentImage(WindowManager windowManager, View decorview) {
		// 1.构建Bitmap
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		// 2.获取屏幕

		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();

		SavePath = getSDCardPath() + "/AndyDemo/ScreenImage";

		// 3.保存Bitmap
		try {
			File path = new File(SavePath);
			// 文件
			String filepath = SavePath + image_name;
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
		}
		return SavePath;
	}

	private String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	// 分享照片
	public Intent getIntentSharePhotoAndText(String photoUri, String share_text) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		File file = new File(photoUri + image_name);
		shareIntent.putExtra(Intent.EXTRA_TEXT, share_text);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("image/png");
		return shareIntent;
	}
}