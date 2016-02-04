package com.android.yijiang.kzx.fragment;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MedalConfigBean;
import com.android.yijiang.kzx.fragment.KzxRegisterFragment.TimeCount;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.AppManager;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.LoginActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.widget.MsgTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 修改手机号弹出框
 * @title com.yijiang.jiangdou.fragment
 * @date 2014年5月7日
 * @author tanke
 */
public class KzxChangePhoneDialogFragment extends DialogFragment {

	private Dialog dialog;
	private EditText codeTv;
	private Button btnSave;
	private ImageButton closeBtn;
	private TextView phoneTv;

	private String phoneStr;
	private TimeCount time;
	private Button btnCode;
	private InputMethodManager mInputMethodManager;
	private AsyncHttpClient asyncHttpClient;
	
	public static KzxChangePhoneDialogFragment newInstance(String phoneStr) {
		KzxChangePhoneDialogFragment fragment = new KzxChangePhoneDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("phoneStr", phoneStr);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phoneStr=getArguments().getString("phoneStr");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		codeTv = (EditText) view.findViewById(R.id.codeTv);
		phoneTv=(TextView)view.findViewById(R.id.phoneTv);
		btnSave = (Button) view.findViewById(R.id.btnSave);
		btnCode= (Button) view.findViewById(R.id.btnCode);
		closeBtn=(ImageButton)view.findViewById(R.id.closeBtn);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String code=codeTv.getText().toString();
				if(!StringUtils.isEmpty(code)){
					RequestParams rp = new RequestParams();
					rp.put("phone", phoneStr);
					rp.put("phoneCode",code);
					postUploadAccount(rp, Constants.uploadAccountAPI);
				}
			}
		});
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mInputMethodManager.hideSoftInputFromWindow(codeTv.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
				dismiss();
			}
		});
		btnCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				postSmsCode(phoneStr);
			}
		});
		phoneTv.setText(getString(R.string.send_phone_hint,phoneStr));
		codeTv.requestFocus(); // EditText获得焦点
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // 显示软键盘
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		postSmsCode(phoneStr);
	}

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
				String data = new JSONObject(content).optString("data");// 1:不用,2:重新登录
				if (success) {
					gotoLogin();
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
	
	
	/** 获取验证码 */
	private void postSmsCode(final String phone) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("phone", phone);
		rp.put("type", "2");
		rp.put("accountId", getContext().getKzxTokenBean().getAccountId());
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
	
	/**跳转到登录界面*/
	private void gotoLogin(){
		SharedPreferences sp = getActivity().getSharedPreferences("token_info", 0);
		boolean isSuccess=sp.edit().clear().commit();
		if(isSuccess){
			SharedPreferences loginSp = getActivity().getSharedPreferences("login_info", 0);
			loginSp.edit().putString("username", phoneStr).commit();
			Intent i = new Intent(getActivity(), LoginActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        	getActivity().startActivity(i);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(time!=null){
			time.cancel();
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.kzx_change_phone_dialog_fragment, container, false);
		return view;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}
}
