package com.android.yijiang.kzx.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.CookieStore;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.backup.FileBackupHelper;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxTaskProcessAdapter;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.AmountInfoBean;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskDetailBean;
import com.android.yijiang.kzx.bean.TaskProcessBean;
import com.android.yijiang.kzx.fragment.KzxTaskDetailInfoFragment.InputHandler;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.PersistentCookieStore;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.http.ResponseHandlerInterface;
import com.android.yijiang.kzx.http.SyncHttpClient;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileSizeUtil;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.LoginActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.MySeekBar;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.ResizeLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 任务反馈
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxTaskProcessFragment extends BaseFragment {

	private String TAG = KzxTaskProcessFragment.class.getName();

	private SwipeRefreshLayout swipeLayout;
	private ListView dateCustomList;
	private TextView img_empty_feed;
	private View footerView;
	private ProgressBar default_load_view;// 默认加载框
	private boolean mHasRequestedMore;

	// 底部菜单布局
	private PopupWindow popupWindow;
	private TextView sendBtn;// 发送
	private EditText contentTv;// 文本内容
	private LinearLayout executeRadio;// 执行人菜单
	private LinearLayout sponsorRadio;// 发起人菜单
	private LinearLayout acceptanceRadio;// 验收菜单
	private LinearLayout commentRadio;// 评价菜单
	private LinearLayout commentView;
	private LinearLayout bottomReserved;
	private LinearLayout bottomMoreAction;
	private CheckBox zuaiBtn;// 遇到阻碍
	private CheckBox yanqiBtn;// 需要延期
	private CheckBox orderBtn;// 顺序进行
	private CheckBox fireBtn;// 抓紧处理
	private CheckBox progressBtn;// 进度如何
	private CheckBox acceptanceBtn;// 验收通过
	private CheckBox reworkBtn;// 需要返工
	private CheckBox goodBtn;// 好评
	private CheckBox badBtn;// 差评
	private TextView seekBarStateTv;//进度状态
	private TextView hintTv;
	private TextView callBtn;//拨打号码
	private TextView imageBtn;// 添加图片
	private TextView seekBarTv;
	private SeekBar seekBar;// 反馈进度条
	private RelativeLayout seekBarLayout;
	
	private ImageView coverBg;
    private static final int BIGGER = 1; 
    private static final int SMALLER = 2; 
    private static final int MSG_RESIZE = 1; 
    private InputHandler mHandler = new InputHandler(); 
    
	private ResizeLayout bottomAction;//底部菜单
	private LinearLayout bottomDivider;

	private TaskDetailBean taskBean;
	private AsyncHttpClient asyncHttpClient;
	private KzxTaskProcessAdapter kzxTaskProcessAdapter;

	private long pageNow = 1;
	private int InputResultCode = 3;
	private Gson gson;
	private Type tt;
	private Dialog dialog;

	private ImageButton backBtn;
	private ImageButton menuBtn;
	
	private MessageReceiver mMessageReceiver;

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static String PHOTO_FILE_NAME = "";
	private List<File> mDeleteFiles=new ArrayList<File>();//要删除的文件集合
	
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

	public static KzxTaskProcessFragment newInstance(TaskDetailBean taskBean) {
		KzxTaskProcessFragment fragment = new KzxTaskProcessFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("taskBean", taskBean);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	/**注册广播*/
	public void registerMessageReceiver() {
		postLoad(true);
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".TASK_PROCESS_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}
	
	/**回调广播*/
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".TASK_PROCESS_RECEIVED_ACTION").equals(intent.getAction())) {
				String action = intent.getStringExtra("action");
				if("add_taskprocess".equals(action)){
					boolean isOld=intent.getBooleanExtra("isOld", false);
					File filePath=new File(intent.getStringExtra("filePath"));
					if(!isOld){
						mDeleteFiles.add(filePath);
					}
					addTaskProcess(filePath);
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new GsonBuilder().create();
		tt = new TypeToken<List<TaskProcessBean>>() { }.getType();
		taskBean = (TaskDetailBean) getArguments().getSerializable("taskBean");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_execute_taskprocess_fragment, null);
		return contentView;
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
		
		coverBg=(ImageView) view.findViewById(R.id.coverBg);
		bottomDivider=(LinearLayout) view.findViewById(R.id.bottomDivider);
		bottomAction=(ResizeLayout) view.findViewById(R.id.bottomAction);
		footerView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer_taskprocess, null);
		sendBtn = (TextView) view.findViewById(R.id.sendBtn);// 发送
		contentTv = (EditText) view.findViewById(R.id.contentTv);// 文本内容
		executeRadio = (LinearLayout) view.findViewById(R.id.executeRadio);
		sponsorRadio = (LinearLayout) view.findViewById(R.id.sponsorRadio);
		acceptanceRadio = (LinearLayout) view.findViewById(R.id.acceptanceRadio);
		commentRadio = (LinearLayout) view.findViewById(R.id.commentRadio);
		commentView= (LinearLayout) view.findViewById(R.id.commentView);
		bottomReserved= (LinearLayout) footerView.findViewById(R.id.bottomReserved);
		bottomMoreAction= (LinearLayout) view.findViewById(R.id.bottomMoreAction);
		zuaiBtn = (CheckBox) view.findViewById(R.id.zuaiBtn);// 遇到阻碍
		yanqiBtn = (CheckBox) view.findViewById(R.id.yanqiBtn);// 需要延期
		orderBtn = (CheckBox) view.findViewById(R.id.orderBtn);// 顺序进行
		fireBtn = (CheckBox) view.findViewById(R.id.fireBtn);// 抓紧处理
		acceptanceBtn= (CheckBox) view.findViewById(R.id.acceptanceBtn);// 验收通过
		reworkBtn= (CheckBox) view.findViewById(R.id.reworkBtn);// 需要返工
		seekBarStateTv = (TextView) view.findViewById(R.id.seekBarStateTv);// 反馈进度条
		hintTv= (TextView) view.findViewById(R.id.hintTv);// 反馈进度条
		goodBtn= (CheckBox) view.findViewById(R.id.goodBtn);// 好评
		badBtn= (CheckBox) view.findViewById(R.id.badBtn);// 差评
		progressBtn = (CheckBox) view.findViewById(R.id.progressBtn);// 进度如何
		imageBtn = (TextView) view.findViewById(R.id.imageBtn);// 添加图片
		callBtn=(TextView)view.findViewById(R.id.callBtn);//拨打号码
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);// 反馈进度条
		seekBarLayout = (RelativeLayout) view.findViewById(R.id.seekBarLayout);
		seekBarTv = (TextView) view.findViewById(R.id.seekBarTv);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_green_dark, android.R.color.holo_orange_dark);
		kzxTaskProcessAdapter = new KzxTaskProcessAdapter(getActivity());
		dateCustomList = (ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.addFooterView(footerView, null, false);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		default_load_view= (ProgressBar) view.findViewById(R.id.default_load_view);
		dateCustomList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		dateCustomList.setStackFromBottom(true);
		dateCustomList.setAdapter(kzxTaskProcessAdapter);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				if (!mHasRequestedMore) {
					postLoad(false);
					mHasRequestedMore = true;
	            }
			}
		});
		dateCustomList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				 if (visibleItemCount == 0 || visibleItemCount == totalItemCount || (firstVisibleItem + visibleItemCount == totalItemCount)) {
					if (dateCustomList.getTranscriptMode() != AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL)
						dateCustomList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	            } else {
	                if (dateCustomList.getTranscriptMode() != AbsListView.TRANSCRIPT_MODE_DISABLED)
	                	dateCustomList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
	            }
			}
		});
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
				int i=seekBar.getProgress();
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
				seekBar.setProgress(Integer.valueOf(taskBean.getSchedule()));				
			}
		});
		// 发送反馈
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KeyBoardUtils.closeKeybord(contentTv, getActivity());
				addTaskProcess(null);
			}
		});
		contentTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEND) {
					addTaskProcess(null);
				}
				return true;
			}
		});
		// popwindow弹出层设置
		View leftMenuView = getActivity().getLayoutInflater().inflate(R.layout.task_process_right_menu_fragment, null);
		popupWindow = new PopupWindow(leftMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.popWindowAnim);
		popupWindow.update();
		leftMenuView.findViewById(R.id.detailInfoBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "task_detail");
				mIntent.putExtra("taskBean", taskBean);
				getActivity().startActivity(mIntent);
			}
		});
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)). 
			     hideSoftInputFromWindow(contentTv.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				getActivity().finish();
			}
		});
		menuBtn = (ImageButton) view.findViewById(R.id.menuBtn);
		menuBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.showAsDropDown(menuBtn);
			}
		});
		// 选择图片
		imageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedPic();
			}
		});
		kzxTaskProcessAdapter.setOnItemClickListener(new KzxTaskProcessAdapter.OnItemClickListener() {
			public void onItemClick(AttachementBean attachementBean) {
				DialogFragment myFragment = KzxAttachementDownloadDialogFragment.newInstance(attachementBean);
				myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
				myFragment.show(getFragmentManager(), TAG);
			}
		});
		//监听键盘弹出
		bottomAction.setOnResizeListener(new ResizeLayout.OnResizeListener() {
			@Override
			public void OnResize(boolean isShowed) {
                Message msg = new Message(); 
                msg.what = 1; 
                msg.obj = isShowed; 
                mHandler.sendMessage(msg); 
			}
		});
		initIsSelfUI();
	}
	
	class InputHandler extends Handler { 
        @Override 
        public void handleMessage(Message msg) { 
            switch (msg.what) { 
            case MSG_RESIZE: 
                if (Boolean.parseBoolean(msg.obj.toString())) { 
                	//渐变动画    从显示（1.0）到隐藏（0.0）  
                    AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 0.3f);  
                    alphaAnim.setDuration(300);  
                    coverBg.startAnimation(alphaAnim);  
                    coverBg.setVisibility(View.VISIBLE);
                    coverBg.clearAnimation();
                	bottomDivider.setVisibility(View.VISIBLE);
                } else { 
                	//渐变动画    从显示（1.0）到隐藏（0.0）  
                    AlphaAnimation alphaAnim = new AlphaAnimation(0.3f, 0.0f);  
                    alphaAnim.setDuration(300);  
                    coverBg.startAnimation(alphaAnim);  
                    coverBg.setVisibility(View.GONE);
                    coverBg.clearAnimation();
                	bottomDivider.setVisibility(View.GONE);
                } 
                break; 
            default: 
                break; 
            } 
            super.handleMessage(msg); 
        } 
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
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerMessageReceiver();
	}
	
	/**设置底部反馈信息状态*/
	private void initIsSelfUI(){

		final String memberId=getContext().getKzxTokenBean().getEncryptMemberId();
		final JSONArray clientIds=JSON.parseArray(getContext().getKzxTokenBean().getClientIds());
		
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
		}else if(TASK_STATE_3.equals(taskBean.getState())&&memberId.equals(taskBean.getSponsor())){
			executeRadio.setVisibility(View.GONE);
			sponsorRadio.setVisibility(View.GONE);
			acceptanceRadio.setVisibility(View.GONE);
			commentView.setVisibility(View.VISIBLE);
			commentRadio.setVisibility(View.VISIBLE);
			seekBarLayout.setVisibility(View.GONE);
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
				commentView.setVisibility(View.VISIBLE);
				commentRadio.setVisibility(View.VISIBLE);
				seekBarLayout.setVisibility(View.GONE);
			}
		} 
		//添加底部预留空白区域
//		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//		bottomMoreAction.measure(w, h);
//		bottomReserved.getLayoutParams().height=bottomMoreAction.getMeasuredHeight();
	}
	
	/**选择图片上传模式*/
	private void selectedPic(){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setItems(getResources().getStringArray(R.array.avatar), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: gallery(imageBtn); break;
				case 1: camera(imageBtn); break;
				default: break;}
				dialog.dismiss();
			}
		}).create().show();
	}

	/**加载任务反馈列表数据*/
	private void postLoad(final boolean isFirstLoad) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("taskId", taskBean.getId());
		rp.put("pageNow", pageNow + "");// 页码
		asyncHttpClient.post(getActivity(), Constants.taskProcessAPI, rp, taskProcessResponseHandler);
	}
	
	/**加载任务反馈列表数据回调*/
	AsyncHttpResponseHandler taskProcessResponseHandler=new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			if (kzxTaskProcessAdapter.isEmpty()) {
				default_load_view.setVisibility(View.VISIBLE);
				img_empty_feed.setVisibility(View.GONE);
			} else {
				swipeLayout.setRefreshing(true);
			}
		}
		@Override
		public void onFinish() {
			default_load_view.setVisibility(View.GONE);
			swipeLayout.setRefreshing(false);
			super.onFinish();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				long pageSize = new JSONObject(content).optLong("pageSize", 0);
				long rowCount = new JSONObject(content).optLong("rowCount", 0);
				long totalPage = (rowCount + pageSize - 1) / pageSize;
				String records = new JSONObject(content).optString("records");
				if (!StringUtils.isEmpty(records) && !"[]".equals(records)) {
					List<TaskProcessBean> taskBeanList = new ArrayList<TaskProcessBean>();
					taskBeanList.addAll((List<TaskProcessBean>) gson.fromJson(records, tt));
					for (TaskProcessBean taskProcessBean : taskBeanList) {
						if (taskProcessBean.isSelf()) {
							// 自己的反馈信息
							taskProcessBean.setViewType(kzxTaskProcessAdapter.VALUE_RIGHT_TEXT);
						} else {
							taskProcessBean.setViewType(kzxTaskProcessAdapter.VALUE_LEFT_TEXT);
						}
					}
					kzxTaskProcessAdapter.setDataForLoader(taskBeanList, kzxTaskProcessAdapter.isEmpty()?true:false);
					scrollMyListViewToBottom(taskBeanList.size()-1);
					if (pageNow == totalPage) {
						// 最后一页
						swipeLayout.setEnabled(false);
						mHasRequestedMore = true;
					} else {
						// 新增页码
						pageNow++;
						mHasRequestedMore = false;
					}
				} else {
					// 空数据
					kzxTaskProcessAdapter.clearDataForLoader();
					img_empty_feed.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};

	/**添加新反馈信息*/
	private void addTaskProcess(File mFile){
		String typeState = null,scheduleStr = null,passtype=null,endIsGood=null,clientEndIsGood=null;
		
		final String memberId=getContext().getKzxTokenBean().getEncryptMemberId();
		final JSONArray clientIds=JSON.parseArray(getContext().getKzxTokenBean().getClientIds());
		
		// 判断任务是执行人还是发起人
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
		if(StringUtils.isEmpty(contentStr)&&mFile==null&&contentTv.getCompoundDrawables()[0]==null&&seekBarLayout.getVisibility()==View.GONE){
			MsgTools.toast(getActivity(), "请填写反馈内容", Toast.LENGTH_SHORT);
			return;
		}else if(seekBarLayout.getVisibility()==View.VISIBLE){
			if(seekBar.getProgress()==Integer.valueOf(taskBean.getSchedule())&&mFile==null&&contentTv.getCompoundDrawables()[0]==null&&StringUtils.isEmpty(contentStr)){
				MsgTools.toast(getActivity(), getString(R.string.input_taskprocess_hint), ResourceMap.LENGTH_SHORT);
				return;
			}
		}
		postAddTaskProcess(typeState,scheduleStr,passtype,endIsGood,clientEndIsGood,mFile);
	}

	/**添加到反馈*/
	private void postAddTaskProcess(String type,String schedule,String passtype,String endIsGood,String clientEndIsGood,final File files) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("taskId", taskBean.getId());//任务详情ID
		if(!StringUtils.isEmpty(type)){
			rp.put("type", type);//状态
		}
		if(!StringUtils.isEmpty(schedule)){
			rp.put("schedule", schedule);//进度
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
		if(files!=null){
			try {
				rp.put("Filedata", files);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		asyncHttpClient.post(getActivity(), Constants.addTaskProcessAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				if(files!=null){
					showDialog();
				}
			   	zuaiBtn.setChecked(false);
            	yanqiBtn.setChecked(false);
            	orderBtn.setChecked(false);
            	fireBtn.setChecked(false);
            	acceptanceBtn.setChecked(false);
            	reworkBtn.setChecked(false);
            	goodBtn.setChecked(false);
            	badBtn.setChecked(false);
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String content = new String(responseBody);
				try {
					boolean success = new JSONObject(content).optBoolean("success", false);
					String message = new JSONObject(content).optString("message");
					String data = new JSONObject(content).optString("data");
					String taskState = new JSONObject(content).optString("taskState");
					if (success) {
						// 状态改变的时候关闭当前并且重新刷新详情页
						if(!taskState.equals(taskBean.getState())){
							Intent mIntent=new Intent(getActivity().getPackageName()+".TASK_DETAIL_RECEIVED_ACTION");
							mIntent.putExtra("action", "change_schedule");
							mIntent.putExtra("schedule", taskBean.getSchedule());
							mIntent.putExtra("message", message);
							mIntent.putExtra("state", taskState+"");
							getActivity().sendBroadcast(mIntent);
							getActivity().finish();
						}else{
							addNewTaskProcess(data);
						}
					} else {
						MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onFinish() {
				if(files!=null){
					dialog.dismiss();
				}
				//清空输入框和收起输入框
				contentTv.setText("");
	            Message msg = new Message(); 
	            msg.what = 1; 
	            msg.obj = false; 
	            mHandler.sendMessage(msg); 
			};

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				onFailureToast(error);
			}
		});
	}

	/**显示进度条**/
	private void showDialog(){
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
	
	/** 添加新视图到ScrollView到底部  */
	private void addNewTaskProcess(String data){
		try {
			if(kzxTaskProcessAdapter.getCount()==0){
				pageNow=1;
				postLoad(true);
				return;
			}
			// 重新设置进度信息
			taskBean.setSchedule(new JSONObject(data).optString("schedule",taskBean.getSchedule()));
			// 刷新详情页的进度条
			Intent mIntent=new Intent(getActivity().getPackageName()+".TASK_DETAIL_RECEIVED_ACTION");
			mIntent.putExtra("action", "change_schedule");
			mIntent.putExtra("schedule", taskBean.getSchedule());
			getActivity().sendBroadcast(mIntent);
			// 创建一条即时消息
			TaskProcessBean taskProcessBean = new TaskProcessBean();
			taskProcessBean.setAttachement(new JSONObject(data).optString("attachement",""));
			taskProcessBean.setCreater(new JSONObject(data).optString("creater",""));
			taskProcessBean.setCreaterIcon(new JSONObject(data).optString("createrIcon",""));
			taskProcessBean.setCreaterName(new JSONObject(data).optString("createrName",""));
			taskProcessBean.setContent(new JSONObject(data).optString("content",""));
			taskProcessBean.setTaskId(taskBean.getId());
			taskProcessBean.setCreateTime(new JSONObject(data).optLong("createTime", System.currentTimeMillis()));
			taskProcessBean.setType(new JSONObject(data).optString("type",""));
			taskProcessBean.setViewType(kzxTaskProcessAdapter.VALUE_RIGHT_TEXT);
			kzxTaskProcessAdapter.setDataForLoaderOne(taskProcessBean);
			scrollMyListViewToBottom(kzxTaskProcessAdapter.getCount() - 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/** 滑动到底部  */
	private void scrollMyListViewToBottom(final int position) {
		dateCustomList.post(new Runnable() {
	        @Override
	        public void run() {
	            // Select the last row so it will scroll into view...
	        	dateCustomList.setSelection(position);
	        }
	    });
	}
	
	/** 从相册获取 */
	public void gallery(View view) {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/** 从相机获取 */
	public void camera(View view) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (FileUtil.hasSdcard()) {
			PHOTO_FILE_NAME=FileUtil.renameFile();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}
	
	/** 上传图片 */
	private void uploadImageBitmap(final boolean isOld,final File savedImages){
		DialogFragment myFragment = KzxImageViewDialogFragment.newInstance(isOld,savedImages.getAbsolutePath(),"TASK_PROCESS_RECEIVED_ACTION");
		myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
		myFragment.show(getFragmentManager(), TAG);
	}
	
	/** 上传图片回调 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data == null) {
				return;
			}
			// 得到图片的全路径
			Uri uri = data.getData();
			if(uri==null){
				return;
			}
			File newFile=null;
			//判断图片大小是否有超过100K
			if(FileSizeUtil.getFileOrFilesSize(FileUtil.getFileByUri(getActivity(), uri), FileSizeUtil.SIZETYPE_KB)>Constants.Filesize){
				newFile=BitmapUtil.transImage(FileUtil.getFileByUri(getActivity(), uri), FileUtil.getFileFromUrl().getAbsolutePath(), 100);
				uploadImageBitmap(false,newFile);
			}else{
				newFile=new File(FileUtil.getFileByUri(getActivity(), uri));
				uploadImageBitmap(true,newFile);
			}
		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (!FileUtil.hasSdcard()) {
				MsgTools.toast(getActivity(), getString(R.string.upload_error), Toast.LENGTH_SHORT);
				return;
			}
			File tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
			if(tempFile.exists()){
				File newFile=BitmapUtil.transImage(tempFile.getAbsolutePath(), FileUtil.getFileFromUrl().getAbsolutePath(), 100);
		    	uploadImageBitmap(false,newFile);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
		if(mMessageReceiver!=null)
			getActivity().unregisterReceiver(mMessageReceiver);
		if(!StringUtils.isNullOrEmpty(mDeleteFiles)){
			for (File mDeleteFile : mDeleteFiles) {
				mDeleteFile.delete();
			}
		}
	}

}
