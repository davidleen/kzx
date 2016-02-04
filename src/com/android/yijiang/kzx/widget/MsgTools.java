package com.android.yijiang.kzx.widget;

import com.android.yijiang.kzx.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ResourceAsColor")
public class MsgTools {

	public static void toast(Context context, String msg, int timeTag) {
//		Toast toast = Toast.makeText(context, null, timeTag);
//		toast.getView().setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//		LinearLayout layout = (LinearLayout) toast.getView();
//		layout.setBackgroundResource(R.drawable.app_msg_bg);
//		layout.setOrientation(LinearLayout.HORIZONTAL);
//		layout.setGravity(Gravity.CENTER|Gravity.TOP);
//		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//		TextView tv = new TextView(context,null,android.R.attr.textAppearanceMedium);
//		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//		tv.setPadding(25, 15, 25, 15);
//		tv.setGravity(Gravity.CENTER_VERTICAL);
//		tv.setTextColor(Color.parseColor("#444444"));
//		tv.setText(msg);
//		layout.addView(tv);
//		toast.show();
        LayoutInflater inflater=LayoutInflater.from(context);
        View layout=inflater.inflate(R.layout.toast_dialog,null);
        TextView textView=(TextView)layout.findViewById(R.id.messageTv);
        textView.setText(msg);
        Toast toast=new Toast(context);
        toast.setDuration(timeTag);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
	}
	
	public static void toast_target(Context context, String msg, int timeTag) {
//		Toast toast = Toast.makeText(context, null, timeTag);
//		LinearLayout layout = (LinearLayout) toast.getView();
//		layout.setBackgroundResource(R.drawable.app_msg_bg);
//		layout.setOrientation(LinearLayout.HORIZONTAL);
//		layout.setGravity(Gravity.CENTER|Gravity.TOP);
//		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//		TextView tv = new TextView(context,null,android.R.attr.textAppearanceMedium);
//		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//		tv.setPadding(25, 15, 25, 15);
//		tv.setGravity(Gravity.CENTER_VERTICAL);
//		tv.setTextColor(Color.parseColor("#444444"));
//		tv.setText(msg);
//		layout.addView(tv);
//		toast.show();
	    LayoutInflater inflater=LayoutInflater.from(context);
        View layout=inflater.inflate(R.layout.toast_dialog,null);
        TextView textView=(TextView)layout.findViewById(R.id.messageTv);
        textView.setText(msg);
        Toast toast=new Toast(context);
        toast.setDuration(timeTag);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 0);
        toast.setView(layout);
        toast.show();
	}
	
}