package com.android.yijiang.kzx.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.yijiang.kzx.sdk.BitmapProcessor;

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxNewAcceptanceAdapter;
import com.android.yijiang.kzx.adapter.KzxNewAttachmentAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.ClientBean;
import com.android.yijiang.kzx.bean.Contacts;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.TargetCanRelateBean;
import com.android.yijiang.kzx.bean.TargetMemberBean;
import com.android.yijiang.kzx.bean.TaskCanRelateBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.FileSize;
import com.android.yijiang.kzx.sdk.FileSizeUtil;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.AppManager;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.LoginActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.FloatLabeledEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.MyListView;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
import com.android.yijiang.kzx.widget.StickyScrollView;
import com.android.yijiang.kzx.widget.fileselector.FileSelectionActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

import java.util.TimerTask;

/**
 * 添加任务
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxAddTaskFragment extends BaseFragment {

	private String TAG=KzxAddTaskFragment.class.getName();
	
	private LinearLayout cycleDoneBtn;// 完成周期
	private LinearLayout copySelectedBtn;// 抄送人
	private LinearLayout executionSelectedBtn;// 执行人
	private LinearLayout targetBtn;// 关联目标
	private LinearLayout taskBtn;// 关联任务
	private LinearLayout clientBtn;// 关联客户
	private TextView executionTag;
	private TextView copySelectedTag;
	private TextView taskSelectedTag;
	private TextView targetTag;
	private TextView cycleDoneTag;
	private TextView clientTag;
	private LinearLayout acceptanceCustomList;
	private LinearLayout attachmentCustomList;
	private ImageButton addNewAcceptanceBtn;

	private LinearLayout mLinearLayout;
	private LinearLayout mLinearLayoutHeader;
	private ImageView headerIv;
	private TextView headerTv;
	private ValueAnimator mAnimator;
	private Button saveBtn;
	private Dialog dialog;
	private ImageButton backBtn;
	private CheckBox isUrgency;// 是否紧急
	private ImageButton cameraBtn;
	private ImageButton galleryBtn;

	private FloatLabeledEditText titleCt;// 标题
	private FloatLabeledEditText contentCt;// 内容
	private AsyncHttpClient asyncHttpClient;
	private MessageReceiver mMessageReceiver;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private HashMap<String, LeaderBean> isLeaderHash;// 抄送人数据
	private HashMap<String, LeaderBean> isExecutionLeaderHash;// 抄送人数据
	private HashMap<String, TaskCanRelateBean> isTaskHash;// 相关任务数据
	private HashMap<String, TargetCanRelateBean> isTargetHash;// 相关任务数据
	private HashMap<String, ClientBean> isClientBeanHash;// 客户数据
	private List<File> fileList = new ArrayList<File>();// 附件集合
	private List<File> deleteFileList = new ArrayList<File>();// 要删除的附件集合
	private List<String> acceptanceList = new ArrayList<String>();// 验收标准
	private List<String> relateTasksList = new ArrayList<String>();// 关联任务
	private List<String> relateTargetList = new ArrayList<String>();// 关联目标
	private List<String> copyToList = new ArrayList<String>();// 抄送
	private List<String> executionList = new ArrayList<String>();// 执行人
	private ClientBean clientBean;// 手动输入客户
	private String beginTime;// 开始时间
	private String endTime;// 结束时间
	private int betweenTime = 1;// 间隔
	private String relateClientName;// 客户姓名
	private String relateClientPhone;// 客户电话
	private TargetMemberBean targetMemberBean;
	private LeaderBean leaderBean;
	
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static String PHOTO_FILE_NAME = "";//文件名

	public static KzxAddTaskFragment newInstance(TargetMemberBean targetMemberBean,LeaderBean leaderBean) {
		KzxAddTaskFragment fragment = new KzxAddTaskFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("targetMemberBean", targetMemberBean);
		bundle.putSerializable("leaderBean", leaderBean);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		targetMemberBean = (TargetMemberBean) getArguments().getSerializable("targetMemberBean");
		leaderBean= (LeaderBean) getArguments().getSerializable("leaderBean");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_add_task_fragment, null);
		return contentView;
	}

	// 注册广播
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".ADD_TASK_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		titleCt = (FloatLabeledEditText) view.findViewById(R.id.titleCt);
		contentCt = (FloatLabeledEditText) view.findViewById(R.id.contentCt);
		// 初始化完成周期时间
		beginTime = DateUtil.getNowTime();
		endTime = format.format(DateUtil.getTimestampExpiredDay(DateUtil.toDate(beginTime), 1));
		// 是否紧急
		isUrgency = (CheckBox) view.findViewById(R.id.isUrgency);
		// 验收标准
		cameraBtn= (ImageButton) view.findViewById(R.id.cameraBtn);
		galleryBtn= (ImageButton) view.findViewById(R.id.galleryBtn);
		addNewAcceptanceBtn = (ImageButton) view.findViewById(R.id.addNewAcceptanceBtn);
		acceptanceCustomList = (LinearLayout) view.findViewById(R.id.acceptanceCustomList);
		attachmentCustomList = (LinearLayout) view.findViewById(R.id.attachmentCustomList);
		// 注册广播
		registerMessageReceiver();
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		saveBtn = (Button) view.findViewById(R.id.saveBtn);
		// 返回
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)). 
//			     hideSoftInputFromWindow(titleCt.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				KeyBoardUtils.closeKeybord(titleCt.getEditText(), getActivity());
				getActivity().finish();
			}
		});
		cameraBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 拍照
				if(attachmentCustomList.getChildCount()<3){
					camera(v);
				}else{
					MsgTools.toast(getActivity(),getResources().getString(ResourceMap.getString_upload_max_length()), ResourceMap.LENGTH_SHORT);
				}
			}
		});
		galleryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 图库
				if(attachmentCustomList.getChildCount()<3){
					gallery(v);
				}else{
					MsgTools.toast(getActivity(), getResources().getString(ResourceMap.getString_upload_max_length()), ResourceMap.LENGTH_SHORT);
				}
			}
		});
		// 完成周期
		cycleDoneBtn = (LinearLayout) view.findViewById(R.id.cycleDoneBtn);
		cycleDoneTag = (TextView) view.findViewById(R.id.cycleDoneTag);
		cycleDoneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "cycle_done");
				startActivity(mIntent);
			}
		});
		// 抄送人
		copySelectedBtn = (LinearLayout) view.findViewById(R.id.copySelectedBtn);
		copySelectedTag = (TextView) view.findViewById(R.id.copySelectedTag);
		copySelectedBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isLeaderHash);
				mIntent.putExtra("action", "copy_selected");
				startActivity(mIntent);
			}
		});
		// 执行人
		executionTag = (TextView) getView().findViewById(R.id.executionTag);
		executionSelectedBtn = (LinearLayout) view.findViewById(R.id.executionSelectedBtn);
		executionSelectedBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isExecutionLeaderHash);
				mIntent.putExtra("action", "execution_selected");
				mIntent.putExtra("typeStr", "single-select");
				startActivity(mIntent);
			}
		});
		// 关联目标
		targetBtn = (LinearLayout) view.findViewById(R.id.targetBtn);
		targetTag = (TextView) view.findViewById(R.id.targetTag);
		targetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isTargetHash);
				mIntent.putExtra("action", "target_selected");
				startActivity(mIntent);
			}
		});
		// 相关任务
		taskBtn = (LinearLayout) view.findViewById(R.id.taskBtn);
		taskSelectedTag = (TextView) view.findViewById(R.id.taskSelectedTag);
		taskBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "task_selected");
				mIntent.putExtra("data", isTaskHash);
				startActivity(mIntent);
			}
		});
		// 关联客户
		clientBtn = (LinearLayout) view.findViewById(R.id.clientBtn);
		clientTag = (TextView) view.findViewById(R.id.clientTag);
		clientBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isClientBeanHash);
				mIntent.putExtra("action", "client_selected");
				startActivity(mIntent);
			}
		});
		// 隐藏伸缩动画
		headerIv = (ImageView) view.findViewById(R.id.headerIv);
		headerTv = (TextView) view.findViewById(R.id.headerTv);
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
					headerTv.setText(getResources().getString(ResourceMap.getString_task_less_hint()));
				} else {
					collapse();
					Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.imageview_rotate_2);
					LinearInterpolator lin = new LinearInterpolator();
					operatingAnim.setInterpolator(lin);
					operatingAnim.setFillAfter(true);
					headerIv.startAnimation(operatingAnim);
					headerTv.setText(getResources().getString(ResourceMap.getString_task_more_hint()));
				}
			}
		});
		// 新验收标准
		addNewAcceptanceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAcceptanceDialog();
			}
		});
		//targetMemberBean不为空的时候表示是要落实目标
		if(targetMemberBean!=null){
			isTargetHash = new HashMap<String, TargetCanRelateBean>();
			TargetCanRelateBean targetCanRelateBean=new TargetCanRelateBean();
			targetCanRelateBean.setId(targetMemberBean.getId());
			targetCanRelateBean.setDescription(targetMemberBean.getDescription());
			isTargetHash.put(targetMemberBean.getId(), targetCanRelateBean);
			targetTag.setText(isTargetHash.size()+getResources().getString(ResourceMap.getString_task_target_amount()));
			//添加到post参数请求中
			relateTargetList.add(targetMemberBean.getId());
			expand();
		}
		//leaderBean不为空的时候执行人
		if(leaderBean!=null){
			isExecutionLeaderHash=new HashMap<String, LeaderBean>();
			isExecutionLeaderHash.put(leaderBean.getId(), leaderBean);
			executionList.add(leaderBean.getId());
			executionTag.setText(leaderBean.getName());
		}
		
		// 保存
		saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String titleStr = titleCt.getText().toString();
				if (StringUtils.isEmpty(titleStr)) {
					MsgTools.toast(getActivity(), getResources().getString(ResourceMap.getString_task_content_hint()), ResourceMap.LENGTH_SHORT);
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
				int length=titleStr.length();
				if(length<=20){
					rp.put("title",titleStr);
				}else if(length>20){
					String contentStr=titleStr.substring(20);
					rp.put("title",titleStr.substring(0, 20));
					rp.put("content", contentStr);// 内容
				}
				
				rp.put("isUrgency", isUrgency.isChecked()?"1":"0");// 是否紧急
				if (!StringUtils.isNullOrEmpty(executionList)) {
					rp.put("executor", new Gson().toJson(executionList));// 执行人
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					rp.put("startTime", format.parse(beginTime).getTime());// 开始时间
					rp.put("endTime", format.parse(endTime).getTime());// 结束时间
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (!StringUtils.isNullOrEmpty(acceptanceList)) {
					rp.put("acceptStandard", new Gson().toJson(acceptanceList));// 验收标准
				}
				if (!StringUtils.isNullOrEmpty(relateTasksList)) {
					rp.put("relateTasks", new Gson().toJson(relateTasksList));// 相关任务
				}
				if (!StringUtils.isNullOrEmpty(copyToList)) {
					rp.put("copyto", new Gson().toJson(copyToList));// 抄送人
				}
				if (!StringUtils.isEmpty(relateClientPhone)) {
					rp.put("relateClientPhone", relateClientPhone);// 客户电话
				}
				if (!StringUtils.isEmpty(relateClientName)) {
					rp.put("relateClientName", relateClientName);// 客户电话
				}
				if (!StringUtils.isNullOrEmpty(relateTargetList)) {
					rp.put("relateTarget", relateTargetList.get(0));// 关联目标
				}
				if (!StringUtils.isNullOrEmpty(fileList)) {
					try {
						for (int i = 0; i < fileList.size(); i++) {
							File mFile = fileList.get(i);
							rp.put("Filedata" + i, mFile);// 附件
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				postSave(rp);
			}
		});
		//弹出键盘
		titleCt.postDelayed(new Runnable() {
			@Override
			public void run() {
				titleCt.requestFieldFocus();
				contentCt.clearFocus();
				KeyBoardUtils.openKeybord(titleCt.getEditText(), getActivity());
			}
		},300);
	}

	/**保存*/ 
	private void postSave(RequestParams rp) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.addTaskAPI, rp, responseHandler);
	}

	/**保存回调*/ 
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
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
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				if (!success) {
					String data = new JSONObject(content).optString("data","");
					boolean gemNotEnough = new JSONObject(data).optBoolean("gemNotEnough", false);
					if (gemNotEnough) {
						new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
						.setIcon(null)
						.setMessage(message)
						.setTitle(R.string.prompt)
						.setNegativeButton(R.string.alert_dialog_contact, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "059163123239")));
							}
						})
						.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).create().show();
					}else{
						MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
					}
				} else {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_LONG);
					Intent mIntent = new Intent(getActivity().getPackageName() + ".BENCH_RECEIVED_ACTION");
					mIntent.putExtra("action", "destroy");
					mIntent.putExtra("content_pager", 1);
					getActivity().sendBroadcast(mIntent);
					getActivity().finish();
				}
			} catch (Exception e) {
				MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};

	/**回调广播*/ 
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".ADD_TASK_RECEIVED_ACTION").equals(intent.getAction())) {
				String action = intent.getStringExtra("action");
				String waitStr=getResources().getString(R.string.task_wait_more);
				String personStr=getResources().getString(R.string.task_person_more);
				// 抄送人
				if ("copy_selected".equals(action)) {
					String showName = null;// 取第一位姓名
					copyToList.clear();
					isLeaderHash = (HashMap<String, LeaderBean>) intent.getSerializableExtra("data");
					for (Map.Entry<String, LeaderBean> entry : isLeaderHash.entrySet()) {
						LeaderBean leaderBean = entry.getValue();
						showName = leaderBean.getName();
						copyToList.add(leaderBean.getId());
					}
					if (isLeaderHash.size() > 1) {
						copySelectedTag.setText(showName + waitStr + isLeaderHash.size() + personStr);
					} else {
						copySelectedTag.setText(showName);
					}
				} else if ("execution_selected".equals(action)) {
					// 执行人
					String showName = null;// 取第一位姓名
					executionList.clear();
					isExecutionLeaderHash = (HashMap<String, LeaderBean>) intent.getSerializableExtra("data");
					for (Map.Entry<String, LeaderBean> entry : isExecutionLeaderHash.entrySet()) {
						LeaderBean leaderBean = entry.getValue();
						showName = leaderBean.getName();
						executionList.add(leaderBean.getId());
					}
					if (isExecutionLeaderHash.size() > 1) {
						executionTag.setText(showName + waitStr + isExecutionLeaderHash.size() + personStr);
					} else {
						executionTag.setText(showName);
					}
				} else if ("task_selected".equals(action)) {
					// 相关任务
					relateTasksList.clear();
					isTaskHash = (HashMap<String, TaskCanRelateBean>) intent.getSerializableExtra("data");
					taskSelectedTag.setText(getString(R.string.task_count,isTaskHash.size()));
					for (Map.Entry<String, TaskCanRelateBean> entry : isTaskHash.entrySet()) {
						TaskCanRelateBean taskCanRelateBean = entry.getValue();
						relateTasksList.add(taskCanRelateBean.getId());
					}
				} else if ("target_selected".equals(action)) {
					// 相关目标
					relateTargetList.clear();
					isTargetHash = (HashMap<String, TargetCanRelateBean>) intent.getSerializableExtra("data");
//					targetTag.setText(isTargetHash.size() + getResources().getString(ResourceMap.getString_task_target_amount()));
					for (Map.Entry<String, TargetCanRelateBean> entry : isTargetHash.entrySet()) {
						TargetCanRelateBean targetCanRelateBean = entry.getValue();
						relateTargetList.add(targetCanRelateBean.getId());
						targetTag.setText(targetCanRelateBean.getTitle());
						break;
					}
				} else if ("cycle_done".equals(action)) {
					// 完成周期(天)
					beginTime = intent.getStringExtra("beginDate") + " " + intent.getStringExtra("beginTime")+":00";
					endTime = intent.getStringExtra("endDate") + " " + intent.getStringExtra("endTime")+":00";
					betweenTime = intent.getIntExtra("betweenTime", 1);
					cycleDoneTag.setText(betweenTime + getResources().getString(ResourceMap.getString_task_day()));
				} else if ("client_selected".equals(action)) {
					// 相关目标
					isClientBeanHash = (HashMap<String, ClientBean>) intent.getSerializableExtra("data");
					for (Map.Entry<String, ClientBean> entry : isClientBeanHash.entrySet()) {
						ClientBean clientBean = entry.getValue();
						relateClientPhone = clientBean.getPhone();
						relateClientName = clientBean.getName();
					}
					clientTag.setText(relateClientName);
				} else if ("client_add".equals(action)) {
					// 相关目标
					clientBean = (ClientBean) intent.getSerializableExtra("data");
					relateClientPhone = clientBean.getPhone().replaceAll(" ", "").replaceAll("[+]86", "");
					relateClientName = clientBean.getName();
					clientTag.setText(relateClientName);
				}
			}
		}
	}

	/**弹出验收标准Dialog*/ 
	private void showAcceptanceDialog() {
		final View contentView = getActivity().getLayoutInflater().inflate(R.layout.kzx_add_acceptance_dialog_fragment, null);
		final EditText contentTv = (EditText) contentView.findViewById(R.id.contentTv);
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setTitle(R.string.acceptance_title).setView(contentView).setPositiveButton(R.string.alert_dialog_contiue, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				final String contentStr = contentTv.getText().toString();
				if (!StringUtils.isEmpty(contentStr)) {
					addNewAcceptance(contentStr);
					showAcceptanceDialog();
				}
			}
		}).setNegativeButton(R.string.alert_dialog_add, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				final String contentStr = contentTv.getText().toString();
				if (!StringUtils.isEmpty(contentStr)) {
					addNewAcceptance(contentStr);
					KeyBoardUtils.closeKeybord(contentTv, getActivity());
					dialog.dismiss();
				}
			}
		}).create().show();
		contentTv.requestFocus();
		titleCt.clearFocus();
		contentCt.clearFocus();
		KeyBoardUtils.openKeybord(contentTv, getActivity());
	}

	/**添加新的验收标准*/ 
	private void addNewAcceptance(final String contentStr) {
		final View acceptanceView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_new_acceptance_lv_item_fragment, null);
		TextView acceptanceTv = (TextView) acceptanceView.findViewById(R.id.acceptanceTv);
		ImageButton clearBtn = (ImageButton) acceptanceView.findViewById(R.id.clearBtn);
		acceptanceTv.setText(contentStr);
		// 删除当前验收标准
		clearBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptanceCustomList.removeView((View) acceptanceCustomList.findViewWithTag(contentStr));
				acceptanceList.remove(contentStr);
				resetMeasureSpec(acceptanceView,true);
			}
		});
		// 删除当前验收标准
		acceptanceView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptanceCustomList.removeView((View) acceptanceCustomList.findViewWithTag(contentStr));
				acceptanceList.remove(contentStr);
				resetMeasureSpec(acceptanceView,true);
			}
		});
		// 设置Tag管理
		acceptanceView.setTag(contentStr);
		// 添加到视图中
		acceptanceList.add(contentStr);
		acceptanceCustomList.addView(acceptanceView);
		
		resetMeasureSpec(acceptanceView,false);
	}

	/**重新计算高度防止视图溢出*/
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

	private void expand() {
		// set Visible
		mLinearLayout.setVisibility(View.VISIBLE);
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		mAnimator.start();
	}

	//收起动画
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
	
	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
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

	/**添加照片UI*/
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
				attachmentCustomList.removeView((View) attachmentCustomList.findViewWithTag(currentFile));
				if(attachmentCustomList.getChildCount()==0){
					resetMeasureSpec(attachmentView, true);
				}
			}
		});
		attachmentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fileList.remove(currentFile);
				currentFile.delete();
				attachmentCustomList.removeView((View) attachmentCustomList.findViewWithTag(currentFile));
				if(attachmentCustomList.getChildCount()==0){
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
		attachmentCustomList.addView(attachmentView);
		
		/**不必重复添加高度*/
		if(attachmentCustomList.getChildCount()>1){
			return;
		}
		resetMeasureSpec(attachmentView, false);
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
			if(uri!=null){
				//判断图片大小是否有超过100K
				if(FileSizeUtil.getFileOrFilesSize(FileUtil.getFileByUri(getActivity(), uri), FileSizeUtil.SIZETYPE_KB)>Constants.Filesize){
					newFile=BitmapUtil.transImage(FileUtil.getFileByUri(getActivity(), uri), FileUtil.getFileFromUrl().getAbsolutePath(), 100);
					addNewPictureUi(newFile,false);
				}else{
					newFile=new File(FileUtil.getFileByUri(getActivity(), uri));
					addNewPictureUi(newFile,true);
				}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMessageReceiver != null) {
			getActivity().unregisterReceiver(mMessageReceiver);
		}
		if (asyncHttpClient != null) {
			asyncHttpClient.cancelRequests(getActivity(), true);
		}
		if (!StringUtils.isNullOrEmpty(deleteFileList)) {
			for (int i = 0; i < deleteFileList.size(); i++) {
				File mFile = deleteFileList.get(i);
				mFile.delete();
			}
		}
	}

}
