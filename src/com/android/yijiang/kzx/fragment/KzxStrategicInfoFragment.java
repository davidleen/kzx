package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.cookie.Cookie;
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicInfoAdapter;
import com.android.yijiang.kzx.bean.PartnerBean;
import com.android.yijiang.kzx.bean.TargetDutyersBean;
import com.android.yijiang.kzx.bean.TargetMemberBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
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
import com.android.yijiang.kzx.widget.FloatLabeledEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 战略目标详情
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxStrategicInfoFragment extends BaseFragment {

	private String TAG=KzxStrategicInfoFragment.class.getName();
	
	private SwipeRefreshLayout swipeLayout;
	private ListView dateCustomList;
	private View headerView;
	private FloatLabeledEditText targetTitleTv;
	private FloatLabeledEditText targetTimeTv;
	private FloatLabeledEditText targetDescriptionTv;
	private FloatLabeledEditText completeStandardTv;
	private TextView dutyersTv;
	private LinearLayout doneTargetSwitch;//落实目标
	private ImageView bottomDivider;//divider
	
	private TargetMemberBean targetMemberBean;
	private String targetId;
	private boolean notDoDecrypt;
	
	private Dialog dialog;
	private AsyncHttpClient asyncHttpClient;
	private KzxStrategicInfoAdapter kzxStrategicAdapter;
	
	//头部View
	private ImageButton btn_more;
	private ImageButton backBtn;
	
	private PopupWindow popupWindow;
	private LinearLayout editBtn;
	private LinearLayout doneBtn;
	private TextView cancelBtn;
	
	public static KzxStrategicInfoFragment newInstance(TargetMemberBean targetMemberBean,String targetId,boolean notDoDecrypt) {
		KzxStrategicInfoFragment fragment = new KzxStrategicInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("targetMemberBean", targetMemberBean);
		bundle.putString("targetId", targetId);
		bundle.putBoolean("notDoDecrypt", notDoDecrypt);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		targetMemberBean=(TargetMemberBean) getArguments().getSerializable("targetMemberBean");
		targetId=getArguments().getString("targetId", "");
		notDoDecrypt=getArguments().getBoolean("notDoDecrypt",false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_strategic_info_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headerView=getLayoutInflater(savedInstanceState).inflate(R.layout.kzx_strategic_header_info_fragment, null);
		targetTitleTv=(FloatLabeledEditText) headerView.findViewById(R.id.targetTitleTv);
		targetTimeTv=(FloatLabeledEditText) headerView.findViewById(R.id.targetTimeTv);
		targetDescriptionTv=(FloatLabeledEditText) headerView.findViewById(R.id.targetDescriptionTv);
		completeStandardTv=(FloatLabeledEditText) headerView.findViewById(R.id.completeStandardTv);
		targetTitleTv.getEditText().setEnabled(false);
		targetTimeTv.getEditText().setEnabled(false);
		targetDescriptionTv.getEditText().setEnabled(false);
		completeStandardTv.getEditText().setEnabled(false);
		doneTargetSwitch=(LinearLayout)view.findViewById(R.id.doneTargetSwitch);
		bottomDivider=(ImageView)view.findViewById(R.id.bottomDivider);
		dutyersTv=(TextView) headerView.findViewById(R.id.dutyersTv);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_red_dark, android.R.color.holo_blue_dark,
				android.R.color.holo_red_dark);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				postLoadForHeader();
				postLoad();
			}
		});
		kzxStrategicAdapter=new KzxStrategicInfoAdapter(getActivity());
		dateCustomList=(ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.addHeaderView(headerView, null, false);
		dateCustomList.setAdapter(kzxStrategicAdapter);
		dateCustomList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TargetDutyersBean targetDutyersBean=(TargetDutyersBean) parent.getItemAtPosition(position);
				PartnerBean partnerBean=new PartnerBean();
				partnerBean.setIcon(targetDutyersBean.getIcon());
				partnerBean.setPartnerName(targetDutyersBean.getName());
				partnerBean.setPartnerId(targetDutyersBean.getId());
				partnerBean.setTargetId(targetMemberBean.getId());
				Intent mIntent=new Intent(getActivity(),ContentFragmentActivity.class);
				mIntent.putExtra("action", "partner_info");
				mIntent.putExtra("partnerBean", partnerBean);
				startActivity(mIntent);				
			}
		});
		backBtn=(ImageButton)view.findViewById(R.id.backBtn);
		btn_more=(ImageButton)view.findViewById(R.id.btn_more);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
				Intent i = new Intent(getActivity(), MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				getActivity().startActivity(i);
			}
		});
		btn_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.showAsDropDown(v);
			}
		});
		doneTargetSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(getActivity(),ContentFragmentActivity.class);
				mIntent.putExtra("action", "add_task");
				mIntent.putExtra("targetMemberBean", targetMemberBean);
				startActivity(mIntent);
			}
		});
		// popwindow弹出层设置
		View leftMenuView = getActivity().getLayoutInflater().inflate(R.layout.target_info_right_menu_fragment, null);
		popupWindow = new PopupWindow(leftMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.popWindowAnim);
		popupWindow.update();
		editBtn=(LinearLayout) leftMenuView.findViewById(R.id.editBtn);
		doneBtn=(LinearLayout) leftMenuView.findViewById(R.id.doneBtn);
		cancelBtn=(TextView) leftMenuView.findViewById(R.id.cancelBtn);
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				postCompleted(0);
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				postCompleted(1);
			}
		});
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//判断是否要重新加载headerview数据
		postLoadForHeader();
		//获取数据
		postLoad();
	}
	
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**初始化headerView UI数据更新*/
	private void initHeaderData(TargetMemberBean targetMemberBean){
		if(targetMemberBean==null) return;
		targetTitleTv.setText(targetMemberBean.getTitle());
		targetTimeTv.setText(DateUtils.getStrTime(targetMemberBean.getStartTime(),"MM月dd日")+"/"+DateUtils.getStrTime(targetMemberBean.getEndTime(),"MM月dd日"));
		targetDescriptionTv.setText(targetMemberBean.getDescription());
		String completeStr=!StringUtils.isEmpty(targetMemberBean.getCompleteStandard())?targetMemberBean.getCompleteStandard():"无.";
		completeStandardTv.setText(completeStr);
		//判断是否需要有落实目标事件(targetMemberBean.getTaskCount()==0&&)
		if(targetMemberBean.getState()==1){
			doneTargetSwitch.setVisibility(View.VISIBLE);
			bottomDivider.setVisibility(View.VISIBLE);
		}else{
			bottomDivider.setVisibility(View.GONE);
			doneTargetSwitch.setVisibility(View.GONE);
		}
		//目标已完成情况下,隐藏所有菜单操作
		if(targetMemberBean.getState()!=3&&targetMemberBean.isSelf()){
			btn_more.setVisibility(View.VISIBLE);
		}else{
			btn_more.setVisibility(View.GONE);
		}
	}
	
	/**加载headerView数据*/
	private void postLoadForHeader(){
		if(targetMemberBean==null&&!StringUtils.isEmpty(targetId)){
			asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
			asyncHttpClient.setUserAgent(Constants.USER_AGENT);
			RequestParams rp = new RequestParams();
			rp.put("id",targetId);
			
			if(notDoDecrypt){
				rp.put("notDoDecrypt", notDoDecrypt);
			}
			
			asyncHttpClient.post(getActivity(), Constants.queryTargetInfoAPI, rp, headerResponseHandler);
		}
	}
	
	/**加载headerView数据回调*/
	AsyncHttpResponseHandler headerResponseHandler=new AsyncHttpResponseHandler(){
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content=new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				String target=new JSONObject(data).optString("target");
				if (!success) {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				} else {
					targetMemberBean=new Gson().fromJson(target, TargetMemberBean.class);
					setTargetId(targetMemberBean.getId());//重新设置加密ID
					initHeaderData(targetMemberBean);
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
	
	/**
	 * 责任人数据请求
	 * notDoDecrypt用于判断是否加密访问(推送需要)
	 */ 
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		
		String targetIdStr=targetMemberBean!=null?targetMemberBean.getId():targetId;
		
		rp.put("targetId",targetIdStr);
		
		if(notDoDecrypt){
			rp.put("notDoDecrypt", notDoDecrypt);
		}
		
		asyncHttpClient.post(getActivity(), Constants.queryDutyersAPI, rp, responseHandler);
	}
	
	/**责任人数据请求回调*/
	AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){
		@Override
		public void onStart() {
			super.onStart();
			swipeLayout.setRefreshing(true);
			initHeaderData(targetMemberBean);
		}
		@Override
		public void onFinish() {
			super.onFinish();
			swipeLayout.setRefreshing(false);
		}
		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content=new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (!success) {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				} else {
					List<TargetDutyersBean> targetDutyersList=new ArrayList<TargetDutyersBean>();
					targetDutyersList.addAll((List<TargetDutyersBean>) new Gson().fromJson(data, new TypeToken<List<TargetDutyersBean>>() {}.getType()));
					kzxStrategicAdapter.setDataForLoader(targetDutyersList);
					dutyersTv.setText(getString(R.string.zerenren_count,targetDutyersList.size()));
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

	/**完成目标和取消目标*/
	private void postCompleted(int i) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		
		String targetIdStr=targetMemberBean!=null?targetMemberBean.getId():targetId;
		
		rp.put("id",targetIdStr);
		asyncHttpClient.post(getActivity(), i==0?Constants.targetCompletedAPI:Constants.targetCancelAPI, rp, postResponseHandler);
	}
	
	/**完成目标和取消目标回调*/ 
	AsyncHttpResponseHandler postResponseHandler=new AsyncHttpResponseHandler(){
		@Override
		public void onStart() {
			super.onStart();
			//close right menu
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();// 关闭
			}
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
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				if (!success) {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				} else {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
					Intent mIntent=new Intent(getActivity().getPackageName()+".TARGET_RECEIVED_ACTION");
					getActivity().sendBroadcast(mIntent);
					getActivity().finish();
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
	
	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
	}

}
