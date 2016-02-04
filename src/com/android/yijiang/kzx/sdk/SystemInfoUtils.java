package com.android.yijiang.kzx.sdk;

import android.content.Context;
import android.graphics.Rect;
import android.view.Display;
import android.app.Activity;
public class SystemInfoUtils {
	public static Rect getDefaultImageBounds(Context context) {
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = (int) (width * 9 / 16);

		Rect bounds = new Rect(0, 0, width, height);
		return bounds;
	}
}
