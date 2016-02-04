package com.android.yijiang.kzx.sdk;

import java.lang.Thread.UncaughtExceptionHandler;





import com.android.yijiang.kzx.ui.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

//全局异常捕获
public class CustomException implements UncaughtExceptionHandler {
	// 获取application 对象；
	private Context mContext;

	private Thread.UncaughtExceptionHandler defaultExceptionHandler;
	// 单例声明CustomException;
	private static CustomException customException;

	private CustomException() {
	}

	public static CustomException getInstance() {
		if (customException == null) {
			customException = new CustomException();
		}
		return customException;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		// TODO Auto-generated method stub
		if (defaultExceptionHandler != null) {

			Log.e("tag", "exception >>>>>>>" + exception.getLocalizedMessage());
			// 将异常抛出，则应用会弹出异常对话框.这里先注释掉
			// defaultExceptionHandler.uncaughtException(thread, exception);
			Intent bootActivityIntent = new Intent(mContext, MainActivity.class);
			bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(bootActivityIntent);
		}
	}

	public void init(Context context) {
		mContext = context;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
}
