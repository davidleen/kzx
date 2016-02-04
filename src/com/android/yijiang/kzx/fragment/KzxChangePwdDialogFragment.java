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
 * 修改密码弹出框
 * @title com.yijiang.jiangdou.fragment
 * @date 2014年5月7日
 * @author tanke
 */
public class KzxChangePwdDialogFragment extends DialogFragment {

	private Dialog dialog;
	private EditText contentTv;
	private Button btnSave;
	private ImageButton closeBtn;

	private InputMethodManager mInputMethodManager;
	private AsyncHttpClient asyncHttpClient;
	
	public static KzxChangePwdDialogFragment newInstance() {
		KzxChangePwdDialogFragment fragment = new KzxChangePwdDialogFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		contentTv = (EditText) view.findViewById(R.id.contentTv);
		btnSave = (Button) view.findViewById(R.id.btnSave);
		closeBtn=(ImageButton)view.findViewById(R.id.closeBtn);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String reason=contentTv.getText().toString();
				if(!StringUtils.isEmpty(reason)){
					postChangePwd(reason);
				}
			}
		});
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mInputMethodManager.hideSoftInputFromWindow(contentTv.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
				dismiss();
			}
		});
		contentTv.requestFocus(); // EditText获得焦点
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // 显示软键盘
	}

	/**修改密码*/ 
	private void postChangePwd(String password){
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("password", password);
		rp.put("phone", getActivity().getSharedPreferences("login_info", 0).getString("username", ""));
		asyncHttpClient.post(getActivity(), Constants.uploadAccountAPI, rp, responseHandler);
	}
	
	/**修改密码回调*/
	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			showProgressDialog();
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
				String data = new JSONObject(content).optString("data");
				if (success) {
					new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(message).setTitle(R.string.prompt).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
							gotoLogin();
						}
					}).create().show();
				}else{
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
	};
	
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
			loginSp.edit().putString("password", contentTv.getText().toString()).commit();
			Intent i = new Intent(getActivity(), LoginActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        	getActivity().startActivity(i);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.kzx_change_pwd_dialog_fragment, container, false);
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
