package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.fragment.KzxRegisterFragment.TimeCount;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.PersistentCookieStore;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.AppManager;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 获取密码
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxGetPwdFragment extends BaseFragment {

	private Button btnSure;
	private EditText phoneTv;
	private LinearLayout passwordLinear;
	private LinearLayout codeLinear;
	private Button btnCode;
	private EditText phoneCodeTv;
	private EditText newPwdTv;
	private EditText reNewPwdTv;
	private PersistentCookieStore myCookieStore;

	private int stepCurrent = 1;
	private String dataStr;// 用于修改新密码
	private TimeCount time;
	private Dialog dialog;
	private MessageReceiver mMessageReceiver;
	private AsyncHttpClient asyncHttpClient;
	
	private ImageButton backBtn;
	private TextView titleTv;

	public static KzxGetPwdFragment newInstance() {
		KzxGetPwdFragment fragment = new KzxGetPwdFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_get_pwd_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		registerMessageReceiver();
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		titleTv = (TextView) view.findViewById(R.id.titleTv);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		passwordLinear = (LinearLayout) view.findViewById(R.id.passwordLinear);
		codeLinear = (LinearLayout) view.findViewById(R.id.codeLinear);
		btnSure = (Button) view.findViewById(R.id.btnSure);
		phoneTv = (EditText) view.findViewById(R.id.phoneTv);
		reNewPwdTv = (EditText) view.findViewById(R.id.reNewPwdTv);
		newPwdTv = (EditText) view.findViewById(R.id.newPwdTv);
		btnCode = (Button) view.findViewById(R.id.btnCode);
		phoneCodeTv = (EditText) view.findViewById(R.id.phoneCodeTv);
		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneStr = phoneTv.getText().toString();
				String phoneCodeStr = phoneCodeTv.getText().toString();
				String newPwdStr = newPwdTv.getText().toString();
				String reNewPwdStr = reNewPwdTv.getText().toString();
				myCookieStore = new PersistentCookieStore(v.getContext());// 設置Cookie
				switch (stepCurrent) {
				case 1:
					if (!RegexUtils.checkMobile(phoneStr)) {
						MsgTools.toast(getActivity(), getString(R.string.validate_phone), ResourceMap.LENGTH_SHORT);
						return;
					}
					if (StringUtils.isEmpty(phoneCodeStr)) {
						MsgTools.toast(getActivity(), getString(R.string.validate_phone_code), ResourceMap.LENGTH_SHORT);
						return;
					}
					postVidateCode(phoneStr, phoneCodeStr);
					break;
				case 2:
					if (StringUtils.isEmpty(newPwdStr)) {
						MsgTools.toast(getActivity(), getString(R.string.validate_pwd), ResourceMap.LENGTH_SHORT);
						return;
					}
					if (StringUtils.isEmpty(reNewPwdStr)) {
						MsgTools.toast(getActivity(), getString(R.string.validate_repwd), ResourceMap.LENGTH_SHORT);
						return;
					}
					if (!newPwdStr.equals(reNewPwdStr)) {
						MsgTools.toast(getActivity(), getString(R.string.validate_repwd), ResourceMap.LENGTH_SHORT);
						return;
					}
					postResetPwd(dataStr, newPwdStr, phoneStr);
					break;
				default:
					break;
				}
			}
		});
		btnCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneStr = phoneTv.getText().toString();
				if (!StringUtils.isEmpty(phoneStr) && RegexUtils.checkMobile(phoneStr)) {
					postSmsCode(phoneStr);
				} else {
					MsgTools.toast(getActivity(), getString(R.string.validate_phone), ResourceMap.LENGTH_SHORT);
				}
			}
		});
	}

//	/**验证手机号*/ 
//	private void postCheckPhone(String phone) {
//		asyncHttpClient = new AsyncHttpClient();
//		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
//		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
//		RequestParams rp = new RequestParams();
//		rp.put("phone", phone);
//		asyncHttpClient.post(getActivity(), Constants.queryByPhoneAPI, rp, checkResponseHandler);
//	}
//
//	/**验证手机号回调*/ 
//	AsyncHttpResponseHandler checkResponseHandler = new AsyncHttpResponseHandler() {
//		@Override
//		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//			String content = new String(responseBody);
//			try {
//				boolean success = new JSONObject(content).optBoolean("success");
//				String message = new JSONObject(content).optString("message");
//				String data = new JSONObject(content).optString("data");
//				if (success) {
//					String phoneStr = phoneTv.getText().toString();
//					postSmsCode(phoneStr);
//				} else {
//					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
//			}
//		}
//		@Override
//		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//			onFailureToast(error);
//		}
//	};
	
	/** 验证码验证 */
	private void postVidateCode(final String phone, final String phoneCode) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("phone", phone);
		if (!StringUtils.isEmpty(phoneCode)) {
			rp.put("code", phoneCode);
		}
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.validateCodeByPhoneAPI, rp, responseHandler);
	}

	/** 验证码验证回调 */
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			showProgressDialog();
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success");
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (!success) {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				} else {
					titleTv.setText(getString(R.string.settings_change_pwd_hint));
					if (passwordLinear.getVisibility() != View.VISIBLE) {
						codeLinear.setVisibility(View.GONE);
						phoneTv.setVisibility(View.GONE);
						passwordLinear.setVisibility(View.VISIBLE);
						newPwdTv.setText("");
						reNewPwdTv.setText("");
						// 更改为修改新密码
						stepCurrent = 2;
						dataStr = data;
					}
				}
			} catch (Exception e) {
				MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}

		@Override
		public void onFinish() {
			super.onFinish();
			dialog.dismiss();
		}
	};

	/** 进度条弹出框 */
	private void showProgressDialog() {
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

	/** 重置密码 */
	private void postResetPwd(final String data, final String newPwd, final String username) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("key", data);
		rp.put("password", newPwd);
		rp.put("username", username);
		asyncHttpClient.setCookieStore(myCookieStore);
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.resetPasswordAPI, rp, resetResponseHandler);
	}

	/** 重置密码回调 */
	AsyncHttpResponseHandler resetResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			showProgressDialog();
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success");
				String message = new JSONObject(content).optString("message");
				if (!success) {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				} else {
					successMain();
				}
			} catch (Exception e) {
				MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}

		@Override
		public void onFinish() {
			super.onFinish();
			dialog.dismiss();
		}
	};

	/** 验证成功跳转 */
	private void successMain() {
		SharedPreferences sp = getActivity().getSharedPreferences("token_info", 0);
		// 成功记录登录验证信息ACCESS_KEY和ACCESS_TOKEN
		if (!StringUtils.isNullOrEmpty(myCookieStore.getCookies())) {
			for (Cookie c : myCookieStore.getCookies()) {
				if (c.getName().equals(Constants.ACCESS_KEY)) {
					sp.edit().putString("ACCESS_KEY", c.getValue()).commit();
				} else if (c.getName().equals(Constants.ACCESS_TOKEN)) {
					sp.edit().putString("ACCESS_TOKEN", c.getValue()).commit();
				}
			}
			String phoneStr = phoneTv.getText().toString();
			String pwdStr=newPwdTv.getText().toString();
			SharedPreferences loginSp = getActivity().getSharedPreferences("login_info", 0);
			loginSp.edit().putString("username", phoneStr).commit();
			loginSp.edit().putString("password", pwdStr).commit();
			// 跳转到主页面
			Intent i = new Intent(getActivity(), MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			getActivity().startActivity(i);
		} else {
			MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
		}
	}

	/** 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnCode.setText(getString(R.string.get_again));
			btnCode.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnCode.setEnabled(false);
			btnCode.setText("已发送("+millisUntilFinished / 1000+")");
		}
	}

	/** 获取验证码 */
	private void postSmsCode(final String phone) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("phone", phone);
		rp.put("type", "1");
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.sendCodeByPhoneAPI, rp, smsResponseHandler);
	}

	/** 获取验证码回调 */
	AsyncHttpResponseHandler smsResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			time.start();// 开始计时
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				String message = new JSONObject(content).optString("message");
				MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
			} catch (Exception e) {
				MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};

	/** 注册广播 */
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".REGISTER_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	/** 回调广播 */
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".REGISTER_RECEIVED_ACTION").equals(intent.getAction())) {
				String smsMessage = intent.getStringExtra("message");
				if (smsMessage.indexOf(getString(R.string.app_name)) != -1) {
					phoneCodeTv.setText(smsMessage.substring(0,6));
					String phoneStr = phoneTv.getText().toString();
					String phoneCodeStr = phoneCodeTv.getText().toString();
					if (StringUtils.isEmpty(phoneCodeStr)) {
						MsgTools.toast(getActivity(), getString(R.string.validate_phone_code), ResourceMap.LENGTH_SHORT);
						return;
					}
					if (!RegexUtils.checkMobile(phoneStr)) {
						MsgTools.toast(getActivity(), getString(R.string.validate_phone), ResourceMap.LENGTH_SHORT);
						return;
					}
					postVidateCode(phoneStr, phoneCodeStr);
				}
			}
		}
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(time!=null){
			time.cancel();
		}
		if (asyncHttpClient != null) {
			asyncHttpClient.cancelRequests(getActivity(), true);
		}
		if (mMessageReceiver != null) {
			getActivity().unregisterReceiver(mMessageReceiver);
		}
	}

}
