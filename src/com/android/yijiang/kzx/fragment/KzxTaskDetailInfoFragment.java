package com.android.yijiang.kzx.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxAcceptanceAdapter;
import com.android.yijiang.kzx.adapter.KzxAttachmentAdapter;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxIncentiveAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxRelatedAdapter;
import com.android.yijiang.kzx.adapter.KzxTaskProcessAdapter;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.ClientBean;
import com.android.yijiang.kzx.bean.CopyTaskBean;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.NewTaskProcessBean;
import com.android.yijiang.kzx.bean.TargetBean;
import com.android.yijiang.kzx.bean.TargetCanRelateBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskCanRelateBean;
import com.android.yijiang.kzx.bean.TaskDetailBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.bean.TaskProcessBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ArithUtils;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.FileSizeUtil;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.transformer.CropSquareTransformation;
import com.android.yijiang.kzx.ui.AppManager;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.LoginActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.FloatLabeledEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.MyListView;
import com.android.yijiang.kzx.widget.MySeekBar;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.ResizeLayout;
import com.android.yijiang.kzx.widget.StickyScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 任务详情
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxTaskDetailInfoFragment extends BaseFragment {
	private String TAG = KzxTaskDetailInfoFragment.class.getName();
	private FloatLabeledEditText titleTv;// 标题
	private FloatLabeledEditText contentTv;// 内容
	private TextView executionTv;// 执行人→抄送人
	private TextView taskTimeTv;// 任务时间
	private ImageView taskTimeIv;//任务超期时间图标
	private TextView taskDayTv;// 任务超期时间
	private ProgressBar acceptanceBar;// 验收标准进度
	private TextView acceptanceBarTv;
	private TextView acceptanceBarStateTv;
	private TextView acceptanceAmountTv;// 验收标准统计
	private TextView attachmentTv;// 附件数
	private TextView relateTargetDescTv;// 关联目标描述
	private TextView clientNameTv;// 客户姓名
	private TextView clientPhoneTv;// 客户电话号码
	private TextView relateCopyTv;//抄送
	private ImageView flagCb;// 是否订阅
	private TextView taskProcessBtn;//任务反馈
	private TextView img_empty_feed;// 默认信息
	private ProgressBar default_load_view;// 默认加载框
	private StickyScrollView contentScrollView;// 滚动内容
	private AsyncHttpClient asyncHttpClient;
	
	private TextView relateTargetDescEmptyTv;
	private TextView relateTaskEmptyTv;
	private TextView clientNameEmptyTv;
	
	private SwipeRefreshLayout swipeLayout;

	private LinearLayout acceptanceCustomList;// 验收标准
	private LinearLayout attachmentCustomList;// 附件
	private LinearLayout taskCustomList;// 相关任务
	private LinearLayout taskProcessCustomList;//任务反馈

	private ImageView coverBg;
    private static final int BIGGER = 1; 
    private static final int SMALLER = 2; 
    private static final int MSG_RESIZE = 1; 
    private InputHandler mHandler = new InputHandler(); 
    
	private ResizeLayout bottomAction;//底部菜单
	private LinearLayout bottomDivider;
	private LinearLayout mLinearLayout;
	private LinearLayout mLinearLayoutHeader;
	private ImageView headerIv;
	private TextView headerTv;
	private ValueAnimator mAnimator;
	private String taskId;
	private boolean notDoDecrypt;//用于是否加密

	// 底部View
	private int InputResultCode = 3;
	private Dialog dialog;
	private int successAm = 0,totalAm = 0;
	
	private TextView sendBtn;// 发送
	private EditText contentCt;// 文本内容
	private LinearLayout bottomReserved;//底部预留区域
	private LinearLayout executeRadio;// 执行人菜单
	private LinearLayout sponsorRadio;// 发起人菜单
	private LinearLayout commentView;
	private RelativeLayout seekBarLayout;
	private LinearLayout acceptanceRadio;// 验收菜单
	private LinearLayout commentRadio;// 评价菜单
	private CheckBox zuaiBtn;// 遇到阻碍
	private CheckBox yanqiBtn;// 需要延期
	private CheckBox orderBtn;// 顺序进行
	private CheckBox fireBtn;// 抓紧处理
	private CheckBox acceptanceBtn;// 验收通过
	private CheckBox reworkBtn;// 需要返工
	private CheckBox goodBtn;// 好评
	private CheckBox badBtn;// 差评
	private TextView callBtn;//拨打号码
	private CheckBox progressBtn;// 进度如何
	private TextView imageBtn;// 添加图片
	private TextView seekBarTv;
	private TextView seekBarStateTv;//进度状态
	private TextView hintTv;
	private MySeekBar seekBar;// 反馈进度条
	private List<File> mDeleteFiles=new ArrayList<File>();//要删除的文件集合
	
	// 头部View
	private PopupWindow popupWindow;
	private LinearLayout forwardedBtn;// 转发
	private LinearLayout editBtn;// 编辑
	private TextView cancelBtn;// 取消
	private ImageButton backBtn;
	private ImageButton menuBtn;
	
	private String detailStr;
	private String type;
	private TaskDetailBean taskDetailBean;
	private MessageReceiver mMessageReceiver;
	
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static String PHOTO_FILE_NAME = "";
	
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
	private Drawable yanqiDrawableWhite;
	private Drawable shunxuDrawableWhite;
	private Drawable zhuajinDrawableWhite;
	private Drawable jinduDrawableWhite;
	private Drawable yanshouDrawable;
	private Drawable fangongDrawable;
	private Drawable haopingDrawable;
	private Drawable chapingDrawable;
	private DisplayImageOptions options;

	public static KzxTaskDetailInfoFragment newInstance(String taskId,boolean notDoDecrypt,String type) {
		KzxTaskDetailInfoFragment fragment = new KzxTaskDetailInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putString("taskId", taskId);
		bundle.putString("type", type);
		bundle.putBoolean("notDoDecrypt", notDoDecrypt);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskId = getArguments().getString("taskId","");
		type= getArguments().getString("type","");
		notDoDecrypt=getArguments().getBoolean("notDoDecrypt",false);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_avatar_120)
		.showImageForEmptyUri(R.drawable.ic_avatar_120)
		.showImageOnFail(R.drawable.ic_avatar_120)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_task_info_fragment, null);
		return contentView;
	}
	
	// 注册广播
	public void registerMessageReceiver() {
		postLoad();
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".TASK_DETAIL_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);	
		
		zuaiDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_zuai);
		yanqiDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_more);
		shunxuDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_ok);
		zhuajinDrawable= getResources().getDrawable(R.drawable.kzx_ic_task_process_fire);
		jinduDrawable = getResources().getDrawable(R.drawable.kzx_ic_task_process_more);
		yanqiDrawableWhite = getResources().getDrawable(R.drawable.kzx_ic_task_process_more_white);
		shunxuDrawableWhite = getResources().getDrawable(R.drawable.kzx_ic_task_process_ok_white);
		zhuajinDrawableWhite= getResources().getDrawable(R.drawable.kzx_ic_task_process_fire_white);
		jinduDrawableWhite = getResources().getDrawable(R.drawable.kzx_ic_task_process_more_white);
		yanshouDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_arrow_action);
		fangongDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_arrow_back_action);
		haopingDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_good_comment_action);
		chapingDrawable = getResources().getDrawable(R.drawable.kzx_ic_taskprocess_bad_comment_action);
		
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_red_dark, android.R.color.holo_orange_dark,
				android.R.color.holo_green_dark);
		coverBg=(ImageView)view.findViewById(R.id.coverBg);
		sendBtn = (TextView) view.findViewById(R.id.sendBtn);// 发送
		contentCt = (EditText) view.findViewById(R.id.contentCt);// 文本内容
		executeRadio = (LinearLayout) view.findViewById(R.id.executeRadio);
		sponsorRadio = (LinearLayout) view.findViewById(R.id.sponsorRadio);
		commentView= (LinearLayout) view.findViewById(R.id.commentView);
		seekBarLayout = (RelativeLayout) view.findViewById(R.id.seekBarLayout);
		bottomReserved = (LinearLayout) view.findViewById(R.id.bottomReserved);
		acceptanceRadio = (LinearLayout) view.findViewById(R.id.acceptanceRadio);
		commentRadio = (LinearLayout) view.findViewById(R.id.commentRadio);
		bottomDivider=(LinearLayout) view.findViewById(R.id.bottomDivider);
		bottomAction=(ResizeLayout) view.findViewById(R.id.bottomAction);
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
		imageBtn = (TextView) view.findViewById(R.id.imageBtn);// 添加图片
		seekBar = (MySeekBar) view.findViewById(R.id.seekBar);// 反馈进度条
		seekBarStateTv = (TextView) view.findViewById(R.id.seekBarStateTv);// 反馈进度条
		seekBarTv = (TextView) view.findViewById(R.id.seekBarTv);
		hintTv = (TextView) view.findViewById(R.id.hintTv);
		relateCopyTv= (TextView) view.findViewById(R.id.relateCopyTv);
		
		titleTv = (FloatLabeledEditText) view.findViewById(R.id.titleTv);// 标题
		titleTv.getEditText().setEnabled(false);
		contentTv = (FloatLabeledEditText) view.findViewById(R.id.contentTv);// 内容
		contentTv.getEditText().setEnabled(false);
		executionTv = (TextView) view.findViewById(R.id.executionTv);// 执行人→抄送人
		taskTimeTv = (TextView) view.findViewById(R.id.taskTimeTv);// 任务时间
		taskDayTv = (TextView) view.findViewById(R.id.taskDayTv);// 任务超期时间
		taskTimeIv=(ImageView)view.findViewById(R.id.taskTimeIv);
		acceptanceBar = (ProgressBar) view.findViewById(R.id.acceptanceBar);// 验收标准进度
		acceptanceBarTv= (TextView) view.findViewById(R.id.acceptanceBarTv);
		acceptanceBarStateTv= (TextView) view.findViewById(R.id.acceptanceBarStateTv);
		acceptanceAmountTv = (TextView) view.findViewById(R.id.acceptanceTv);// 验收标准统计
		attachmentTv = (TextView) view.findViewById(R.id.attachmentTv);// 附件数
		relateTargetDescTv = (TextView) view.findViewById(R.id.relateTargetDescTv);// 关联目标描述
		clientNameTv = (TextView) view.findViewById(R.id.clientNameTv);// 客户姓名
		clientPhoneTv = (TextView) view.findViewById(R.id.clientPhoneTv);// 客户电话号码
		flagCb = (ImageView) view.findViewById(R.id.flagCb);// 是否订阅
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		taskProcessBtn=(TextView)view.findViewById(R.id.taskProcessBtn);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		contentScrollView = (StickyScrollView) view.findViewById(R.id.contentScrollView);
		acceptanceCustomList = (LinearLayout) view.findViewById(R.id.acceptanceCustomList);
		attachmentCustomList = (LinearLayout) view.findViewById(R.id.attachmentCustomList);
		taskCustomList = (LinearLayout) view.findViewById(R.id.taskCustomList);
		taskProcessCustomList=(LinearLayout)view.findViewById(R.id.taskProcessCustomList);
		relateTargetDescEmptyTv = (TextView) view.findViewById(R.id.relateTargetDescEmptyTv);
		relateTaskEmptyTv= (TextView) view.findViewById(R.id.relateTaskEmptyTv);
		clientNameEmptyTv= (TextView) view.findViewById(R.id.clientNameEmptyTv);
		// 隐藏伸缩动画
		headerTv = (TextView) view.findViewById(R.id.headerTv);
		headerIv = (ImageView) view.findViewById(R.id.headerIv);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.expandable);
		mLinearLayout.setVisibility(View.GONE);
		mLinearLayoutHeader = (LinearLayout) view.findViewById(R.id.header);

		// Add onPreDrawListener
		mLinearLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				mLinearLayout.getViewTreeObserver().removeOnPreDrawListener(this);
				mLinearLayout.setVisibility(View.GONE);

				final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
				final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
				mLinearLayout.measure(widthSpec, heightSpec);

				mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
				return true;
			}
		});

		mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				headerIv.clearAnimation();
				if (mLinearLayout.getVisibility() == View.GONE) {
					expand();
					Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.imageview_rotate);
					LinearInterpolator lin = new LinearInterpolator();
					operatingAnim.setInterpolator(lin);
					operatingAnim.setFillAfter(true);
					headerIv.startAnimation(operatingAnim);
					headerTv.setText("收起");
				} else {
					collapse();
					Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.imageview_rotate_2);
					LinearInterpolator lin = new LinearInterpolator();
					operatingAnim.setInterpolator(lin);
					operatingAnim.setFillAfter(true);
					headerIv.startAnimation(operatingAnim);
					headerTv.setText("更多");
				}
			}
		});
		// 上拉监听
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				postLoad();
			}
		});
		// 头部事件
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KeyBoardUtils.closeKeybord(contentCt, getActivity());
				getActivity().finish();
				if(StringUtils.isEmpty(type)){
					Intent i = new Intent(getActivity(), MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
				}
			}
		});
		// popwindow弹出层设置
		View leftMenuView = getActivity().getLayoutInflater().inflate(R.layout.task_info_right_menu_fragment, null);
		popupWindow = new PopupWindow(leftMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.popWindowAnim);
		popupWindow.update();
		forwardedBtn = (LinearLayout) leftMenuView.findViewById(R.id.forwardedBtn);// 转发
		editBtn = (LinearLayout) leftMenuView.findViewById(R.id.editBtn);// 编辑
		cancelBtn = (TextView) leftMenuView.findViewById(R.id.cancelBtn);// 取消
		// 弹出层事件
		menuBtn = (ImageButton) view.findViewById(R.id.menuBtn);
		menuBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.showAsDropDown(menuBtn);
			}
		});
		// 关注事件
		flagCb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (flagCb.getDrawable().getLevel()) {
				case 0:
					postAttention(Constants.taskAttentionAPI);
					break;
				case 1:
					postAttention(Constants.removeAttentionAPI);
					break;
				default:
					break;
				}
			}
		});
		//////////////// 底部菜单////////////////////////////
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
					contentCt.setCompoundDrawables(zuaiDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
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
					contentCt.setCompoundDrawables(yanqiDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
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
					contentCt.setCompoundDrawables(shunxuDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
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
					contentCt.setCompoundDrawables(zhuajinDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
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
					contentCt.setCompoundDrawables(jinduDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
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
					contentCt.setCompoundDrawables(yanshouDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
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
					contentCt.setCompoundDrawables(fangongDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
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
					contentCt.setCompoundDrawables(haopingDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
				
			}
		});
		// 差评
		badBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					sendBtn.setEnabled(true);
					goodBtn.setChecked(false);
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					chapingDrawable.setBounds(0, 0, chapingDrawable.getMinimumWidth(), chapingDrawable.getMinimumHeight());
					contentCt.setCompoundDrawables(chapingDrawable, null, null, null); // 设置左图标
				} else {
					updateSeekBarState();
					contentCt.setCompoundDrawables(null, null, null, null); // 设置左图标
				}
				
			}
		});
		contentCt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(contentCt.getCompoundDrawables()[0]!=null){
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
		// 选择图片
		imageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedPic();
			}
		});
		contentCt.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEND) {
					KeyBoardUtils.closeKeybord(contentCt, getActivity());
					addTaskProcess(null, null);
				}
				return true;
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
                	contentCt.setText("");
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
		if(!StringUtils.isEmpty(contentCt.getText().toString())){
			return;
		}
		if(seekBarLayout.getVisibility()==View.VISIBLE){
			sendBtn.setEnabled(seekBar.getProgress()!=Integer.valueOf(taskDetailBean.getSchedule())?true:false);
		}else{
			sendBtn.setEnabled(false);
		}
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerMessageReceiver();
				
	}
	
	/**选择图片上传模式*/
	private void selectedPic(){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setItems(getResources().getStringArray(R.array.avatar), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: gallery(imageBtn); break;
				case 1: camera(imageBtn); break;
				default: break;
				}
				dialog.dismiss();
			}
		}).create().show();
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
		notDoDecrypt=false;
	}

	/**回调广播*/ 
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".TASK_DETAIL_RECEIVED_ACTION").equals(intent.getAction())) {
				String action = intent.getStringExtra("action");
				if ("reload".equals(action)) {
					postLoad();
				}else if("add_taskprocess".equals(action)){
					boolean isOld=intent.getBooleanExtra("isOld", false);
					File filePath=new File(intent.getStringExtra("filePath"));
					if(!isOld){
						mDeleteFiles.add(filePath);
					}
					addTaskProcess(filePath,null);
				}else if("change_schedule".equals(action)){
					String schedule=intent.getStringExtra("schedule");
					String state=intent.getStringExtra("state");
					String message=intent.getStringExtra("message");
					taskDetailBean.setSchedule(schedule);
					seekBar.setProgress(Integer.valueOf(schedule));
					acceptanceBar.setProgress(seekBar.getProgress());
					acceptanceBarTv.setText(seekBar.getProgress()+"%");
					changeProgressState();
					ApplicationController.setTaskIdsBean(new TaskIdsBean(taskId, taskDetailBean.getSchedule(),taskDetailBean.getState(),null));
					if(!StringUtils.isEmpty(state)&&!state.equals(taskDetailBean.getState())){
						reloadDataByDialog(message,state);
					}
				}
			}
		}
	}
	
	/**任务状态变化的时候*/ 
	private void reloadDataByDialog(final String message,final String taskState) {
		asyncHttpClient.cancelRequests(getActivity(), true);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ApplicationController.setTaskIdsBean(new TaskIdsBean(taskId, taskDetailBean.getSchedule(),taskState,null));
				MsgTools.toast(getActivity(), message, Toast.LENGTH_LONG);
				postLoad();
			}
		});
	}

	/**请求任务详情数据*/
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("taskId", taskId);// 任务详情ID
		if(notDoDecrypt){
			rp.put("notDoDecrypt", notDoDecrypt);
		}
		asyncHttpClient.post(getActivity(), Constants.queryTaskDetailAPI, rp, detailResponseHandler);
	}

	/**请求任务详情数据回调*/
	AsyncHttpResponseHandler detailResponseHandler = new AsyncHttpResponseHandler() {
		private void initView(){
			contentCt.setCompoundDrawables(null, null, null, null); // qingchu zuo tubiao!
			if(default_load_view.getVisibility()==View.GONE){
				swipeLayout.setRefreshing(true);
			}else{
				contentScrollView.setVisibility(View.GONE);
				default_load_view.setVisibility(View.VISIBLE);
			}
		}
		private void fishiedView(){
			default_load_view.setVisibility(View.GONE);
			swipeLayout.setRefreshing(false);
		}
		@Override
		public void onStart() {
			super.onStart();
			initView();
		}
		@Override
		public void onFinish() {
			super.onFinish();
			fishiedView();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				String data = new JSONObject(content).optString("data", null);
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				if (!success) {
					MsgTools.toast(getActivity(), message, Toast.LENGTH_LONG);
				} else {
					if (!StringUtils.isEmpty(data)) {
						Gson gson = new Gson();
						String clientStr = new JSONObject(data).optString("client", "");// 关联客户
						String taskStr = new JSONObject(data).optString("task", "");// 任务详情
						String copytoList = new JSONObject(data).optString("copytoList", "");// 抄送列表
						String relateTasks = new JSONObject(data).optString("relateTasks", "");// 关联任务
						String target = new JSONObject(data).optString("target", "");// 关联目标
						boolean isSelf = new JSONObject(data).optBoolean("isSelf", false);// 是否自身任務
						ClientBean clientBean = gson.fromJson(clientStr, ClientBean.class);
						taskDetailBean = JSON.parseObject(taskStr, TaskDetailBean.class);
						taskDetailBean.setSelf(isSelf);
						setTaskId(taskDetailBean.getId());//重新设置ID
						detailStr=data;//用于传递到编辑任务或者是转发任务
						List<AcceptanceBean> acceptanceList = gson.fromJson(taskDetailBean.getAcceptStandard(), new TypeToken<List<AcceptanceBean>>() {}.getType());
						List<AttachementBean> attachementList = gson.fromJson(taskDetailBean.getAttachement(), new TypeToken<List<AttachementBean>>() {}.getType());
						TargetBean targetBean = gson.fromJson(target, TargetBean.class);
						List<TaskDetailBean> taskDetailList = gson.fromJson(relateTasks, new TypeToken<List<TaskDetailBean>>() {}.getType());
						List<CopyTaskBean> copyTaskList = gson.fromJson(copytoList, new TypeToken<List<CopyTaskBean>>() {}.getType());
						NewTaskProcessBean newTaskProcessBean=gson.fromJson(taskDetailBean.getLastProcess(), NewTaskProcessBean.class);
						initIsSelfUI(taskDetailBean);
						// 关联客户
						initClientUI(clientBean);
						// 抄送
						initCopyUI(copyTaskList);
						// 任务详情
						initTaskDetailUI(taskDetailBean);
						// 验收标准
						initAccptanceUI(acceptanceList, taskDetailBean);
						// 附件
						initAttachementUI(attachementList);
						// 关联目标
						initTargetUI(targetBean);
						// 关联任务
						initTaskUI(taskDetailList);
						// 最新反馈
						if(newTaskProcessBean!=null){
							newTaskProcessBean.setType(taskDetailBean.getLastProcessType());
						}
						initTaskProcessUI(newTaskProcessBean);
						//初始化滑动模块最小值
						initSeekBarUI(taskDetailBean);
						// 拨打号码
						callBtn.setOnClickListener(callClick);
						//任务反馈
						taskProcessBtn.setOnClickListener(taskProcessClick);
						//任务编辑
						editBtn.setOnClickListener(modifyTaskClick);
						//任务转发
						forwardedBtn.setOnClickListener(forwardedTaskClick);
						// 任务取消
						cancelBtn.setOnClickListener(cancelTaskClick);
						// 发送反馈
						sendBtn.setOnClickListener(sendTaskProcessClick);
						contentScrollView.setVisibility(View.VISIBLE);
						if(!StringUtils.isNullOrEmpty(acceptanceList)){
							expand();
							headerTv.setText("收起");
						}
					} else {
						img_empty_feed.setVisibility(View.VISIBLE);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				MsgTools.toast(getActivity(), getString(R.string.request_error), Toast.LENGTH_LONG);
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};
	
	/**抄送**/
	private void initCopyUI(List<CopyTaskBean> copyTaskList){
		if(!StringUtils.isNullOrEmpty(copyTaskList)){
			StringBuffer buffer=new StringBuffer();
			for (CopyTaskBean copyTaskBean : copyTaskList) {
				buffer.append(copyTaskBean.getMemberName());
				buffer.append(";");
			}
			relateCopyTv.setText(buffer.toString());
			relateCopyTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showAcceptanceInfoDialog(getString(R.string.task_copied_hint),relateCopyTv.getText().toString());
				}
			});
		}else{
			relateCopyTv.setText(getString(R.string.task_target_empty_hint));
		}
	}
	
	/**判断任务是执行人还是发起人*/
	private void initIsSelfUI(TaskDetailBean taskBean){
		
		final String memberId=getContext().getKzxTokenBean().getEncryptMemberId();
		final JSONArray clientIds=JSON.parseArray(getContext().getKzxTokenBean().getClientIds());
		bottomAction.setVisibility(View.VISIBLE);
		menuBtn.setVisibility(View.VISIBLE);
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
				commentView.setVisibility(View.VISIBLE);
				executeRadio.setVisibility(View.GONE);
				sponsorRadio.setVisibility(View.GONE);
				acceptanceRadio.setVisibility(View.GONE);
				commentRadio.setVisibility(View.VISIBLE);
				seekBarLayout.setVisibility(View.GONE);
			}
		} 
		//设置右上角菜单信息
		if(memberId.equals(taskBean.getSponsor())||memberId.equals(taskBean.getExecutor())){
			forwardedBtn.setVisibility(View.VISIBLE);
		}
		if(memberId.equals(taskBean.getSponsor())&&"0".equals(taskBean.getEndIsGood())&&taskBean.getClientEndIsGood()==0){
			editBtn.setVisibility(View.VISIBLE);
		}
		if(memberId.equals(taskBean.getSponsor())&&TASK_STATE_1.equals(taskBean.getState())){
			cancelBtn.setVisibility(View.VISIBLE);
		}
		//好评阶段自动弹出反馈框
		if(TASK_STATE_3.equals(taskBean.getState())){
			KeyBoardUtils.openKeybord(contentCt, getActivity());
		}
		
	}
	
	/**关联客户UI设置*/
	private void initClientUI(ClientBean clientBean){
		if (clientBean != null) {
			clientNameEmptyTv.setVisibility(View.GONE);
			clientNameTv.setVisibility(View.VISIBLE);
			clientNameTv.setText(clientBean.getName());
		} else {
			clientNameEmptyTv.setVisibility(View.VISIBLE);
		}
	}
	
	/**任务详情UI设置*/
	private void initTaskDetailUI(TaskDetailBean taskDetailBean){
		acceptanceBar.setProgress(Integer.valueOf(taskDetailBean.getSchedule()));
		acceptanceBarTv.setText(taskDetailBean.getSchedule()+"%");
		changeProgressState();
		titleTv.setText(taskDetailBean.getTitle());
		contentTv.setText(taskDetailBean.getContent());
		executionTv.setText(taskDetailBean.getSponsorName() + " → " + taskDetailBean.getExecutorName());
		String startTime = DateUtils.getStrTime(taskDetailBean.getStartTime(), "MM/dd HH:mm");
		String endTime = DateUtils.getStrTime(taskDetailBean.getEndTime(), "MM/dd HH:mm");
		Date endDate=new Date(taskDetailBean.getEndTime());
		int hour=DateUtil.getExpiredHour(DateUtil.dataToString(endDate));
		long diftime=0;
		if(taskDetailBean.getCompleteTime()==0){
			diftime=System.currentTimeMillis() / 1000;
		}else{
			diftime=taskDetailBean.getCompleteTime()/1000;
		}
		long dif = (diftime - taskDetailBean.getEndTime() / 1000) / (24 * 60 * 60);
		taskTimeTv.setText(startTime + " → " + endTime);
		if (dif > 0) {
			taskTimeIv.setEnabled(false);
			taskDayTv.setText(getString(R.string.chaoqi_day,dif));
			taskDayTv.setTextColor(Color.parseColor("#fd2e1f"));
		} else {
			taskTimeIv.setEnabled(true);
			if(hour>0&hour<=12){
				taskDayTv.setText(getString(R.string.shengyu_bantian_day));
			}else if(hour>12&hour<=24){
				taskDayTv.setText(getString(R.string.shengyu_day,1));
			}else{
				taskDayTv.setText(getString(R.string.shengyu_day,Math.abs(dif)));
			}
			taskDayTv.setTextColor(Color.parseColor("#1ea839"));
		}
		// 是否关注
		flagCb.getDrawable().setLevel("0".equals(taskDetailBean.getIsOrder()) ? 0 : 1);
	}
	
	private void changeProgressState(){
		final String seek_bar_state_1=getString(R.string.seek_bar_state_1);
		final String seek_bar_state_2=getString(R.string.seek_bar_state_2);
		final String seek_bar_state_3=getString(R.string.seek_bar_state_3);
		final String seek_bar_state_4=getString(R.string.seek_bar_state_4);
		final String seek_bar_state_5=getString(R.string.seek_bar_state_5);
		int progress=acceptanceBar.getProgress();
		if(progress==0){
			acceptanceBarStateTv.setText(seek_bar_state_1);
		}else if(1<progress&&progress<40){
			acceptanceBarStateTv.setText(seek_bar_state_2);
		}else if(41<progress&&progress<70){
			acceptanceBarStateTv.setText(seek_bar_state_3);
		}else if(71<progress&&progress<99){
			acceptanceBarStateTv.setText(seek_bar_state_4);
		}else if(progress==100){
			acceptanceBarStateTv.setText(seek_bar_state_5);
		}
	}

	private void showSeekBarDoneHint(){
		if(successAm!=totalAm){
			new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
			.setIcon(null)
			.setCancelable(true)
			.setMessage(R.string.seek_done_100_hint)
			.setTitle(R.string.prompt).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).create().show();
		}
	}
	
	/**任务详情最新一条反馈UI设置*/
	private void initTaskProcessUI(NewTaskProcessBean newTaskProcessBean){
		if(newTaskProcessBean!=null){
			taskProcessCustomList.removeAllViews();
			if(newTaskProcessBean.getCreater().equals(getContext().getKzxTokenBean().getMemberId())){
				View rightView = LayoutInflater.from(getActivity()).inflate(R.layout.faceback_list_item_right_text, null);
				LinearLayout right_text=(LinearLayout) rightView.findViewById(R.id.right_text);
				CircleImageView ivRightIcon = (CircleImageView) rightView.findViewById(R.id.iv_icon);
				TextView btnRightText = (TextView) rightView.findViewById(R.id.btn_right_text);
				TextView type_right_text = (TextView) rightView.findViewById(R.id.type_right_text);
				btnRightText.setMaxWidth(getResources().getDisplayMetrics().widthPixels - getResources().getDisplayMetrics().widthPixels / 4);
				TextView timeRightTv = (TextView) rightView.findViewById(R.id.timeRightTv);
				LinearLayout right_attachement = (LinearLayout) rightView.findViewById(R.id.right_attachement);
				btnRightText.setVisibility(!StringUtils.isEmpty(newTaskProcessBean.getContent()) ? View.VISIBLE : View.GONE);
				btnRightText.setText(Html.fromHtml(newTaskProcessBean.getContent()));
//				timeRightTv.setText(DateUtil.convertDateToShowStr(new Date(newTaskProcessBean.getCreateTime())));
				timeRightTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(newTaskProcessBean.getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
				if (!StringUtils.isEmpty(newTaskProcessBean.getCreaterIcon())) {
//					Picasso.with(getActivity()) //
//							.load(newTaskProcessBean.getCreaterIcon()) //
//							.placeholder(R.drawable.ic_avatar_120) //
//							.error(R.drawable.ic_avatar_120) //
//							.into(ivRightIcon);
					ImageLoader.getInstance().displayImage(newTaskProcessBean.getCreaterIcon(), ivRightIcon, options);
				}
				right_text.setOnClickListener(taskProcessClick);
				// 反馈类型
				type_right_text.setVisibility(View.VISIBLE);
				validateTaskProcessType(type_right_text,newTaskProcessBean.getType(),1);
				// 附件
				if (!StringUtils.isNullOrEmpty(newTaskProcessBean.getAttachement())) {
					Gson gson = new Gson();
					Type tt = new TypeToken<List<AttachementBean>>() { }.getType();
					List<AttachementBean> attachementList = gson.fromJson(newTaskProcessBean.getAttachement(), tt);
					right_attachement.setVisibility(!StringUtils.isNullOrEmpty(attachementList) ? View.VISIBLE : View.GONE);
					Drawable fileImage = getActivity().getResources().getDrawable(R.drawable.kzx_ic_task_attachment);
					fileImage.setBounds(0, 0, fileImage.getMinimumWidth(), fileImage.getMinimumHeight());
					right_attachement.removeAllViews();
					for (final AttachementBean attachementBean : attachementList) {
						View attachementView = LayoutInflater.from(getActivity()).inflate(R.layout.faceback_attachement_lv_item, null);
						ImageView attachementBg = (ImageView) attachementView.findViewById(R.id.attachementBg);
						TextView attachementTv = (TextView) attachementView.findViewById(R.id.attachementTv);
						TextView sizeTv = (TextView) attachementView.findViewById(R.id.sizeTv);
						if(!StringUtils.isEmpty(attachementBean.getType())){
							setFileIcon(attachementBg,attachementBean);
						}else{
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
						}
						attachementTv.setText(attachementBean.getName());
						sizeTv.setText(ArithUtils.getKb(attachementBean.getSize()));
						attachementView.findViewById(R.id.attachementBtn).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DialogFragment myFragment = KzxAttachementDownloadDialogFragment.newInstance(attachementBean);
								myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
								myFragment.show(getFragmentManager(), TAG);
							}
						});
						right_attachement.addView(attachementView);
					}
				} else {
					right_attachement.setVisibility(View.GONE);
				}
				taskProcessCustomList.addView(rightView);
				taskProcessCustomList.setVisibility(View.VISIBLE);
			}else{
				View taskProcessView = LayoutInflater.from(getActivity()).inflate(R.layout.faceback_list_item_left_text, null);
				LinearLayout left_text=(LinearLayout) taskProcessView.findViewById(R.id.left_text);
				CircleImageView ivLeftIcon = (CircleImageView) taskProcessView.findViewById(R.id.iv_icon);
				TextView btnLeftText = (TextView) taskProcessView.findViewById(R.id.btn_left_text);
				btnLeftText.setMaxWidth(getResources().getDisplayMetrics().widthPixels - getResources().getDisplayMetrics().widthPixels / 4);
				TextView timeLeftTv = (TextView) taskProcessView.findViewById(R.id.timeLeftTv);
				TextView type_left_text = (TextView) taskProcessView.findViewById(R.id.type_left_text);
				LinearLayout left_attachement = (LinearLayout) taskProcessView.findViewById(R.id.left_attachement);
				btnLeftText.setVisibility(!StringUtils.isEmpty(newTaskProcessBean.getContent()) ? View.VISIBLE : View.GONE);
				btnLeftText.setText(Html.fromHtml(newTaskProcessBean.getContent()));
//				timeLeftTv.setText(DateUtil.convertDateToShowStr(new Date(newTaskProcessBean.getCreateTime())));
				timeLeftTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(newTaskProcessBean.getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
				// 头像
				if (!StringUtils.isEmpty(newTaskProcessBean.getCreaterIcon())) {
//					Picasso.with(getActivity()) //
//							.load(newTaskProcessBean.getCreaterIcon()) //
//							.placeholder(R.drawable.ic_avatar_120) //
//							.error(R.drawable.ic_avatar_120) //
//							.into(ivLeftIcon);
					ImageLoader.getInstance().displayImage(newTaskProcessBean.getCreaterIcon(), ivLeftIcon, options);
				}
				left_text.setOnClickListener(taskProcessClick);
				// 反馈类型
				type_left_text.setVisibility(View.VISIBLE);
				type_left_text.setTextColor(Color.parseColor("#777777"));
				validateTaskProcessType(type_left_text,newTaskProcessBean.getType(),0);
				// 附件
				if (!StringUtils.isNullOrEmpty(newTaskProcessBean.getAttachement())) {
					Gson gson = new Gson();
					Type tt = new TypeToken<List<AttachementBean>>() { }.getType();
					List<AttachementBean> attachementList = gson.fromJson(newTaskProcessBean.getAttachement(), tt);
					left_attachement.setVisibility(!StringUtils.isNullOrEmpty(attachementList) ? View.VISIBLE : View.GONE);
					Drawable fileImage = getActivity().getResources().getDrawable(R.drawable.kzx_ic_task_attachment);
					fileImage.setBounds(0, 0, fileImage.getMinimumWidth(), fileImage.getMinimumHeight());
					left_attachement.removeAllViews();
					for (final AttachementBean attachementBean : attachementList) {
						View attachementView = LayoutInflater.from(getActivity()).inflate(R.layout.faceback_attachement_lv_item, null);
						ImageView attachementBg = (ImageView) attachementView.findViewById(R.id.attachementBg);
						TextView attachementTv = (TextView) attachementView.findViewById(R.id.attachementTv);
						TextView sizeTv = (TextView) attachementView.findViewById(R.id.sizeTv);
						if(!StringUtils.isEmpty(attachementBean.getType())){
							setFileIcon(attachementBg,attachementBean);
						}else{
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
						}
						attachementTv.setText(attachementBean.getName());
						sizeTv.setText(ArithUtils.getKb(attachementBean.getSize()));
						attachementView.findViewById(R.id.attachementBtn).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DialogFragment myFragment = KzxAttachementDownloadDialogFragment.newInstance(attachementBean);
								myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
								myFragment.show(getFragmentManager(), TAG);
							}
						});
						left_attachement.addView(attachementView);
					}
				} else {
					left_attachement.setVisibility(View.GONE);
				}
				taskProcessCustomList.addView(taskProcessView);
				taskProcessCustomList.setVisibility(View.VISIBLE);
			}
		}else{
			taskProcessCustomList.setVisibility(View.GONE);
		}
	}
	
	/**判断反馈类型**/
	private void validateTaskProcessType(TextView type_right_text,String type,int leftOrRight){
		if ("99".equals(type) || StringUtils.isEmpty(type) || "0".equals(type)) {
			type_right_text.setVisibility(View.GONE);
		} else if ("1".equals(type)) {
			zuaiDrawable.setBounds(0, 0, zuaiDrawable.getMinimumWidth(), zuaiDrawable.getMinimumHeight());
			type_right_text.setCompoundDrawables(zuaiDrawable, null, null, null);
			type_right_text.setText(getString(R.string.taskprocess_yudaowenti));
		} else if ("2".equals(type)) {
			yanqiDrawable.setBounds(0, 0, yanqiDrawable.getMinimumWidth(), yanqiDrawable.getMinimumHeight());
			yanqiDrawableWhite.setBounds(0, 0, yanqiDrawableWhite.getMinimumWidth(), yanqiDrawableWhite.getMinimumHeight());
			type_right_text.setCompoundDrawables(leftOrRight==0?yanqiDrawable:yanqiDrawableWhite, null, null, null);
			type_right_text.setText(getString(R.string.taskprocess_yanqi));
		} else if ("3".equals(type)) {
			shunxuDrawable.setBounds(0, 0, shunxuDrawable.getMinimumWidth(), shunxuDrawable.getMinimumHeight());
			shunxuDrawableWhite.setBounds(0, 0, shunxuDrawableWhite.getMinimumWidth(), shunxuDrawableWhite.getMinimumHeight());
			type_right_text.setCompoundDrawables(leftOrRight==0?shunxuDrawable:shunxuDrawableWhite, null, null, null);
			type_right_text.setText(getString(R.string.taskprocess_shunli));
		} else if ("4".equals(type)) {
			zhuajinDrawable.setBounds(0, 0, zhuajinDrawable.getMinimumWidth(), zhuajinDrawable.getMinimumHeight());
			zhuajinDrawableWhite.setBounds(0, 0, zhuajinDrawableWhite.getMinimumWidth(), zhuajinDrawableWhite.getMinimumHeight());
			type_right_text.setCompoundDrawables(leftOrRight==0?zhuajinDrawable:zhuajinDrawableWhite, null, null, null);
			type_right_text.setText(getString(R.string.taskprocess_zhuajin));
		} else if ("5".equals(type)) {
			jinduDrawable.setBounds(0, 0, jinduDrawable.getMinimumWidth(), jinduDrawable.getMinimumHeight());
			jinduDrawableWhite.setBounds(0, 0, jinduDrawableWhite.getMinimumWidth(), jinduDrawableWhite.getMinimumHeight());
			type_right_text.setCompoundDrawables(leftOrRight==0?jinduDrawable:jinduDrawableWhite, null, null, null);
			type_right_text.setText(getString(R.string.taskprocess_jindu));
		}
	}
	
	/**验收标准UI设置*/
	private void initAccptanceUI(final List<AcceptanceBean> acceptanceList,TaskDetailBean taskDetailBean){
		if (!StringUtils.isNullOrEmpty(acceptanceList)) {
			// 清空视图
			acceptanceCustomList.removeAllViews();
			totalAm=acceptanceList.size();
			successAm=0;
			// 循环添加验收标准视图
			for (final AcceptanceBean acceptanceBean : acceptanceList) {
				//构建动态验收标准视图
				View acceptanceView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_task_acceptance_lv_item_fragment, null);
				final TextView acceptanceTv = (TextView) acceptanceView.findViewById(R.id.acceptanceTv);
				final ImageView acceptanceBg=(ImageView)acceptanceView.findViewById(R.id.acceptanceBg);
//				CheckBox acceptanceSwitch=(CheckBox)acceptanceView.findViewById(R.id.acceptanceSwitch);
				//判断是否是执行人或者是发起人
				if(isTaskCreater()){
//					acceptanceBg.setVisibility(View.VISIBLE);
//					acceptanceSwitch.setVisibility(View.GONE);
					acceptanceBg.setEnabled(false);
				}else{
//					acceptanceBg.setVisibility(View.GONE);
//					acceptanceSwitch.setVisibility(View.VISIBLE);
					acceptanceBg.setEnabled(true);
				}
				if (!StringUtils.isEmpty(acceptanceBean.getComplete()) && "true".equals(acceptanceBean.getComplete())) {
//					acceptanceBg.setEnabled(true);
					acceptanceBg.getDrawable().setLevel(1);
				} else {
//					acceptanceSwitch.setChecked(false);
//					acceptanceBg.setEnabled(false);
					acceptanceBg.getDrawable().setLevel(0);
				}
				acceptanceTv.setText(acceptanceBean.getName());
				// 设置Tag管理
				acceptanceView.setTag(acceptanceBean.getName());
				acceptanceTv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showAcceptanceInfoDialog(getString(R.string.task_acceptance_hint),acceptanceBean.getName());
					}
				});
				// 添加到视图中
				acceptanceCustomList.addView(acceptanceView);
				if ("true".equals(acceptanceBean.getComplete())) {
					successAm++;
				}
				acceptanceBg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AcceptanceBean acceptance=null;
						if(acceptanceBg.getDrawable().getLevel()==0){
							acceptance=new AcceptanceBean(acceptanceBean.getName(),"true");
							acceptanceAmountTv.setText(getString(R.string.acceptance,successAm+1 + "/" + acceptanceList.size()));
							acceptanceBg.getDrawable().setLevel(1);
							successAm=successAm+1;
						}else{
							acceptance=new AcceptanceBean(acceptanceBean.getName(),"false");
							acceptanceAmountTv.setText(getString(R.string.acceptance,successAm-1 + "/" + acceptanceList.size()));
							acceptanceBg.getDrawable().setLevel(0);
							successAm=successAm-1;
						}
						addTaskProcess(null,acceptance);
					}
				});
			}
			acceptanceAmountTv.setVisibility(View.VISIBLE);
			acceptanceAmountTv.setText(getString(R.string.acceptance,successAm + "/" + acceptanceList.size()));
		}else{
			acceptanceAmountTv.setVisibility(View.GONE);
		}
	}
	
	/**查看详情验收**/
	private void showAcceptanceInfoDialog(String title,String message){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
		.setIcon(null)
		.setMessage(message)
		.setTitle(title).setPositiveButton(R.string.alert_dialog_close, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).create().show();
	}
	
	/**附件UI设置*/
	private void initAttachementUI(final List<AttachementBean> attachementList){
		if (!StringUtils.isNullOrEmpty(attachementList)) {
			attachmentTv.setText(attachementList.size() + "");
			// 清空视图
			attachmentCustomList.removeAllViews();
			// 循环添加附件视图
			for (final AttachementBean attachementBean : attachementList) {
				View attachementView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_task_attachment_lv_item_fragment, null);
				ImageView attachementBg = (ImageView) attachementView.findViewById(R.id.attachementBg);
				TextView attachmentTv = (TextView) attachementView.findViewById(R.id.attachmentTv);
				TextView attachmentSizeTv = (TextView) attachementView.findViewById(R.id.attachmentSizeTv);
				if(!StringUtils.isEmpty(attachementBean.getType())){
					setFileIcon(attachementBg, attachementBean);
				}else{
					attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
				}
				attachmentTv.setText(attachementBean.getName());
				attachmentSizeTv.setText(ArithUtils.getKb(attachementBean.getSize()));
				// 设置Tag管理
				attachementView.setTag(attachementBean.getName());
				// 添加到视图中
				attachmentCustomList.addView(attachementView);
				attachementView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						DialogFragment myFragment = KzxAttachementDownloadDialogFragment.newInstance(attachementBean);
						myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
						myFragment.show(getFragmentManager(), TAG);
					}
				});
			}
		} else {
			attachmentTv.setText("0");
		}
	}
	
	/**关联目标UI设置*/
	private void initTargetUI(final TargetBean targetBean){
		if (targetBean != null) {
			relateTargetDescEmptyTv.setVisibility(View.GONE);
			relateTargetDescTv.setVisibility(View.VISIBLE);
			relateTargetDescTv.setText(targetBean.getTitle());
			relateTargetDescTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showAcceptanceInfoDialog(getString(R.string.task_target_hint),targetBean.getTitle());
				}
			});
		}else{
			relateTargetDescEmptyTv.setVisibility(View.VISIBLE);
		}
	}
	
	/**关联任务UI设置*/
	private void initTaskUI(List<TaskDetailBean> taskDetailList){
		if (!StringUtils.isNullOrEmpty(taskDetailList)) {
			relateTaskEmptyTv.setVisibility(View.GONE);
			// 清空视图
			taskCustomList.removeAllViews();
			// 循环添加附件视图
			for (TaskDetailBean taskDetail : taskDetailList) {
				View taskDetailView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_task_related_lv_item_fragment, null);
				TextView taskRelatedTitleTv = (TextView) taskDetailView.findViewById(R.id.taskRelatedTitleTv);
				TextView taskRelatedContentTv = (TextView) taskDetailView.findViewById(R.id.taskRelatedContentTv);
				TextView taskRelatedScheduleTv = (TextView) taskDetailView.findViewById(R.id.taskRelatedScheduleTv);
				taskRelatedTitleTv.setText(taskDetail.getTitle());
				taskRelatedContentTv.setText(taskDetail.getExecutorName());
				taskRelatedScheduleTv.setText(taskDetail.getSchedule() + "%");
				// 设置Tag管理
				taskDetailView.setTag(taskDetail.getId());
				// 添加到视图中
				taskCustomList.addView(taskDetailView);
			}
		}else{
			taskCustomList.setVisibility(View.GONE);
			relateTaskEmptyTv.setVisibility(View.VISIBLE);
		}
		resetMeasureSpec(taskCustomList,false);
	}
	
	//重新计算高度防止视图溢出
	private void resetMeasureSpec(View specView,boolean isDelete){
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		specView.measure(widthSpec, heightSpec);
		ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
		if(isDelete){
			layoutParams.height = layoutParams.height-specView.getMeasuredHeight();
		}else{
			layoutParams.height = layoutParams.height+specView.getMeasuredHeight();
		}
		mLinearLayout.setLayoutParams(layoutParams);
	}
	
	/**初始化滑动模块最小值*/ 
	private void initSeekBarUI(final TaskDetailBean taskBean){
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
					showSeekBarDoneHint();
				}
				if(contentCt.getCompoundDrawables()[0]==null&&StringUtils.isEmpty(contentCt.getText().toString())){
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
	}
	
	/**拨打电话事件*/
	OnClickListener callClick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(!StringUtils.isEmpty(taskDetailBean.getExecutorPhone())){
			    Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+taskDetailBean.getExecutorPhone()));
			    getActivity().startActivity(intent);
			}
		}
	};
	
	/**更多(编辑)事件*/
	OnClickListener  modifyTaskClick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			closePopupWindow();
			Intent mIntent=new Intent(getActivity(),ContentFragmentActivity.class);
			mIntent.putExtra("action", "modify_task");
			mIntent.putExtra("taskBean", taskDetailBean);
			mIntent.putExtra("detailStr", detailStr);
			mIntent.putExtra("actionStr", "modify");
			getActivity().startActivity(mIntent);
		}
	};
	
	/**更多(转发)事件*/
	OnClickListener  forwardedTaskClick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			closePopupWindow();
			Intent mIntent=new Intent(getActivity(),ContentFragmentActivity.class);
			mIntent.putExtra("action", "modify_task");
			mIntent.putExtra("taskBean", taskDetailBean);
			mIntent.putExtra("detailStr", detailStr);
			mIntent.putExtra("actionStr", "repeat");
			getActivity().startActivity(mIntent);
		}
	};
	
	/**更多(取消)事件*/
	OnClickListener cancelTaskClick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			closePopupWindow();
			new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(R.string.alert_dialog_cancel_task).setTitle(R.string.prompt).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					postCancelTaskProcess();
					dialog.dismiss();
				}
			}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).create().show();
		}
	};
	
	/**更多(取消)事件*/
	OnClickListener taskProcessClick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent mIntent=new Intent(getActivity(),ContentFragmentActivity.class);
			mIntent.putExtra("action", "task_process");
			mIntent.putExtra("taskBean", taskDetailBean);
			startActivity(mIntent);
		}
	};
	
	/**发送反馈事件*/
	OnClickListener sendTaskProcessClick=new OnClickListener(){
		@Override
		public void onClick(View v) {
			KeyBoardUtils.closeKeybord(contentCt, getActivity());
			addTaskProcess(null, null);
		}
	};
	/**添加新反馈信息*/
	private void addTaskProcess(File mFile,AcceptanceBean acceptance){
		String typeState = null,scheduleStr = null,passtype=null,endIsGood=null,clientEndIsGood=null;
		
		if(acceptance!=null){
			postAddTaskProcess(typeState,scheduleStr,passtype,endIsGood,clientEndIsGood,acceptance,mFile);
			return;
		}
		
		final String memberId=getContext().getKzxTokenBean().getEncryptMemberId();
		final JSONArray clientIds=JSON.parseArray(getContext().getKzxTokenBean().getClientIds());
		
		// 判断任务是执行人还是发起人
		if(TASK_STATE_1.equals(taskDetailBean.getState())){
			if(memberId.equals(taskDetailBean.getExecutor())){
				if (zuaiBtn.isChecked()) {
					typeState = TASK_STATE_1;// 遇到阻碍
				} else if (yanqiBtn.isChecked()) {
					typeState = TASK_STATE_2;// 需要延期
				} else if (orderBtn.isChecked()) {
					typeState = TASK_STATE_3;// 顺序进行
				}
				scheduleStr = String.valueOf(seekBar.getProgress());
			}else if(memberId.equals(taskDetailBean.getSponsor())){
				if (fireBtn.isChecked()) {
					typeState = TASK_STATE_4;// 抓紧处理
				} else if (progressBtn.isChecked()) {
					typeState = TASK_STATE_5;// 进度如何
				}
			}
		}else if(TASK_STATE_2.equals(taskDetailBean.getState())&&memberId.equals(taskDetailBean.getSponsor())){
			if (acceptanceBtn.isChecked()) {
				passtype = TASK_STATE_1;// 验收通过 
			} else if (reworkBtn.isChecked()) {
				passtype = TASK_STATE_0;// 需要返工
			}
		}else if(TASK_STATE_3.equals(taskDetailBean.getState())&&memberId.equals(taskDetailBean.getSponsor())){
			if (goodBtn.isChecked()) {
				endIsGood = TASK_STATE_1;// 好评
			} else if (badBtn.isChecked()) {
				endIsGood = TASK_STATE_2;// 差评
			}
		}else if((TASK_STATE_3.equals(taskDetailBean.getState())||TASK_STATE_4.equals(taskDetailBean.getState()))&&!StringUtils.isEmpty(taskDetailBean.getRelateClient())&&taskDetailBean.getClientEndIsGood()==0){
			boolean isCliented=false;
			for (Object clientId : clientIds) {
				if(taskDetailBean.getRelateClient().equals(clientId.toString())){
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
		//验收标准
		List<AcceptanceBean> acceptanceDataList=new ArrayList<AcceptanceBean>();
		int acceptanceIndex=acceptanceCustomList.getChildCount();
		for (int i = 0; i < acceptanceIndex; i++) {
			View acceptanceView=acceptanceCustomList.getChildAt(i);
			TextView acceptanceTv=(TextView) acceptanceView.findViewById(R.id.acceptanceTv);
//			CheckBox acceptanceSwitch=(CheckBox) acceptanceView.findViewById(R.id.acceptanceSwitch);
		    ImageView acceptanceBg=(ImageView)acceptanceView.findViewById(R.id.acceptanceBg);
			acceptanceDataList.add(new AcceptanceBean(acceptanceTv.getText().toString(), (acceptanceBg.getDrawable().getLevel()==1?true:false)+""));
		}
		String contentStr=contentCt.getText().toString();
		if(StringUtils.isEmpty(contentStr)&&mFile==null
				&&contentCt.getCompoundDrawables()[0]==null&&
				seekBarLayout.getVisibility()==View.GONE){
				MsgTools.toast(getActivity(), getString(R.string.input_taskprocess_hint), ResourceMap.LENGTH_SHORT);
				return;
		}else if(seekBarLayout.getVisibility()==View.VISIBLE&&mFile==null&&contentCt.getCompoundDrawables()[0]==null&&StringUtils.isEmpty(contentStr)){
			if(seekBar.getProgress()==Integer.valueOf(taskDetailBean.getSchedule())){
				MsgTools.toast(getActivity(), getString(R.string.input_taskprocess_hint), ResourceMap.LENGTH_SHORT);
				return;
			}
		}
		postAddTaskProcess(typeState,scheduleStr,passtype,endIsGood,clientEndIsGood,acceptance,mFile);
	}
	
	/**是否执行人*/
	private boolean isTaskCreater(){
		return taskDetailBean.isSelf()&&!taskDetailBean.getSponsor().equals(taskDetailBean.getExecutor());
	}
	
	/**关闭PopupWindow*/
	private void closePopupWindow(){
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();// 关闭
		}
	}

	// 关注
	private void postAttention(String path) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("taskId", taskId);// 任务详情ID
		asyncHttpClient.post(getActivity(), path, rp, attentionResponseHandler);
	}
	
	//关注回调
	AsyncHttpResponseHandler attentionResponseHandler=new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				if (success) {
					// 需要通过文本内容来标记控件
					if (message.indexOf(getString(R.string.taskprocess_flag)) != -1) {
						flagCb.getDrawable().setLevel(1);
					} else if (message.indexOf(getString(R.string.taskprocess_cancel)) != -1) {
						flagCb.getDrawable().setLevel(0);
					}
					ApplicationController.setTaskIdsBean(new TaskIdsBean(taskId, taskDetailBean.getSchedule(),taskDetailBean.getState(),"isOrder"));
				}
				MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
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

	private void expand() {
		// set Visible
		mLinearLayout.setVisibility(View.VISIBLE);
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		mAnimator.start();
	}

	private void collapse() {
		int finalHeight = mLinearLayout.getHeight();
		ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
		mAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animator) {
				// Height=0, but it set visibility to GONE
				mLinearLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		mAnimator.start();
	}

	private ValueAnimator slideAnimator(int start, int end) {
		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// Update Height
				int value = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
				layoutParams.height = value;
				mLinearLayout.setLayoutParams(layoutParams);
			}
		});
		return animator;
	}

	// 添加到反馈
	private void postAddTaskProcess(String type,String schedule,String passtype,String endIsGood,String clientEndIsGood,AcceptanceBean acceptanceBean,final File files) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("taskId", taskId);
		if(!StringUtils.isEmpty(type)){
			rp.put("type", type);//状态
		}
		if(!StringUtils.isEmpty(schedule)){
			rp.put("schedule", schedule);//进度
		}
		if(acceptanceBean!=null){
			rp.put("acceptStandard", new Gson().toJson(acceptanceBean));
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
		String contentStr=contentCt.getText().toString();
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
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String content = new String(responseBody);
				try {
					boolean success = new JSONObject(content).optBoolean("success", false);
					String message = new JSONObject(content).optString("message");
					String data = new JSONObject(content).optString("data");
					String taskState = new JSONObject(content).optString("taskState");
					if (success) {
						// 存放到临时变量中(状态改变的时候重新刷新)
						acceptanceBar.setProgress(seekBar.getProgress());
						acceptanceBarTv.setText(seekBar.getProgress()+"%");
						changeProgressState();
						taskDetailBean.setSchedule(seekBar.getProgress()+"");//重新设置进度情况
						ApplicationController.setTaskIdsBean(new TaskIdsBean(taskId, taskDetailBean.getSchedule(),taskState,null));
						if(!taskState.equals(taskDetailBean.getState())){
							reloadDataByDialog(message,taskState);
						}else{
							// 创建一条即时消息
							TaskProcessBean taskProcessBean = new TaskProcessBean();
							taskProcessBean.setAttachement(new JSONObject(data).optString("attachement",""));
							taskProcessBean.setCreater(new JSONObject(data).optString("creater",""));
							taskProcessBean.setCreaterIcon(new JSONObject(data).optString("createrIcon",""));
							taskProcessBean.setCreaterName(new JSONObject(data).optString("createrName",""));
							taskProcessBean.setContent(new JSONObject(data).optString("content",""));
							taskProcessBean.setTaskId(taskId);
							taskProcessBean.setCreateTime(new JSONObject(data).optLong("createTime", System.currentTimeMillis()));
							taskProcessBean.setType(new JSONObject(data).optString("type",""));
							taskProcessBean.setViewType(KzxTaskProcessAdapter.VALUE_RIGHT_TEXT);
							addNewTaskProcess(taskProcessBean);
						}
					} else {
						MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				onFailureToast(error);
			}
		});
	}
	
	// 取消任务
	private void postCancelTaskProcess(){
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("taskId", taskId);
		asyncHttpClient.post(getActivity(), Constants.cancelTaskAPI, rp, cancelResponseHandler);
	}
	
	// 取消任务回调
	private AsyncHttpResponseHandler cancelResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			showDialog();
		}
		public void onFinish() {
			dialog.dismiss();
		};
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				if (success) {
					ApplicationController.setTaskIdsBean(new TaskIdsBean(taskId,taskDetailBean.getSchedule(),"5",null));
					// 关闭当前页
					getActivity().finish();
				} 
				MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};
	
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
	
	
	//添加新视图到ScrollView到底部
	private void addNewTaskProcess(TaskProcessBean taskProcessBean){
		if(taskProcessCustomList.getVisibility()==View.GONE){
			taskProcessCustomList.setVisibility(View.VISIBLE);
		}
		View rightView = LayoutInflater.from(getActivity()).inflate(R.layout.faceback_list_item_right_text, null);
		LinearLayout right_text=(LinearLayout) rightView.findViewById(R.id.right_text);
		CircleImageView ivRightIcon = (CircleImageView) rightView.findViewById(R.id.iv_icon);
		TextView btnRightText = (TextView) rightView.findViewById(R.id.btn_right_text);
		TextView type_right_text = (TextView) rightView.findViewById(R.id.type_right_text);
		btnRightText.setMaxWidth(getResources().getDisplayMetrics().widthPixels - getResources().getDisplayMetrics().widthPixels / 4);
		TextView timeRightTv = (TextView) rightView.findViewById(R.id.timeRightTv);
		LinearLayout right_attachement = (LinearLayout) rightView.findViewById(R.id.right_attachement);
		btnRightText.setVisibility(!StringUtils.isEmpty(taskProcessBean.getContent()) ? View.VISIBLE : View.GONE);
		btnRightText.setText(Html.fromHtml(taskProcessBean.getContent()));
//		timeRightTv.setText(DateUtil.convertDateToShowStr(new Date(taskProcessBean.getCreateTime())));
		timeRightTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(taskProcessBean.getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
		if (!StringUtils.isEmpty(taskProcessBean.getCreaterIcon())) {
//			Picasso.with(getActivity()) //
//					.load(taskProcessBean.getCreaterIcon()) //
//					.placeholder(R.drawable.ic_avatar_120) //
//					.error(R.drawable.ic_avatar_120) //
//					.into(ivRightIcon);
			ImageLoader.getInstance().displayImage(taskProcessBean.getCreaterIcon(), ivRightIcon, options);
		}
		right_text.setOnClickListener(taskProcessClick);
		// 反馈类型
		type_right_text.setVisibility(View.VISIBLE);
		validateTaskProcessType(type_right_text,taskProcessBean.getType(),1);
		// 附件
		if (!StringUtils.isNullOrEmpty(taskProcessBean.getAttachement())) {
			Gson gson = new Gson();
			Type tt = new TypeToken<List<AttachementBean>>() { }.getType();
			List<AttachementBean> attachementList = gson.fromJson(taskProcessBean.getAttachement(), tt);
			right_attachement.setVisibility(!StringUtils.isNullOrEmpty(attachementList) ? View.VISIBLE : View.GONE);
			Drawable fileImage = getActivity().getResources().getDrawable(R.drawable.kzx_ic_task_attachment);
			fileImage.setBounds(0, 0, fileImage.getMinimumWidth(), fileImage.getMinimumHeight());
			right_attachement.removeAllViews();
			for (final AttachementBean attachementBean : attachementList) {
				View attachementView = LayoutInflater.from(getActivity()).inflate(R.layout.faceback_attachement_lv_item, null);
				ImageView attachementBg = (ImageView) attachementView.findViewById(R.id.attachementBg);
				TextView attachementTv = (TextView) attachementView.findViewById(R.id.attachementTv);
				TextView sizeTv = (TextView) attachementView.findViewById(R.id.sizeTv);
				if(!StringUtils.isEmpty(attachementBean.getType())){
					setFileIcon(attachementBg,attachementBean);
				}else{
					attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
				}
				attachementTv.setText(attachementBean.getName());
				sizeTv.setText(ArithUtils.getKb(attachementBean.getSize()));
				attachementView.findViewById(R.id.attachementBtn).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						DialogFragment myFragment = KzxAttachementDownloadDialogFragment.newInstance(attachementBean);
						myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
						myFragment.show(getFragmentManager(), TAG);
					}
				});
				right_attachement.addView(attachementView);
			}
		} else {
			right_attachement.setVisibility(View.GONE);
		}
		//添加新视图
		taskProcessCustomList.addView(rightView);
		//滚动到底部
		new Handler().post(mScrollToBottom);
	}
	
	/**设置附件图标**/
	private void setFileIcon(ImageView attachementBg,AttachementBean attachementBean){
		if (attachementBean.getType().indexOf("ppt") != -1) {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_ppt);
		} else if (attachementBean.getType().indexOf("doc") != -1) {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_word);
		} else if (attachementBean.getType().indexOf("xls") != -1) {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_excel);
		} else if (attachementBean.getType().indexOf("rar") != -1) {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_rar);
		} else if (attachementBean.getType().indexOf("pdf") != -1) {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_pdf);
		} else if (StringUtils.isPicture(attachementBean.getType())) {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_image);
		} else {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
		}
	}

	/**上传图片*/
	private void uploadImageBitmap(final boolean isOld,final File savedImages){
		DialogFragment myFragment = KzxImageViewDialogFragment.newInstance(isOld,savedImages.getAbsolutePath(),"TASK_DETAIL_RECEIVED_ACTION");
		myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
		myFragment.show(getFragmentManager(), TAG);
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
	
	/**滚动到底部*/
	private Runnable mScrollToBottom = new Runnable() {   
        @Override  
        public void run() {   
        	contentScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }   
    };     
    
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
			if(uri!=null){
				//判断图片大小是否有超过100K
				if(FileSizeUtil.getFileOrFilesSize(FileUtil.getFileByUri(getActivity(), uri), FileSizeUtil.SIZETYPE_KB)>Constants.Filesize){
					newFile=BitmapUtil.transImage(FileUtil.getFileByUri(getActivity(), uri), FileUtil.getFileFromUrl().getAbsolutePath(), 100);
					uploadImageBitmap(false,newFile);
				}else{
					newFile=new File(FileUtil.getFileByUri(getActivity(), uri));
					uploadImageBitmap(true,newFile);
				}
			}
		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (!FileUtil.hasSdcard()) {
				MsgTools.toast(getActivity(), getString(R.string.upload_error), ResourceMap.LENGTH_SHORT);
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
		//防止从通知点击进来返回的时候状态未改变
		if(ApplicationController.getTaskIdsBean()==null&&taskDetailBean!=null){
			ApplicationController.setTaskIdsBean(new TaskIdsBean(taskId, taskDetailBean.getSchedule(),taskDetailBean.getState(),null));
		}
		if (asyncHttpClient != null) {
			asyncHttpClient.cancelRequests(getActivity(), true);
		}
		if(mMessageReceiver!=null){
			getActivity().unregisterReceiver(mMessageReceiver);
		}
		if(!StringUtils.isNullOrEmpty(mDeleteFiles)){
			for (File mDeleteFile : mDeleteFiles) {
				mDeleteFile.delete();
			}
		}
	}

}
