package com.android.yijiang.kzx.ui;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.R.anim;
import com.android.yijiang.kzx.R.id;
import com.android.yijiang.kzx.R.layout;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.PersistentCookieStore;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.ImageUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.SecretTextView;
import com.android.yijiang.kzx.widget.fileselector.FileSelectionActivity;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ViewFlipper;

import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.BinaryHttpResponseHandler;

/**
 * 登录界面
 * 
 * @title com.android.ticket.web.ui
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class LoginActivity extends FragmentActivity {

	private String TAG = LoginActivity.class.getName();

	private Button btnLogin;
	private TextView no_register_in;
	private TextView forgetPwd;

	private AsyncHttpClient asyncHttpClient;
	private PersistentCookieStore myCookieStore;
	private Dialog dialog;

	private EditText userTv;
	private EditText pwdTv;
	private EditText codeTv;
	private ImageView codeIv;
	private ImageView codeDivider;
	private LinearLayout codeLinear;
	
	private String JSESSIONID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);

		setContentView(R.layout.kzx_login_fragment);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		userTv = (EditText) findViewById(R.id.userTv);
		pwdTv = (EditText) findViewById(R.id.pwdTv);
		codeTv = (EditText) findViewById(R.id.codeTv);
		codeIv = (ImageView) findViewById(R.id.codeIv);
		codeDivider = (ImageView) findViewById(R.id.codeDivider);
		codeLinear = (LinearLayout) findViewById(R.id.codeLinear);
		no_register_in = (TextView) findViewById(R.id.no_register_in);
		forgetPwd = (TextView) findViewById(R.id.forgetPwd);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loginDone();
			}
		});
		// 免注册登录
		no_register_in.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(v.getContext(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "register");
				startActivity(mIntent);
			}
		});
		// 找回密码
		forgetPwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(v.getContext(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "get_pwd");
				startActivity(mIntent);
			}
		});
		pwdTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					loginDone();
				}
				return true;
			}
		});
		codeTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					loginDone();
				}
				return true;
			}
		});
		// 设置用户默认
		userTv.setText(getSharedPreferences("login_info", 0).getString("username", ""));
		pwdTv.setText(getSharedPreferences("login_info", 0).getString("password", ""));
		myCookieStore = new PersistentCookieStore(this);// 設置Cookie
		myCookieStore.clear();
		
	}

	/**登录请求*/ 
	private void loginDone() {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		String username = userTv.getText().toString();// 账号
		String password = pwdTv.getText().toString();// 密码
		String code = codeTv.getText().toString();// 验证码
		if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
			postLogin(username, password,code);
		}
	}

	/**登录*/ 
	private void postLogin(final String username, final String password,final String code) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("username", username);
		rp.put("password", password);
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.setCookieStore(myCookieStore);
		if(!StringUtils.isEmpty(JSESSIONID)){
			rp.put("code", code);
			asyncHttpClient.addHeader("Cookie", "JSESSIONID="+JSESSIONID);
		}
		asyncHttpClient.post(this, Constants.loginAPI, rp, responseHandler);
	}

	/**登录回调*/ 
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			dialog = new Dialog(LoginActivity.this, R.style.mystyle);
			dialog.setContentView(R.layout.loading_dialog);
			dialog.setCancelable(true);
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					asyncHttpClient.cancelRequests(LoginActivity.this, true);
				}
			});
			dialog.show();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (!success) {
					MsgTools.toast(LoginActivity.this, message, Toast.LENGTH_SHORT);
					if (new JSONObject(data).optBoolean("needRegister", false)) {
						String username = userTv.getText().toString();
						Intent mIntent = new Intent(LoginActivity.this, ContentFragmentActivity.class);
						mIntent.putExtra("action", "register");
						mIntent.putExtra("phone", username);
						startActivity(mIntent); 
					}
					// 输入3次错误提供验证码
					if (new JSONObject(data).optBoolean("needCode", false)) {
						showPasswordCode();
					}
				} else {
					SharedPreferences sp = getSharedPreferences("token_info", 0);
					// 成功记录登录验证信息ACCESS_KEY和ACCESS_TOKEN
					if (!StringUtils.isNullOrEmpty(myCookieStore.getCookies())) {
						for (Cookie c : myCookieStore.getCookies()) {
							if (c.getName().equals(Constants.ACCESS_KEY)) {
								sp.edit().putString("ACCESS_KEY", c.getValue()).commit();
							} else if (c.getName().equals(Constants.ACCESS_TOKEN)) {
								sp.edit().putString("ACCESS_TOKEN", c.getValue()).commit();
							}
						}
						String username = userTv.getText().toString();
						String password = pwdTv.getText().toString();
						SharedPreferences loginSp = getSharedPreferences("login_info", 0);
						loginSp.edit().putString("username", username).commit();
						loginSp.edit().putString("password", password).commit();
						// 跳转到主页面
						Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(mIntent);
					} else {
						MsgTools.toast(LoginActivity.this, getString(R.string.request_error), 3000);
					}
				}
			} catch (Exception e) {
				MsgTools.toast(LoginActivity.this, getString(R.string.request_error), 3000);
			}
		}

		private void showPasswordCode() {
			codeDivider.setVisibility(View.VISIBLE);
			codeLinear.setVisibility(View.VISIBLE);
			codeIv.setOnClickListener(getCodeListener);
			getPasswordCode();
		}

		private void getPasswordCode() {
			codeTv.setText("");
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.setCookieStore(myCookieStore);
			asyncHttpClient.get(LoginActivity.this, Constants.validatecodeAPI, new BinaryHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
					Bitmap mBitmap=ImageUtils.byteToBitmap(binaryData);
					int width=mBitmap.getWidth();
					int height=mBitmap.getHeight();
					codeIv.getLayoutParams().width=width*2-10;
					codeIv.getLayoutParams().height=height*2-10;
					codeIv.setImageBitmap(mBitmap);
					for (Cookie c : myCookieStore.getCookies()) {
						if (c.getName().equals(Constants.JSESSIONID)) {
							JSESSIONID=c.getValue();
						}  
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
					
				}
			});
			
		}
		
		OnClickListener getCodeListener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPasswordCode();
			}
		};

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			if (error instanceof UnknownHostException) {
				MsgTools.toast(LoginActivity.this, getString(R.string.request_network_error), 3000);
			} else if (error instanceof HttpResponseException) {
				MsgTools.toast(LoginActivity.this, getString(R.string.request_error), 3000);
			} else if (error instanceof SocketTimeoutException) {
				MsgTools.toast(LoginActivity.this, getString(R.string.request_timeout), 3000);
			} else {
				MsgTools.toast(LoginActivity.this, getString(R.string.request_error), 3000);
			}
		}

		@Override
		public void onFinish() {
			super.onFinish();
			dialog.dismiss();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (asyncHttpClient != null) {
			asyncHttpClient.cancelRequests(this, true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getApplicationContext());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 移除Activity
			AppManager.getAppManager().finishActivity(this);
			AppManager.AppExit(this);
		}
		return super.onKeyDown(keyCode, event);
	}

}
