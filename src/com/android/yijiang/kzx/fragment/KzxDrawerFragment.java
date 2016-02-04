package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import android.preference.Preference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.bean.KzxTokenBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.BinaryHttpResponseHandler;
import com.android.yijiang.kzx.http.PersistentCookieStore;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.AppConstants;
import com.android.yijiang.kzx.ui.AppManager;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.LoginActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 菜单Fragment
 * @title com.android.tanke.bus.fragment
 * @date 2014年5月28日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxDrawerFragment extends BaseFragment {
	
	private String TAG=KzxDrawerFragment.class.getName();
	
	private static final int MEDAL_EMPTY_COUNT=0;
	private SharedPreferences TagAliasCallbackTp;

	private RelativeLayout drawerLayout;//菜单集合
	private LinearLayout workbench;//工作台
	private LinearLayout partner;//我的伙伴
	private LinearLayout strategic;//战略目标
	private LinearLayout member;//全体成员
	private LinearLayout incentive;//激励认可
	private LinearLayout notice;//公告通知
	private LinearLayout service;//服务商
	
	private CircleImageView memberIcon;//头像
	private EditText memberNameTv;//用户名
	private TextView medalCountTv;//勋章数
	private TextView growUpTv;//等级
	private ImageButton settingsTv;
	private ImageButton messageTv;
	private ImageView stateTv;
	
	private AsyncHttpClient asyncHttpClient;
	private MessageReceiver mMessageReceiver;
	private DisplayImageOptions options;
	
	public static KzxDrawerFragment newInstance() {
		KzxDrawerFragment fragment = new KzxDrawerFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.drawer_frame, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		registerMessageReceiver();
		memberIcon=(CircleImageView)view.findViewById(R.id.memberIcon);
		memberNameTv=(EditText)view.findViewById(R.id.memberNameTv);
		medalCountTv=(TextView)view.findViewById(R.id.medalCountTv);
		growUpTv=(TextView)view.findViewById(R.id.growUpTv);
		drawerLayout=(RelativeLayout)view.findViewById(R.id.drawerLayout);
		workbench=(LinearLayout) view.findViewById(R.id.workbench);//工作台
		partner=(LinearLayout) view.findViewById(R.id.partner);//我的伙伴
		strategic=(LinearLayout) view.findViewById(R.id.strategic);//战略目标
		member=(LinearLayout) view.findViewById(R.id.member);//全体成员
		incentive=(LinearLayout) view.findViewById(R.id.incentive);//激励认可
		notice=(LinearLayout) view.findViewById(R.id.notice);//公告通知
		service=(LinearLayout) view.findViewById(R.id.service);//服务商
		settingsTv=(ImageButton) view.findViewById(R.id.settingsTv);
		messageTv=(ImageButton) view.findViewById(R.id.messageTv);
		stateTv=(ImageView) view.findViewById(R.id.stateTv);
		drawerLayout.setTag(workbench);
		workbench.setOnClickListener(onDrawerListener);
		partner.setOnClickListener(onDrawerListener);
		strategic.setOnClickListener(onDrawerListener);
		member.setOnClickListener(onDrawerListener);
		incentive.setOnClickListener(onDrawerListener);
		notice.setOnClickListener(onDrawerListener);
		service.setOnClickListener(onDrawerListener);
		//设置事件
		settingsTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(getActivity().getPackageName()+".HOME_RECEIVED_ACTION");
				mIntent.putExtra("action", "settings");
				mIntent.putExtra("title", getString(R.string.drawer_settings_hint));
				getActivity().sendBroadcast(mIntent);
			}
		});
		//消息事件
		messageTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(v.getContext(),ContentFragmentActivity.class);
				mIntent.putExtra("action", "message");
				startActivity(mIntent);
			}
		});
		//个人账户中心
		memberIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getContext().getKzxTokenBean()!=null){
					Intent mIntent=new Intent(getActivity(),ContentFragmentActivity.class);
					mIntent.putExtra("action", "account_info");
					mIntent.putExtra("kzxTokenBean", getContext().getKzxTokenBean());
					startActivity(mIntent);
				}
			}
		});
		// 编辑名字
		memberNameTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					final String contentStr = v.getText().toString();
					if (!StringUtils.isEmpty(contentStr)) {
						RequestParams rp = new RequestParams();
						rp.put("name", contentStr);
						postUploadAccount(rp, Constants.uploadAccountAPI);
					}
				}
				return false;
			}
		});
		//请求账户数据
		getKzxToken();
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
	};
	
	//点击样式
	OnClickListener onDrawerListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			//首页广播跳转到对应的Fragment
			Intent mIntent=new Intent(getActivity().getPackageName()+".HOME_RECEIVED_ACTION");
			switch (v.getId()) {
			case R.id.workbench:
				mIntent.putExtra("action", "workbench");
				mIntent.putExtra("title", "工作台");
				break;
			case R.id.partner:
				mIntent.putExtra("action", "partner");
				mIntent.putExtra("title", "我的伙伴");
				break;
			case R.id.strategic:
				mIntent.putExtra("action", "strategic");
				mIntent.putExtra("title", "战略目标");
				break;
			case R.id.service:
				mIntent.putExtra("action", "service");
				mIntent.putExtra("title", "服务商");
				break;
			case R.id.member:
				mIntent.putExtra("action", "member");
				mIntent.putExtra("title", "全体成员");
				break;
			case R.id.incentive:
				mIntent.putExtra("action", "incentive");
				mIntent.putExtra("title", "激励认可");
				break;
			case R.id.notice:
				mIntent.putExtra("action", "notice");
				mIntent.putExtra("title", "公告通知");
				break;
			default:
				break;
			}
			getActivity().sendBroadcast(mIntent);
		}
	};
	
	/**上传资料*/ 
	private void postUploadAccount(RequestParams rp, String path) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), path, rp, accountResponseHandler);
	}
	
	/**上传资料回调*/ 
	AsyncHttpResponseHandler accountResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (success) {
					SharedPreferences sp = getActivity().getSharedPreferences("kzx_token_info", 0);
					sp.edit().putString("accountName", memberNameTv.getText().toString()).commit();
					memberNameTv.clearFocus();
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
	
	/**获取用户信息*/ 
	private void getKzxToken() {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.getKzxTokenAPI, rp, responseHandler);
	}

	/**获取用户信息回调*/ 
	AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			initKzxToken(getContext().getKzxTokenBean());
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content=new String(responseBody);
			try {
				boolean success=new JSONObject(content).optBoolean("success",false);
				String message=new JSONObject(content).optString("message");
				String data=new JSONObject(content).optString("data");
				if(success){
					KzxTokenBean kzxTokenBean=new Gson().fromJson(data, KzxTokenBean.class);
					// 设置消息红点
					stateTv.setVisibility(Integer.valueOf(kzxTokenBean.getNoReadMessageNum())>0?View.VISIBLE:View.GONE);
					// 设置推送别名
					setAliasAndTags(kzxTokenBean.getAccountId());
					SharedPreferences sp = getActivity().getSharedPreferences("kzx_token_info", 0);
					sp.edit().putInt("medalCount", kzxTokenBean.getMedalCount()).commit();
					sp.edit().putString("accountName", kzxTokenBean.getAccountName()).commit();
					sp.edit().putString("accountId", kzxTokenBean.getAccountId()).commit();
					sp.edit().putString("memberIcon", kzxTokenBean.getMemberIcon()).commit();
					sp.edit().putInt("growUp", kzxTokenBean.getGrowUp()).commit();
					sp.edit().putString("memberId", kzxTokenBean.getMemberId()).commit();
					sp.edit().putString("encryptMemberId", kzxTokenBean.getEncryptMemberId()).commit();
					sp.edit().putString("memberName", kzxTokenBean.getMemberName()).commit();
					sp.edit().putString("companyName", kzxTokenBean.getCompanyName()).commit();
					sp.edit().putString("phone", kzxTokenBean.getPhone()).commit();
					sp.edit().putString("email", kzxTokenBean.getEmail()).commit();
					sp.edit().putString("department", kzxTokenBean.getDepartment()).commit();
					sp.edit().putString("leader", kzxTokenBean.getLeader()).commit();
					sp.edit().putString("leaderName", kzxTokenBean.getLeaderName()).commit();
					sp.edit().putString("clientIds", kzxTokenBean.getClientIds()).commit();
					sp.edit().putString("encryptClientIds", kzxTokenBean.getEncryptClientIds()).commit();
					sp.edit().putString("noReadMessageNum", kzxTokenBean.getNoReadMessageNum()).commit();
					sp.edit().putInt("isSendSms", kzxTokenBean.getIsSendSms()).commit();
					initKzxToken(kzxTokenBean);
					
					Intent mIntent=new Intent(getActivity().getPackageName()+".BENCH_RECEIVED_ACTION");
					mIntent.putExtra("action", "get_message_amount");
					getActivity().sendBroadcast(mIntent);
					
				}else{
					Log.i("token", message);
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

	/**初始化侧边栏用户信息*/
	private void initKzxToken(KzxTokenBean kzxTokenBean){
		loadAvater(kzxTokenBean.getMemberIcon());
		memberNameTv.setText(kzxTokenBean.getAccountName());
		medalCountTv.setText(kzxTokenBean.getMedalCount()+"");
		medalCountTv.setEnabled(kzxTokenBean.getMedalCount()==MEDAL_EMPTY_COUNT?false:true);
		growUpTv.setText("LV "+kzxTokenBean.getGrowUp());
	}
	
	/**头像加载*/
	private void loadAvater(String url){
//		Picasso.with(getActivity())
//		.load(url)
//		.placeholder(R.drawable.ic_avatar_120)
//		.error(R.drawable.ic_avatar_120)
//		.into(memberIcon);
		ImageLoader.getInstance().displayImage(url, memberIcon, options);
	}
	
	/**注册广播*/ 
	public void registerMessageReceiver() {
		TagAliasCallbackTp = getActivity().getSharedPreferences("token_info", 0);
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".DRAWER_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}
	
	/**回调广播*/ 
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".DRAWER_RECEIVED_ACTION").equals(intent.getAction())) {
				String drawerAction=intent.getStringExtra("action");
				//刷新头像
				if("avater".equals(drawerAction)){
					String avater=intent.getStringExtra("avater");
					SharedPreferences sp = getActivity().getSharedPreferences("kzx_token_info", 0);
					sp.edit().putString("memberIcon", avater).commit();
					loadAvater(avater);
				}else if("name".equals(drawerAction)){
					//刷新名字
					String name=intent.getStringExtra("name");
					memberNameTv.setText(name);
				}else if("reload".equals(drawerAction)){
					getKzxToken();
				}else if("clear_message_amount".equals(drawerAction)){
					stateTv.setVisibility(View.GONE);
					SharedPreferences sp = getActivity().getSharedPreferences("kzx_token_info", 0);
					sp.edit().putString("noReadMessageNum", "0").commit();
				}else if("clear_focus".equals(drawerAction)){
					memberNameTv.post(new Runnable() {
						@Override
						public void run() {
							memberNameTv.clearFocus();
							KeyBoardUtils.closeKeybord(memberNameTv, getActivity());							
						}
					});
				}
			}
		}
	}
	
	/**别名 alias*/
	/**为安装了应用程序的用户，取个别名来标识。以后给该用户 Push 消息时，就可以用此别名来指定。每个用户只能指定一个别名。*/
	/**判断防止重复去绑定别名*/
	private void setAliasAndTags(String accountId){
		if(!TagAliasCallbackTp.getBoolean("TAG_ALIAS", false)){
			JPushInterface.setAliasAndTags(getActivity(), accountId, null, mAliasCallback);
		}
	}
	
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case AppConstants.ALIASCALLBACK_SUCCESS:
                logs = "Set tag and alias success";
                Log.i(TAG, logs);
                //绑定成功记录数据防止再次绑定
                TagAliasCallbackTp.edit().putBoolean("TAG_ALIAS", true).commit();
                break;
            case AppConstants.ALIASCALLBACK_TIMEOUT:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(TAG, logs);
                break;
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
            }
        }
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mMessageReceiver!=null)
			getActivity().unregisterReceiver(mMessageReceiver);
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
	}
	
	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

}
