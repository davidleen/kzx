package com.android.yijiang.kzx.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.bean.TaskProcessBean;
import com.android.yijiang.kzx.fragment.KzxTaskProcessFragment.MessageReceiver;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.FileSizeUtil;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.MySeekBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;

/**
 * 快执行反馈弹出框
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxFaceBackDialogFragment extends DialogFragment {

	private String TAG=KzxFaceBackDialogFragment.class.getName();
	private InputMethodManager mInputMethodManager;
	
	private AsyncHttpClient asyncHttpClient;
	private TaskBean taskBean;
	private ImageButton btn_close;
	
	private TextView sendBtn;// 发送
	private TextView titleTv;
	private EditText contentTv;// 文本内容
	private LinearLayout executeRadio;// 执行人菜单
	private LinearLayout sponsorRadio;// 发起人菜单
	private LinearLayout acceptanceRadio;// 验收菜单
	private LinearLayout commentRadio;// 评价菜单
	private CheckBox zuaiBtn;// 遇到阻碍
	private CheckBox yanqiBtn;// 需要延期
	private CheckBox orderBtn;// 顺序进行
	private CheckBox fireBtn;// 抓紧处理
	private CheckBox progressBtn;// 进度如何
	private CheckBox acceptanceBtn;// 验收通过
	private CheckBox reworkBtn;// 需要返工
	private CheckBox goodBtn;// 好评
	private CheckBox badBtn;// 差评
	private TextView callBtn;// 拨打号码
	private TextView seekBarTv;
	private TextView hintTv;
	private TextView seekBarStateTv;//进度状态
	private SeekBar seekBar;// 反馈进度条
	private RelativeLayout seekBarLayout;
	private static OnRefreshSchedule onRefreshSchedule;
	
	private static final String TASK_STATE_0="0";
	private static final String TASK_STATE_1="1";
	private static final String TASK_STATE_2="2";
	private static final String TASK_STATE_3="3";
	private static final String TASK_STATE_4="4";
	private static final String TASK_STATE_5="5";
	
	private Drawable zuaiDrawable;
	private Drawable yanqiDrawable;
	private Drawable shunxuDrawable;
	private Drawable zhuajinDrawable;
	private Drawable jinduDrawable;
	private Drawable yanshouDrawable;
	private Drawable fangongDrawable;
	private Drawable haopingDrawable;
	private Drawable chapingDrawable;
	
	private String typeStr;
	private Dialog dialog;
	
	private String memberId;
	private JSONArray clientIds;
	
	public static KzxFaceBackDialogFragment newInstance(TaskBean taskBean,String typeStr,OnRefreshSchedule refreshSchedule) {
		KzxFaceBackDialogFragment fragment = new KzxFaceBackDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("taskBean", taskBean);
		bundle.putString("typeStr", typeStr);
		fragment.setArguments(bundle);
		onRefreshSchedule=refreshSchedule;
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskBean = (TaskBean) getArguments().getSerializable("taskBean");
		typeStr=getArguments().getString("typeStr");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		zuaiDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_zuai);
		yanqiDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_more);
		shunxuDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_ok);
		zhuajinDrawable= getResources().getDrawable(R.drawable.kzx_ic_task_process_fire);
		jinduDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_more);
		yanshouDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_arrow_action);
		fangongDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_arrow_back_action);
		haopingDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_good_comment_action);
		chapingDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_bad_comment_action);

		
		memberId=getContext().getKzxTokenBean().getMemberId();
		clientIds=JSON.parseArray(getContext().getKzxTokenBean().getClientIds());
		
		mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		titleTv=(TextView) view.findViewById(R.id.titleTv);
		btn_close=(ImageButton)view.findViewById(R.id.btn_close);
		sendBtn = (TextView) view.findViewById(R.id.sendBtn);// 发送
		contentTv = (EditText) view.findViewById(R.id.contentCt);// 文本内容
		executeRadio = (LinearLayout) view.findViewById(R.id.executeRadio);
		sponsorRadio = (LinearLayout) view.findViewById(R.id.sponsorRadio);
		acceptanceRadio = (LinearLayout) view.findViewById(R.id.acceptanceRadio);
		commentRadio = (LinearLayout) view.findViewById(R.id.commentRadio);
		zuaiBtn = (CheckBox) view.findViewById(R.id.zuaiBtn);// 遇到阻碍
		yanqiBtn = (CheckBox) view.findViewById(R.id.yanqiBtn);// 需要延期
		orderBtn = (CheckBox) view.findViewById(R.id.orderBtn);// 顺序进行
		fireBtn = (CheckBox) view.findViewById(R.id.fireBtn);// 抓紧处理
		acceptanceBtn= (CheckBox) view.findViewById(R.id.acceptanceBtn);// 验收通过
		reworkBtn= (CheckBox) view.findViewById(R.id.reworkBtn);// 需要返工
		goodBtn= (CheckBox) view.findViewById(R.id.goodBtn);// 好评
		badBtn= (CheckBox) view.findViewById(R.id.badBtn);// 差评
		callBtn=(TextView)view.findViewById(R.id.callBtn);//拨打号码
		progressBtn = (CheckBox) view.findViewById(R.id.progressBtn);// 进度如何
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);// 反馈进度条
		seekBarStateTv = (TextView) view.findViewById(R.id.seekBarStateTv);// 反馈进度条
		seekBarLayout = (RelativeLayout) view.findViewById(R.id.seekBarLayout);
		seekBarTv = (TextView) view.findViewById(R.id.seekBarTv);
		hintTv= (TextView) view.findViewById(R.id.hintTv);
		// 遇到阻碍
		zuaiBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					yanqiBtn.setChecked(false);
					orderBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					zuaiDrawable.setBounds(0, 0, zuaiDrawable.getMinimumWidth(), zuaiDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(zuaiDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
			}
		});
		// 需要延期
		yanqiBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					zuaiBtn.setChecked(false);
					orderBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					yanqiDrawable.setBounds(0, 0, yanqiDrawable.getMinimumWidth(), yanqiDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(yanqiDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
			}
		});
		// 顺序进行
		orderBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					zuaiBtn.setChecked(false);
					yanqiBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					shunxuDrawable.setBounds(0, 0, shunxuDrawable.getMinimumWidth(), shunxuDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(shunxuDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
			}
		});
		// 抓紧处理
		fireBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					progressBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					zhuajinDrawable.setBounds(0, 0, zhuajinDrawable.getMinimumWidth(), zhuajinDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(zhuajinDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
			}
		});
		// 进度如何
		progressBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					fireBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					jinduDrawable.setBounds(0, 0, jinduDrawable.getMinimumWidth(), jinduDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(jinduDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
			}
		});
		// 验收通过
		acceptanceBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					reworkBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					yanshouDrawable.setBounds(0, 0, yanshouDrawable.getMinimumWidth(),yanshouDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(yanshouDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
				
			}
		});
		// 需要返工
		reworkBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					acceptanceBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					fangongDrawable.setBounds(0, 0, fangongDrawable.getMinimumWidth(), fangongDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(fangongDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
				
			}
		});
		// 好评
		goodBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					badBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					haopingDrawable.setBounds(0, 0, haopingDrawable.getMinimumWidth(), haopingDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(haopingDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
				
			}
		});
		// 差评
		badBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					goodBtn.setChecked(false);
					sendBtn.setEnabled(true);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					chapingDrawable.setBounds(0, 0, chapingDrawable.getMinimumWidth(), chapingDrawable.getMinimumHeight());
					contentTv.setCompoundDrawables(chapingDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentTv.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
				
			}
		});
		// 初始化滑动模块最小值
		seekBarTv.setText(taskBean.getSchedule() + "%");
		hintTv.setText(taskBean.getSchedule() + "%");
		final String seek_bar_state_1=getString(R.string.seek_bar_state_1);
		final String seek_bar_state_2=getString(R.string.seek_bar_state_2);
		final String seek_bar_state_3=getString(R.string.seek_bar_state_3);
		final String seek_bar_state_4=getString(R.string.seek_bar_state_4);
		final String seek_bar_state_5=getString(R.string.seek_bar_state_5);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int leftmargin=0;
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(progress==0){
					seekBarStateTv.setText(seek_bar_state_1);
				}else if(1<progress&&progress<40){
					seekBarStateTv.setText(seek_bar_state_2);
				}else if(41<progress&&progress<70){
					seekBarStateTv.setText(seek_bar_state_3);
				}else if(71<progress&&progress<99){
					seekBarStateTv.setText(seek_bar_state_4);
				}else if(progress==100){
					seekBarStateTv.setText(seek_bar_state_5);
				}
				if(contentTv.getCompoundDrawables()[0]==null&&StringUtils.isEmpty(contentTv.getText().toString())){
					sendBtn.setEnabled(progress!=Integer.valueOf(taskBean.getSchedule()));
				}
//				RelativeLayout.LayoutParams paramsStrength = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.WRAP_CONTENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//	            leftmargin = ((MySeekBar) seekBar).getSeekBarThumb().getBounds()
//	                    .centerX()+63;
//	            if (leftmargin < 0)
//	                leftmargin = 0;
//	            paramsStrength.leftMargin = leftmargin;
//	            seekBarTv.setLayoutParams(paramsStrength);
//	            seekBarTv.setText(progress + "%");
				hintTv.setText(progress + "%");
			}
		});
		seekBar.post(new Runnable() {
			@Override
			public void run() {
				if(!StringUtils.isEmpty(taskBean.getSchedule())){
					seekBar.setProgress(Integer.valueOf(taskBean.getSchedule()));
				}
			}
		});
		// 发送反馈
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendFaceBack();
			}
		});
		contentTv.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(contentTv.getCompoundDrawables()[0]!=null){
					sendBtn.setEnabled(true);
					return;
				}
				if(s.length()>0){
					sendBtn.setEnabled(true);
				}else{
					sendBtn.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		contentTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEND) {
					sendFaceBack();
				}
				return true;
			}
		});
		// 判断任务是执行人还是发起人
		executeRadio.setVisibility(View.GONE);
		sponsorRadio.setVisibility(View.GONE);
		acceptanceRadio.setVisibility(View.GONE);
		commentRadio.setVisibility(View.GONE);
		seekBarLayout.setVisibility(View.GONE);
		if(TASK_STATE_1.equals(taskBean.getState())){
			if(memberId.equals(taskBean.getExecutor())){
				executeRadio.setVisibility(View.VISIBLE);
				sponsorRadio.setVisibility(View.GONE);
				commentRadio.setVisibility(View.GONE);
				acceptanceRadio.setVisibility(View.GONE);
				seekBarLayout.setVisibility(View.VISIBLE);
			}else if(memberId.equals(taskBean.getSponsor())){
				sponsorRadio.setVisibility(View.VISIBLE);
				executeRadio.setVisibility(View.GONE);
				seekBarLayout.setVisibility(View.GONE);
				commentRadio.setVisibility(View.GONE);
				acceptanceRadio.setVisibility(View.GONE);
			}
		}else if(TASK_STATE_2.equals(taskBean.getState())&&memberId.equals(taskBean.getSponsor())){
			executeRadio.setVisibility(View.GONE);
			sponsorRadio.setVisibility(View.GONE);
			commentRadio.setVisibility(View.GONE);
			acceptanceRadio.setVisibility(View.VISIBLE);
			seekBarLayout.setVisibility(View.GONE);
			titleTv.setText(getString(R.string.yanshou_title));
		}else if(TASK_STATE_3.equals(taskBean.getState())&&memberId.equals(taskBean.getSponsor())){
			executeRadio.setVisibility(View.GONE);
			sponsorRadio.setVisibility(View.GONE);
			acceptanceRadio.setVisibility(View.GONE);
			commentRadio.setVisibility(View.VISIBLE);
			seekBarLayout.setVisibility(View.GONE);
			titleTv.setText(getString(R.string.pingjia_title));
		}else if((TASK_STATE_3.equals(taskBean.getState())||TASK_STATE_4.equals(taskBean.getState()))&&!StringUtils.isEmpty(taskBean.getRelateClient())&&taskBean.getClientEndIsGood()==0){
			boolean isCliented=false;
			for (Object clientId : clientIds) {
				if(taskBean.getRelateClient().equals(clientId.toString())){
					isCliented=true;
					break;
				}
			}
			if(isCliented){
				executeRadio.setVisibility(View.GONE);
				sponsorRadio.setVisibility(View.GONE);
				acceptanceRadio.setVisibility(View.GONE);
				commentRadio.setVisibility(View.VISIBLE);
				seekBarLayout.setVisibility(View.GONE);
				titleTv.setText(getString(R.string.pingjia_title));
			}
		} 
		// 拨打号码
		callBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!StringUtils.isEmpty(taskBean.getExecutorPhone())){
				    Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+taskBean.getExecutorPhone()));
				    getActivity().startActivity(intent);
				}
			}
		});
		// 关闭窗体
		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mInputMethodManager.hideSoftInputFromWindow(contentTv.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
				dismiss();
			}
		});
		contentTv.requestFocus(); // EditText获得焦点
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // 显示软键盘
	}
	
	/**判断发送按钮状态**/
	private void updateSeekBarState(){
		if(!StringUtils.isEmpty(contentTv.getText().toString())){
			return;
		}
		if(seekBarLayout.getVisibility()==View.VISIBLE){
			sendBtn.setEnabled(seekBar.getProgress()!=Integer.valueOf(taskBean.getSchedule())?true:false);
		}else{
			sendBtn.setEnabled(false);
		}
	}
	
	/**发送反馈**/
	private void sendFaceBack(){
		String typeState = null,scheduleStr = null,passtype=null,endIsGood=null,clientEndIsGood=null;
		if(TASK_STATE_1.equals(taskBean.getState())){
			if(memberId.equals(taskBean.getExecutor())){
				if (zuaiBtn.isChecked()) {
					typeState = TASK_STATE_1;// 遇到阻碍
				} else if (yanqiBtn.isChecked()) {
					typeState = TASK_STATE_2;// 需要延期
				} else if (orderBtn.isChecked()) {
					typeState = TASK_STATE_3;// 顺序进行
				}
				scheduleStr = String.valueOf(seekBar.getProgress());
			}else if(memberId.equals(taskBean.getSponsor())){
				if (fireBtn.isChecked()) {
					typeState = TASK_STATE_4;// 抓紧处理
				} else if (progressBtn.isChecked()) {
					typeState = TASK_STATE_5;// 进度如何
				}
			}
		}else if(TASK_STATE_2.equals(taskBean.getState())&&memberId.equals(taskBean.getSponsor())){
			if (acceptanceBtn.isChecked()) {
				passtype = TASK_STATE_1;// 验收通过 
			} else if (reworkBtn.isChecked()) {
				passtype = TASK_STATE_0;// 需要返工
			}
		}else if(TASK_STATE_3.equals(taskBean.getState())&&memberId.equals(taskBean.getSponsor())){
			if (goodBtn.isChecked()) {
				endIsGood = TASK_STATE_1;// 好评
			} else if (badBtn.isChecked()) {
				endIsGood = TASK_STATE_2;// 差评
			}
		}else if((TASK_STATE_3.equals(taskBean.getState())||TASK_STATE_4.equals(taskBean.getState()))&&!StringUtils.isEmpty(taskBean.getRelateClient())&&taskBean.getClientEndIsGood()==0){
			boolean isCliented=false;
			for (Object clientId : clientIds) {
				if(taskBean.getRelateClient().equals(clientId.toString())){
					isCliented=true;
					break;
				}
			}
			if(isCliented){
				if (goodBtn.isChecked()) {
					clientEndIsGood = TASK_STATE_1;// 好评
				} else if (badBtn.isChecked()) {
					clientEndIsGood = TASK_STATE_2;// 差评
				}	
			}
		}
		String contentStr=contentTv.getText().toString();
		if(StringUtils.isEmpty(contentStr)&&contentTv.getCompoundDrawables()[0]==null&&seekBarLayout.getVisibility()==View.GONE){
			MsgTools.toast(getActivity(), getString(R.string.input_taskprocess_hint), ResourceMap.LENGTH_SHORT);
			return;
		}else if(seekBarLayout.getVisibility()==View.VISIBLE&&contentTv.getCompoundDrawables()[0]==null&&StringUtils.isEmpty(contentStr)){
			if(seekBar.getProgress()==Integer.valueOf(taskBean.getSchedule())){
				MsgTools.toast(getActivity(), getString(R.string.input_taskprocess_hint), ResourceMap.LENGTH_SHORT);
				return;
			}
		}
		postAddTaskProcess(typeState, scheduleStr,passtype,endIsGood,clientEndIsGood);
	}
	
	/**添加到反馈*/ 
	private void postAddTaskProcess(String type,final String schedule,String passtype,String endIsGood,String clientEndIsGood){
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("taskId", taskBean.getId());
		if (!StringUtils.isEmpty(type)) {
			rp.put("type", type);// 状态
		}
		if (!StringUtils.isEmpty(schedule)) {
			rp.put("schedule", schedule);// 进度
		}
		if (!StringUtils.isEmpty(passtype)) {
			rp.put("passtype", passtype);// 验收标准(1:验收通过,0:需要返工)
		}
		if (!StringUtils.isEmpty(endIsGood)) {
			rp.put("endIsGood", endIsGood);// 评价标准(1:好评,2:差评)
		}
		if (!StringUtils.isEmpty(clientEndIsGood)) {
			rp.put("clientEndIsGood", clientEndIsGood);// 客户好评(1:好评,2:差评)
		}
		String contentStr=contentTv.getText().toString();
		rp.put("content", contentStr);// 内容
		asyncHttpClient.post(getActivity(), Constants.addTaskProcessAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String content = new String(responseBody);
				try {
					boolean success = new JSONObject(content).optBoolean("success", false);
					String message = new JSONObject(content).optString("message");
					String taskState = new JSONObject(content).optString("taskState");
					if (success) {
						MsgTools.toast(getActivity(), getString(R.string.taskprocess_success), ResourceMap.LENGTH_SHORT);
						onRefreshSchedule.onRefreshSchedule(new TaskIdsBean(taskBean.getId(),schedule,taskState,null));
						dismiss();
					} else {
						MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			public void onStart() {
				showProgressDialog();
			};
			public void onFinish() {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			};

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				onFailureToast(error);
			}
		});
	}
	
	/**进度弹出框*/
	private void showProgressDialog(){
		dialog = new Dialog(getActivity(), R.style.mystyle);
		dialog.setContentView(R.layout.loading_dialog);
		dialog.setCancelable(true);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				asyncHttpClient.cancelRequests(getActivity(), true);
			}
		});
		dialog.show();
	}
	
	/**数据请求失败回调*/
	private void onFailureToast(Throwable error){
		if (error instanceof UnknownHostException) {
			MsgTools.toast(getActivity(), getString(R.string.request_network_error), ResourceMap.LENGTH_SHORT);
		} else if (error instanceof HttpResponseException) {
			MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
		} else if (error instanceof SocketTimeoutException) {
			MsgTools.toast(getActivity(), getString(R.string.request_timeout), ResourceMap.LENGTH_SHORT);
		} else {
			MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
		}
	}
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.kzx_faceback_dialog_fragment, container, false);
		return view;
	}

	public interface OnRefreshSchedule{
		void onRefreshSchedule(TaskIdsBean taskIdsBean);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}
}
