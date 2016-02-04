package com.android.yijiang.kzx.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
import android.os.AsyncTask;
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
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.yijiang.kzx.widget.fileselector.FileSelectionActivity;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.FloatLabeledEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.MyListView;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
import com.android.yijiang.kzx.widget.StickyScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 任务编辑
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxTaskModifyFragment extends BaseFragment {

	private String TAG=KzxTaskModifyFragment.class.getName();
	
	private FloatLabeledEditText titleCt;// 标题
	private FloatLabeledEditText contentCt;// 内容
	private TextView executionTv;// 执行人→抄送人
	private TextView taskTimeTv;// 任务时间
	private ImageView taskTimeIv;//任务超期时间图标
	private TextView taskDayTv;// 任务超期时间
	private TextView clientNameTv;// 客户姓名
	private TextView clientPhoneTv;// 客户电话号码
	private StickyScrollView contentScrollView;// 滚动内容
	private AsyncHttpClient asyncHttpClient;
	
	private SwipeRefreshLayout swipeLayout;

	private LinearLayout acceptanceCustomList;// 验收标准
	private LinearLayout attachmentCustomList;// 附件
	private LinearLayout attachmentCustomList2;//附件2
	private LinearLayout taskCustomList;// 相关任务
	private ImageButton addNewAcceptanceBtn;
	private ImageButton cameraBtn;
	private ImageButton galleryBtn;

	private LinearLayout mLinearLayout;
	private LinearLayout mLinearLayoutHeader;
	private ImageView headerIv;
	private TextView headerTv;
	private ValueAnimator mAnimator;
	private TaskDetailBean taskBean;
	private String detailStr;
	private ModifyTaskTask modifyTaskTask;
	private Dialog dialog;
	private LinearLayout cycleDoneBtn;
	private LinearLayout copySelectedBtn;
	private TextView copySelectedTag;
	private LinearLayout clientBtn;
	private LinearLayout taskBtn;
	private TextView titleTv;
	private TextView taskSelectedTag;
	private LinearLayout targetBtn;// 关联目标
	private TextView targetTag;
	private MessageReceiver mMessageReceiver;
	
	private ClientBean clientBean;//手动输入客户
	private HashMap<Integer, LeaderBean> isLeaderHash;// 抄送人数据
	private HashMap<Integer, LeaderBean> isExecutionLeaderHash;// 抄送人数据
	private HashMap<Integer, TaskCanRelateBean> isTaskHash;// 相关任务数据
	private HashMap<Integer, TargetCanRelateBean> isTargetHash;// 相关任务数据
	private List<File> fileList = new ArrayList<File>();// 附件集合
	private List<File> deleteFileList = new ArrayList<File>();// 要删除的附件集合
	private List<AcceptanceBean> acceptanceList=new ArrayList<AcceptanceBean>();//验收标准
	private List<String> relateTasksList=new ArrayList<String>();//关联任务
	private List<String> relateTargetList=new ArrayList<String>();//关联目标
	private List<String> attachementStrList=new ArrayList<String>();//附件项
	private List<String> executionList=new ArrayList<String>();
	private List<String> copyToList=new ArrayList<String>();//抄送
	private HashMap<Integer, ClientBean> isClientBeanHash;// 客户数据
	private String beginTime;// 开始时间
	private String endTime;// 结束时间
	private int betweenTime=1;// 间隔
	private String relateClientName;//客户姓名
	private String relateClientPhone;//客户电话
	
	private TaskDetailBean taskDetailBean;
	private String actionStr;//默认是编辑方法
	
	// 头部View
	private ImageButton backBtn;
	private Button saveBtn;
	
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static String PHOTO_FILE_NAME = "";

	public static KzxTaskModifyFragment newInstance(TaskDetailBean taskBean,String detailStr,String actionStr) {
		KzxTaskModifyFragment fragment = new KzxTaskModifyFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("taskBean", taskBean);
		bundle.putString("detailStr", detailStr);
		bundle.putString("actionStr", actionStr);
		fragment.setArguments(bundle);
		return fragment;
	}
	

	// 注册广播
	public void registerMessageReceiver() {
		//加载数据
		modifyTaskTask = new ModifyTaskTask();
		modifyTaskTask.execute(detailStr);
		
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".ADD_TASK_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskBean = (TaskDetailBean) getArguments().getSerializable("taskBean");
		detailStr=getArguments().getString("detailStr","");
		actionStr=getArguments().getString("actionStr","modify");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_modify_task_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);	
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_red_dark, android.R.color.holo_orange_dark,
				android.R.color.holo_green_dark);
		addNewAcceptanceBtn = (ImageButton) view.findViewById(R.id.addNewAcceptanceBtn);
		cameraBtn= (ImageButton) view.findViewById(R.id.cameraBtn);
		galleryBtn= (ImageButton) view.findViewById(R.id.galleryBtn);
		copySelectedBtn=(LinearLayout)view.findViewById(R.id.copySelectedBtn);
		clientBtn=(LinearLayout)view.findViewById(R.id.clientBtn);
		copySelectedTag=(TextView)view.findViewById(R.id.copySelectedTag);
		taskSelectedTag=(TextView)view.findViewById(R.id.taskSelectedTag);
		titleTv=(TextView)view.findViewById(R.id.titleTv);
		taskBtn=(LinearLayout)view.findViewById(R.id.taskBtn);
		cycleDoneBtn=(LinearLayout)view.findViewById(R.id.cycleDoneBtn);
		targetBtn = (LinearLayout) view.findViewById(R.id.targetBtn);
		targetTag = (TextView) view.findViewById(R.id.targetTag);
		saveBtn=(Button)view.findViewById(R.id.saveBtn);//保存
		titleCt = (FloatLabeledEditText) view.findViewById(R.id.titleCt);// 标题
		contentCt = (FloatLabeledEditText) view.findViewById(R.id.contentCt);// 内容
		executionTv = (TextView) view.findViewById(R.id.executionTv);// 执行人→抄送人
		taskTimeTv = (TextView) view.findViewById(R.id.taskTimeTv);// 任务时间
		taskDayTv = (TextView) view.findViewById(R.id.taskDayTv);// 任务超期时间
		taskTimeIv=(ImageView)view.findViewById(R.id.taskTimeIv);
		clientNameTv = (TextView) view.findViewById(R.id.clientNameTv);// 客户姓名
		clientPhoneTv = (TextView) view.findViewById(R.id.clientPhoneTv);// 客户电话号码
		contentScrollView = (StickyScrollView) view.findViewById(R.id.contentScrollView);
		acceptanceCustomList = (LinearLayout) view.findViewById(R.id.acceptanceCustomList);
		attachmentCustomList = (LinearLayout) view.findViewById(R.id.attachmentCustomList);
		attachmentCustomList2 = (LinearLayout) view.findViewById(R.id.attachmentCustomList2);
		taskCustomList = (LinearLayout) view.findViewById(R.id.taskCustomList);
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
		// 新验收标准
		addNewAcceptanceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAcceptanceDialog(null);
			}
		});
		cameraBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 拍照
				if(attachmentCustomList.getChildCount()+attachmentCustomList2.getChildCount()<3){
					camera(v);
				}else{
					MsgTools.toast(getActivity(), "附件超过3件", Toast.LENGTH_SHORT);
				}
			}
		});
		galleryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 图库
				if(attachmentCustomList.getChildCount()+attachmentCustomList2.getChildCount()<3){
					gallery(v);
				}else{
					MsgTools.toast(getActivity(), "附件超过3件", Toast.LENGTH_SHORT);
				}
			}
		});
		//选择执行人
		executionTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isExecutionLeaderHash);
				mIntent.putExtra("action", "execution_selected");
				if("modify".equals(actionStr)){
					mIntent.putExtra("typeStr", "single-select");
				}
				startActivity(mIntent);
			}
		});
		//选择抄送人
		copySelectedBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isLeaderHash);
				mIntent.putExtra("action", "copy_selected");
				startActivity(mIntent);
			}
		});
		//关联任务
		taskBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isTaskHash);
				mIntent.putExtra("action", "task_selected");
				startActivity(mIntent);
			}
		});
		//关联目标
		targetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isTargetHash);
				mIntent.putExtra("action", "target_selected");
				startActivity(mIntent);
			}
		});
		//下拉刷新监听
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				modifyTaskTask = new ModifyTaskTask();
				modifyTaskTask.execute(detailStr);
			}
		});
		//客户
		clientBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isClientBeanHash);
				mIntent.putExtra("action", "client_selected");
				startActivity(mIntent);
			}
		});
		// 头部事件
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)). 
			     hideSoftInputFromWindow(titleCt.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				getActivity().finish();
			}
		});
		//保存事件
		saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String titleStr=titleCt.getText().toString();
				String contentStr=contentCt.getText().toString();
				if (StringUtils.isEmpty(titleStr)&&StringUtils.isEmpty(contentStr)) {
					MsgTools.toast(getActivity(), getResources().getString(ResourceMap.getString_task_title_hint()), ResourceMap.LENGTH_SHORT);
					return;
				}
				if (StringUtils.isNullOrEmpty(executionList)) {
					MsgTools.toast(getActivity(), getResources().getString(ResourceMap.getString_task_execution_hint()), ResourceMap.LENGTH_SHORT);
					return;
				}
				if (StringUtils.isEmpty(beginTime) && StringUtils.isEmpty(endTime)) {
					MsgTools.toast(getActivity(), getResources().getString(ResourceMap.getString_task_cycle_hint()), ResourceMap.LENGTH_SHORT);
					return;
				}
				RequestParams rp = new RequestParams();
				if("modify".equals(actionStr)){
					rp.put("id",taskBean.getId());//ID
				}
				rp.put("title", !StringUtils.isEmpty(titleStr)?titleStr:contentStr.substring(0, contentStr.length()>10?10:contentStr.length()));// 标题
				rp.put("content", contentStr);// 内容
				if(!StringUtils.isNullOrEmpty(executionList)){
					rp.put("executor", new Gson().toJson(executionList));//执行人
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					rp.put("startTime", !StringUtils.isEmpty(beginTime)?format.parse(beginTime).getTime():taskDetailBean.getStartTime());// 开始时间
					rp.put("endTime", !StringUtils.isEmpty(endTime)?format.parse(endTime).getTime():taskDetailBean.getEndTime());// 结束时间
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(!StringUtils.isNullOrEmpty(acceptanceList)){
					if("modify".equals(actionStr)){
						rp.put("acceptStandard", new Gson().toJson(acceptanceList));//验收标准
					}else{
						List<String> acceptanceStrList=new ArrayList<String>();//关联任务
						for (AcceptanceBean acceptanceBean : acceptanceList) {
							acceptanceStrList.add(acceptanceBean.getName());
						}
						rp.put("acceptStandard", new Gson().toJson(acceptanceStrList));//验收标准
					}
				}
				if(!StringUtils.isNullOrEmpty(relateTasksList)){
					rp.put("relateTasks", new Gson().toJson(relateTasksList));//相关任务
				}
				if(!StringUtils.isNullOrEmpty(copyToList)){
					rp.put("copyto", new Gson().toJson(copyToList));//抄送人
				}
				if(!StringUtils.isEmpty(relateClientPhone)){
					rp.put("relateClientPhone", relateClientPhone);//客户电话
				}
				if(!StringUtils.isEmpty(relateClientName)){
					rp.put("relateClientName", relateClientName);//客户电话
				}
				if(!StringUtils.isNullOrEmpty(relateTargetList)){
					rp.put("relateTarget", relateTargetList.get(0));//关联目标
				}
				if(!StringUtils.isNullOrEmpty(attachementStrList)){
					rp.put("attachement", new Gson().toJson(attachementStrList));//附件集合
				}
				if(!StringUtils.isNullOrEmpty(fileList)){
					try {
						for (int i = 0; i < fileList.size(); i++) {
							File mFile=fileList.get(i);
							rp.put("Filedata"+i, mFile);//附件
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				postSaveEdit(rp);
			}
		});
		//完成周期
		cycleDoneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "cycle_done");
				startActivity(mIntent);
			}
		});
		titleTv.setText("repeat".equals(actionStr)?getString(R.string.task_zhuanfa_hint):getString(R.string.task_bianji_hint));
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerMessageReceiver();
		if(!"modify".equals(actionStr)){
			Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
			mIntent.putExtra("data", isExecutionLeaderHash);
			mIntent.putExtra("action", "execution_selected");
			startActivity(mIntent);
		}
	}
	
	class ModifyTaskTask extends AsyncTask<String, Integer,TaskDetailBean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			swipeLayout.setRefreshing(true);
			contentScrollView.setVisibility(View.GONE);
		}
		@Override
		protected TaskDetailBean doInBackground(String... params) {
			String data=params[0].toString();
			// 任务详情
			Message msg=uiHandler.obtainMessage();
			msg.obj=data;
			uiHandler.sendMessage(msg);
			return taskDetailBean;
		}
		@Override
		protected void onPostExecute(TaskDetailBean taskDetailBean) {
			super.onPostExecute(taskDetailBean);
			swipeLayout.setRefreshing(false);
			swipeLayout.setEnabled(false);
			contentScrollView.setVisibility(View.VISIBLE);
		}

	}
	
	Handler uiHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String data=msg.obj.toString();
			try {
				Gson gson = new Gson();
				String clientStr = new JSONObject(data).optString("client", "");// 关联客户
				String taskStr = new JSONObject(data).optString("task", "");// 任务详情
				String relateTasks = new JSONObject(data).optString("relateTasks", "");// 关联任务
				String copytoList = new JSONObject(data).optString("copytoList", "");// 抄送列表
				String target = new JSONObject(data).optString("target", "");// 关联目标
				// 关联客户
				ClientBean clientBean = gson.fromJson(clientStr, ClientBean.class);
				initClientUI(clientBean);
				// 任务详情
				taskDetailBean = JSON.parseObject(taskStr, TaskDetailBean.class);
				initTaskDetailUI(taskDetailBean);
				// 验收标准
				acceptanceList = gson.fromJson(taskDetailBean.getAcceptStandard(), new TypeToken<List<AcceptanceBean>>() {}.getType());
				initAcceptanceUI(acceptanceList);
				// 附件
				List<AttachementBean> attachementList = gson.fromJson(taskDetailBean.getAttachement(), new TypeToken<List<AttachementBean>>() {}.getType());
				initAttachementUI(attachementList);
				// 抄送
				List<CopyTaskBean> copyTaskList = gson.fromJson(copytoList, new TypeToken<List<CopyTaskBean>>() {}.getType());
				initCopyUI(copyTaskList);
				// 关联目标
				TargetBean targetBean = gson.fromJson(target, TargetBean.class);
				initTargetUI(targetBean);
				// 关联任务
				List<TaskDetailBean> taskDetailList = gson.fromJson(relateTasks, new TypeToken<List<TaskDetailBean>>() {}.getType());
				initTaskUI(taskDetailList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
	
	/**关联chaosong*/
	private void initCopyUI(List<CopyTaskBean> copyTaskList){
		if(!StringUtils.isNullOrEmpty(copyTaskList)){
			StringBuffer buffer=new StringBuffer();
			for (CopyTaskBean copyTaskBean : copyTaskList) {
				copyToList.add(copyTaskBean.getMemberId());
				buffer.append(copyTaskBean.getMemberName());
				buffer.append(";");
			}
			copySelectedTag.setText(buffer.toString());
		}
	}
	
	/**关联客户*/
	private void initClientUI(ClientBean clientBean){
		if (clientBean != null) {
			clientNameTv.setText("客户:" + clientBean.getName());
			clientPhoneTv.setText("电话:" + clientBean.getPhone());
			//初始化到保存项
			relateClientPhone=clientBean.getPhone();
			relateClientName=clientBean.getName();
		} else {
			clientNameTv.setText("客户: 无.");
			clientPhoneTv.setText("电话: 无.");
		}
	}
	
	/**任务详情*/
	private void initTaskDetailUI(TaskDetailBean taskDetailBean){
		beginTime=DateUtil.dataToString(new Date(taskDetailBean.getStartTime()));
		endTime=DateUtil.dataToString(new Date(taskDetailBean.getEndTime()));
		if("modify".equals(actionStr)){
			executionTv.setText(taskDetailBean.getSponsorName()+ " → "+taskDetailBean.getExecutorName());//执行人
			executionList.add(taskDetailBean.getExecutor());
		}else{
			if(!StringUtils.isEmpty(getContext().getKzxTokenBean().getMemberId())){
				executionTv.setText(getContext().getKzxTokenBean().getMemberName() + " → ");
			}else{
				executionTv.setText("执行人:   " + taskDetailBean.getExecutorName());
			}
		}
		titleCt.setText(taskDetailBean.getTitle());
		contentCt.setText(taskDetailBean.getContent());
		String startTime = DateUtils.getStrTime(taskDetailBean.getStartTime(), "MM/dd HH:mm");
		String endTime = DateUtils.getStrTime(taskDetailBean.getEndTime(), "MM/dd HH:mm");
		Date endDate=new Date(taskDetailBean.getEndTime());
		int hour=DateUtil.getExpiredHour(DateUtil.dataToString(endDate));
		long dif = (System.currentTimeMillis() / 1000 - taskDetailBean.getEndTime() / 1000) / (24 * 60 * 60);
//		taskTimeTv.setText(startTime + " → " + endTime);
//		taskDayTv.setVisibility(dif > 1 ? View.VISIBLE : View.GONE);
//		taskTimeIv.setEnabled(dif > 1 ? false:true);
//		taskDayTv.setText("超期:" + dif + "天");
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
	}
	
	/**验收标准*/
	private void initAcceptanceUI(final List<AcceptanceBean> acceptanceList){
		if (!StringUtils.isNullOrEmpty(acceptanceList)) {
			// 清空视图
			acceptanceCustomList.removeAllViews();
			// 循环添加验收标准视图
			for (final AcceptanceBean acceptanceBean : acceptanceList) {
				//构建动态验收标准视图
				final View acceptanceView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_new_acceptance_lv_item_fragment, null);
				final TextView acceptanceTv = (TextView) acceptanceView.findViewById(R.id.acceptanceTv);
				ImageButton clearBtn=(ImageButton)acceptanceView.findViewById(R.id.clearBtn);
				acceptanceTv.setText(acceptanceBean.getName());
				//删除当前验收标准
				clearBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						acceptanceCustomList.removeView((View)acceptanceCustomList.findViewWithTag(acceptanceBean.getName()));
						acceptanceList.remove(acceptanceBean);
						resetMeasureSpec(acceptanceView,true);
					}
				});
				//编辑当前验收标准
				acceptanceView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						acceptanceBean.setName(acceptanceTv.getText().toString());
						showAcceptanceDialog(acceptanceBean);
					}
				});
				// 设置Tag管理
				acceptanceView.setTag(acceptanceBean.getName());
				// 添加到视图中
				acceptanceCustomList.addView(acceptanceView);
				
				resetMeasureSpec(acceptanceView,false);
			}
		}
	}
	
	/**附件*/
	private void initAttachementUI(List<AttachementBean> attachementList){
		if (!StringUtils.isNullOrEmpty(attachementList)) {
			// 清空视图
			attachmentCustomList.removeAllViews();
			// 循环添加附件视图
			for (final AttachementBean attachementBean : attachementList) {
				final View attachementView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_new_acceptance_lv_item_fragment, null);
				TextView acceptanceTv = (TextView) attachementView.findViewById(R.id.acceptanceTv);
				ImageButton clearBtn=(ImageButton)attachementView.findViewById(R.id.clearBtn);
				acceptanceTv.setText(attachementBean.getName());
				//删除当前验收标准
				clearBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						attachmentCustomList.removeView((View)attachmentCustomList.findViewWithTag(attachementBean.getName()));
						attachementStrList.remove(attachementBean.getId());
						resetMeasureSpec(attachementView,true);
					}
				});
				// 设置Tag管理
				attachementView.setTag(attachementBean.getName());
				// 添加到视图中
				attachmentCustomList.addView(attachementView);
				//初始化到保存项 
				attachementStrList.add(attachementBean.getId());
			}
		}
	}
	
	/**关联目标*/
	private void initTargetUI(TargetBean targetBean){
		if (targetBean != null) {
			targetTag.setText(targetBean.getTitle());
			//初始化到保存项
			relateTargetList.add(targetBean.getId());
		}
	}
	
	
	/**关联任务*/
	private void initTaskUI(List<TaskDetailBean> taskDetailList){
		if (!StringUtils.isNullOrEmpty(taskDetailList)) {
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
				//初始化到保存项
				relateTasksList.add(taskDetail.getId());
			}
		}
	}
	
	/**回调广播*/
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".ADD_TASK_RECEIVED_ACTION").equals(intent.getAction())) {
				String action = intent.getStringExtra("action");
				// 抄送人
				if ("copy_selected".equals(action)) {
					String showName = null;// 取第一位姓名
					copyToList.clear();
					isLeaderHash = (HashMap<Integer, LeaderBean>) intent.getSerializableExtra("data");
					for (Map.Entry<Integer, LeaderBean> entry : isLeaderHash.entrySet()) {
						LeaderBean leaderBean = entry.getValue();
						showName = leaderBean.getName();
						copyToList.add(leaderBean.getId());
					}
					if(isLeaderHash.size()>1){
						copySelectedTag.setText(showName + "等" + isLeaderHash.size() + "人");
					}else{
						copySelectedTag.setText(showName);
					}
				} else if ("execution_selected".equals(action)) {
					// 执行人
					executionList.clear();
					StringBuffer buffer=new StringBuffer();
					isExecutionLeaderHash = (HashMap<Integer, LeaderBean>) intent.getSerializableExtra("data");
					for (Map.Entry<Integer, LeaderBean> entry : isExecutionLeaderHash.entrySet()) {
						LeaderBean leaderBean = entry.getValue();
						buffer.append(leaderBean.getName());
						buffer.append(";");
						executionList.add(leaderBean.getId());
					}
					if(!StringUtils.isEmpty(getContext().getKzxTokenBean().getMemberId())){
						executionTv.setText(getContext().getKzxTokenBean().getMemberName() + " → " + buffer.toString());
					}else{
						executionTv.setText("执行人: " + taskDetailBean.getExecutorName());
					}
				} else if ("task_selected".equals(action)) {
					// 相关任务
					relateTasksList.clear();
					isTaskHash = (HashMap<Integer, TaskCanRelateBean>) intent.getSerializableExtra("data");
					taskSelectedTag.setText(isTaskHash.size()+"个任务");
					for (Map.Entry<Integer, TaskCanRelateBean> entry : isTaskHash.entrySet()) {
						TaskCanRelateBean taskCanRelateBean = entry.getValue();
						relateTasksList.add(taskCanRelateBean.getId());
					}
					taskCustomList.removeAllViews();
					expand();
				} else if ("target_selected".equals(action)) {
					// 相关目标
					relateTargetList.clear();
					isTargetHash = (HashMap<Integer, TargetCanRelateBean>) intent.getSerializableExtra("data");
					for (Map.Entry<Integer, TargetCanRelateBean> entry : isTargetHash.entrySet()) {
						TargetCanRelateBean targetCanRelateBean = entry.getValue();
						relateTargetList.add(targetCanRelateBean.getId());
						targetTag.setText(targetCanRelateBean.getTitle());
						break;
					}
				} else if ("cycle_done".equals(action)) {
					// 完成周期(天)
					beginTime = intent.getStringExtra("beginDate")+" "+intent.getStringExtra("beginTime")+":00";
					endTime = intent.getStringExtra("endDate")+" "+intent.getStringExtra("endTime")+":00";
					betweenTime = intent.getIntExtra("betweenTime",1);
					taskTimeTv.setText(DateUtils.getStrTime(DateUtil.parse(intent.getStringExtra("beginDate")).getTime(), "MM/dd") + " → " + DateUtils.getStrTime(DateUtil.parse(intent.getStringExtra("endDate")).getTime(), "MM/dd"));
					taskDayTv.setVisibility(View.GONE);
					taskTimeIv.setEnabled(true);
				}else if ("client_selected".equals(action)) {
					// 相关目标
					isClientBeanHash = (HashMap<Integer, ClientBean>) intent.getSerializableExtra("data");
					for (Map.Entry<Integer, ClientBean> entry : isClientBeanHash.entrySet()) {
						ClientBean clientBean = entry.getValue();
						relateClientPhone=clientBean.getPhone();
						relateClientName=clientBean.getName();
						clientPhoneTv.setText("客户:"+relateClientPhone);
						clientNameTv.setText("电话:"+relateClientName);
					}
				}else if ("client_add".equals(action)) {
					// 相关目标
					clientBean = (ClientBean) intent.getSerializableExtra("data");
					relateClientPhone=clientBean.getPhone().replaceAll(" ", "").replaceAll("[+]86", "");
					relateClientName=clientBean.getName();
					clientPhoneTv.setText("客户:"+relateClientPhone);
					clientNameTv.setText("电话:"+relateClientName);
				}
			}
		}
	}
	
	/**保存编辑*/
	private void postSaveEdit(RequestParams rp) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), "modify".equals(actionStr)?Constants.editTaskAPI:Constants.addTaskAPI, rp, responseHandler);
	}
	
	/**保存编辑回调*/
	AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
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
		@Override
		public void onFinish() {
			super.onFinish();
			dialog.dismiss();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content=new String(responseBody);
			try {
				boolean success=new JSONObject(content).optBoolean("success",false);
				String message=new JSONObject(content).optString("message");
				if(!success){
					MsgTools.toast(getActivity(), message, 3000);
				}else{
					String messageStr=null;
					if("modify".equals(actionStr)){
						messageStr=getString(R.string.task_bianji_success_hint);
					}else{
						messageStr=getString(R.string.task_zhuanfa_success_hint);
					}
					new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
					.setIcon(null)
					.setMessage(!StringUtils.isEmpty(message)?message:messageStr)
					.setTitle(R.string.prompt)
					.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
							if("modify".equals(actionStr)){
								Intent mIntent=new Intent(getActivity().getPackageName()+".TASK_DETAIL_RECEIVED_ACTION");
								mIntent.putExtra("action", "reload");
								getActivity().sendBroadcast(mIntent);
							}
							getActivity().finish();
						}
					}).create().show();
				}
			} catch (Exception e) {
				MsgTools.toast(getActivity(), getString(R.string.request_error), 3000);
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};

	/**弹出验收标准Dialog*/
	private void showAcceptanceDialog(final AcceptanceBean acceptanceBean) {
		final View contentView = getActivity().getLayoutInflater().inflate(R.layout.kzx_add_acceptance_dialog_fragment, null);
		final EditText contentTv = (EditText) contentView.findViewById(R.id.contentTv);
		if(acceptanceBean!=null){
			contentTv.setText(acceptanceBean.getName());
		}
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setTitle(R.string.acceptance_title).setView(contentView).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				final String contentStr = contentTv.getText().toString();
				if(acceptanceBean!=null){
					View acceptanceTag=acceptanceCustomList.findViewWithTag(acceptanceBean.getName());
					TextView acceptanceTv=(TextView)acceptanceTag.findViewById(R.id.acceptanceTv);
					acceptanceTv.setText(contentStr);
					acceptanceList.remove(acceptanceBean);
					acceptanceList.add(new AcceptanceBean(contentStr, "false"));
				}else{
					if (!StringUtils.isEmpty(contentStr)) {
						final View acceptanceView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_new_acceptance_lv_item_fragment, null);
						final TextView acceptanceTv=(TextView)acceptanceView.findViewById(R.id.acceptanceTv);
						ImageButton clearBtn=(ImageButton)acceptanceView.findViewById(R.id.clearBtn);
						acceptanceTv.setText(contentStr);
						//删除当前验收标准
						clearBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								acceptanceCustomList.removeView((View)acceptanceCustomList.findViewWithTag(contentStr));
								resetMeasureSpec(acceptanceView,true);
							}
						});
						//编辑当前验收标准
						acceptanceView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								acceptanceBean.setName(acceptanceTv.getText().toString());
								showAcceptanceDialog(acceptanceBean);
							}
						});
						acceptanceList.add(new AcceptanceBean(acceptanceTv.getText().toString(),"false"));
						//设置Tag管理
						acceptanceView.setTag(contentStr);
						//添加到视图中
						acceptanceCustomList.addView(acceptanceView);
						
						resetMeasureSpec(acceptanceView,false);
					}
				}
				KeyBoardUtils.closeKeybord(contentTv, getActivity());
				dialog.dismiss();
			}
		}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				KeyBoardUtils.closeKeybord(contentTv, getActivity());
				dialog.dismiss();
			}
		}).create().show();
		titleCt.clearFocus();
		contentCt.clearFocus();
		KeyBoardUtils.openKeybord(contentTv, getActivity());
	}
	
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
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	
	/** 添加图片到视图中 */
	private void addNewPictureUi(final File currentFile,boolean isOld){
		final View attachmentView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_new_upload_lv_item_fragment, null);
		final SquaredImageView attachmentIv = (SquaredImageView) attachmentView.findViewById(R.id.acceptanceIv);
		final ImageButton clearBtn = (ImageButton) attachmentView.findViewById(R.id.clearBtn);
		attachmentIv.getLayoutParams().width=getResources().getDisplayMetrics().widthPixels/3;
		attachmentIv.getLayoutParams().height=attachmentIv.getLayoutParams().width;
		attachmentView.setTag(currentFile);
		clearBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fileList.remove(currentFile);
				currentFile.delete();
				attachmentCustomList2.removeView((View) attachmentCustomList2.findViewWithTag(currentFile));
				if(attachmentCustomList2.getChildCount()==0){
					resetMeasureSpec(attachmentView, true);
				}
			}
		});
		attachmentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fileList.remove(currentFile);
				currentFile.delete();
				attachmentCustomList2.removeView((View) attachmentCustomList2.findViewWithTag(currentFile));
				if(attachmentCustomList2.getChildCount()==0){
					resetMeasureSpec(attachmentView, true);
				}
			}
		});
		Picasso.with(getActivity()).load(currentFile)
		.placeholder(R.drawable.default_bg)
		.error(R.drawable.default_bg)
		.into(attachmentIv);
		/**记录是否是新照片*/
		if(!isOld){
			deleteFileList.add(currentFile);
		}
		fileList.add(currentFile);
		attachmentCustomList2.addView(attachmentView);
		/**不必重复添加高度*/
		if(attachmentCustomList2.getChildCount()>1){
			return;
		}
		resetMeasureSpec(attachmentView, false);
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
				addNewPictureUi(newFile,false);
			}else{
				newFile=new File(FileUtil.getFileByUri(getActivity(), uri));
				addNewPictureUi(newFile,true);
			}
		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (!FileUtil.hasSdcard()) {
				MsgTools.toast(getActivity(), getString(R.string.upload_error), Toast.LENGTH_SHORT);
				return;
			}	
			File tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
			File newFile=BitmapUtil.transImage(tempFile.getAbsolutePath(), FileUtil.getFileFromUrl().getAbsolutePath(), 100);
	    	addNewPictureUi(newFile,false);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (asyncHttpClient != null) {
			asyncHttpClient.cancelRequests(getActivity(), true);
		}
		if (modifyTaskTask != null && modifyTaskTask.getStatus() != AsyncTask.Status.FINISHED){
			modifyTaskTask.cancel(true);
		}
		if (mMessageReceiver != null) {
			getActivity().unregisterReceiver(mMessageReceiver);
		}
		if (!StringUtils.isNullOrEmpty(deleteFileList)) {
			for (int i = 0; i < deleteFileList.size(); i++) {
				File mFile = deleteFileList.get(i);
				mFile.delete();
			}
		}
	}

}
