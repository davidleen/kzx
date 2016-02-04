package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.ImageUtils;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.android.yijiang.kzx.widget.betterpickers.calendardatepicker.CalendarDatePickerDialog.OnDateSetListener;
import com.android.yijiang.kzx.widget.betterpickers.datepicker.DatePickerBuilder;
import com.android.yijiang.kzx.widget.betterpickers.datepicker.DatePickerDialogFragment.DatePickerDialogHandler;
import com.android.yijiang.kzx.widget.betterpickers.radialtimepicker.RadialPickerLayout;
import com.android.yijiang.kzx.widget.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.android.yijiang.kzx.widget.betterpickers.radialtimepicker.RadialTimePickerDialog.OnTimeSetListener;

/**
 * 完成周期
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxCycleDoneDialogFragment extends DialogFragment {

	private CheckBox oneDayRadio;
	private CheckBox threeDayRadio;
	private CheckBox sevenDayRadio;
	
	private TextView beginDateBtn;// 开始日期
	private TextView beginTimeBtn;// 开始时间

	private TextView endDateBtn;// 结束日期
	private TextView endTimeBtn;// 结束时间
	private ScrollView contentScrollView;
	private ImageButton closeBtn;
	
	private Button btnSure;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static KzxCycleDoneDialogFragment newInstance() {
		KzxCycleDoneDialogFragment fragment = new KzxCycleDoneDialogFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		contentScrollView=(ScrollView)view.findViewById(R.id.contentScrollView);
		btnSure=(Button)view.findViewById(R.id.btnSure);
		oneDayRadio=(CheckBox)view.findViewById(R.id.oneDayRadio);
		threeDayRadio=(CheckBox)view.findViewById(R.id.threeDayRadio);
		sevenDayRadio=(CheckBox)view.findViewById(R.id.sevenDayRadio);
		beginDateBtn = (TextView) view.findViewById(R.id.beginDateBtn);
		beginTimeBtn = (TextView) view.findViewById(R.id.beginTimeBtn);
		endDateBtn = (TextView) view.findViewById(R.id.endDateBtn);
		endTimeBtn = (TextView) view.findViewById(R.id.endTimeBtn);
		closeBtn = (ImageButton) view.findViewById(R.id.closeBtn);
		// 初始化默认时间
		beginDateBtn.setText(DateUtil.getCurrentDay());
		beginTimeBtn.setText(Calendar.HOUR_OF_DAY+":"+Calendar.MINUTE);
		Timestamp timestamp = null;
		try {
			timestamp = DateUtil.getTimestampExpiredDay(DateUtil.string2Date(DateUtil.getCurrentDay()), 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		endDateBtn.setText(format.format(timestamp));
		endTimeBtn.setText(Calendar.HOUR_OF_DAY+":"+Calendar.MINUTE);
		// 开始日期
		beginDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mCalendar = Calendar.getInstance();
				CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(new OnDateSetListener() {
					@Override
					public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
						beginDateBtn.setText(year + "-" + String.format("%02d", monthOfYear+1) + "-" + String.format("%02d", dayOfMonth));
					}
				}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
				calendarDatePickerDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle);
				calendarDatePickerDialog.setShowsDialog(true);
				calendarDatePickerDialog.show(getChildFragmentManager(), "fragment_date_picker_name");
			}
		});
		// 开始时间
		beginTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mCalendar = Calendar.getInstance();
				RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog.newInstance(new OnTimeSetListener() {
					@Override
					public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
						beginTimeBtn.setText(hourOfDay + ":" + minute);
					}
				}, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
				timePickerDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle);
				timePickerDialog.setShowsDialog(true);
				timePickerDialog.show(getChildFragmentManager(), "fragment_time_picker_name");
			}
		});
		// 结束日期
		endDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mCalendar = Calendar.getInstance();
				CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(new OnDateSetListener() {
					@Override
					public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
						//结束时间要大于当前时间和开始时间
						String endDate=year + "-" + String.format("%02d", monthOfYear+1) + "-" + String.format("%02d", dayOfMonth);
						if(DateUtil.getDaysBetween(DateUtil.getCurrentDay(), endDate)>0&&DateUtil.getDaysBetween(beginDateBtn.getText().toString(), endDate)>0){
							endDateBtn.setText(endDate);
						}else{
							MsgTools.toast(getActivity(), getString(R.string.cycle_end_hint), ResourceMap.LENGTH_SHORT);
						}
					}
				}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
				calendarDatePickerDialog.setShowsDialog(true);
				calendarDatePickerDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle);
				calendarDatePickerDialog.show(getChildFragmentManager(), "fragment_date_picker_name");
			}
		});
		// 结束时间
		endTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar mCalendar = Calendar.getInstance();
				RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog.newInstance(new OnTimeSetListener() {
					@Override
					public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
						endTimeBtn.setText(hourOfDay + ":" + minute);
					}
				}, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
				timePickerDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle);
				timePickerDialog.setShowsDialog(true);
				timePickerDialog.show(getChildFragmentManager(), "fragment_time_picker_name");
			}
		});
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		oneDayRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					String beginDate=beginDateBtn.getText().toString();
					Timestamp timestamp = DateUtil.getTimestampExpiredDay(DateUtil.string2Date(beginDate), 1);
					endDateBtn.setText(format.format(timestamp));
					threeDayRadio.setChecked(false);
					sevenDayRadio.setChecked(false);
				}
			}
		});
		threeDayRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					String beginDate=beginDateBtn.getText().toString();
					Timestamp timestamp = DateUtil.getTimestampExpiredDay(DateUtil.string2Date(beginDate), 3);
					endDateBtn.setText(format.format(timestamp));
					oneDayRadio.setChecked(false);
					sevenDayRadio.setChecked(false);
				}
			}
		});
		sevenDayRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					String beginDate=beginDateBtn.getText().toString();
					Timestamp timestamp = DateUtil.getTimestampExpiredDay(DateUtil.string2Date(beginDate), 7);
					endDateBtn.setText(format.format(timestamp));
					oneDayRadio.setChecked(false);
					threeDayRadio.setChecked(false);
				}
			}
		});
		// 确定选中
		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity().getPackageName()+".ADD_TASK_RECEIVED_ACTION");
				intent.putExtra("action", "cycle_done");
				intent.putExtra("beginDate", beginDateBtn.getText().toString());
				intent.putExtra("beginTime", beginTimeBtn.getText().toString());
				intent.putExtra("endDate", endDateBtn.getText().toString());
				intent.putExtra("endTime", endTimeBtn.getText().toString());
				intent.putExtra("betweenTime", DateUtil.getDaysBetween(beginDateBtn.getText().toString(), endDateBtn.getText().toString()));
				getActivity().sendBroadcast(intent);
				dismiss();
			}
		});
//		Bitmap mBitmap=ImageUtils.drawableToBitmap(getResources().getDrawable(R.drawable.newguide_bg));
//		_stackBlurManager = new StackBlurManager(mBitmap);
//		Drawable mDrawable=ImageUtils.bitmapToDrawable(_stackBlurManager.process(Constants.Blursize));
//		mDrawable.setAlpha(10);
//		contentScrollView.setBackground(mDrawable);
//		if(mBitmap!=null&&!mBitmap.isRecycled()){
//			mBitmap.recycle();
//		}
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.kzx_cycle_done_dialog_fragment, container, false);
		return view;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
