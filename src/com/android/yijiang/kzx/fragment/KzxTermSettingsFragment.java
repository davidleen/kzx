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
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.bean.AmountMemberBean;
import com.android.yijiang.kzx.bean.CompanyInfoBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
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
 * 团队设置
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxTermSettingsFragment extends BaseFragment {

	private EditText teamNameTv;
	private Dialog dialog;
	private AsyncHttpClient asyncHttpClient;
	
	public static KzxTermSettingsFragment newInstance() {
		KzxTermSettingsFragment fragment = new KzxTermSettingsFragment();
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
		View contentView = inflater.inflate(R.layout.kzx_term_settings_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		teamNameTv=(EditText)view.findViewById(R.id.teamNameTv);
		teamNameTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					final String contentStr = v.getText().toString();
					if (!StringUtils.isEmpty(contentStr)) {
						RequestParams rp = new RequestParams();
						rp.put("name", contentStr);
						postUploadAccount(rp,Constants.companyEditAPI);
					}
				}
				return false;
			}
		});
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoad();
	}
	
	/**团队设置加载数据*/ 
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		asyncHttpClient.post(getActivity(), Constants.companyDetailAPI, rp, responseHandler);
	}
	
	/**团队设置上传资料*/
	private void postUploadAccount(RequestParams rp,String path) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), path, rp, accountResponseHandler);
	}

	/**团队设置上传资料回调*/
	AsyncHttpResponseHandler accountResponseHandler=new AsyncHttpResponseHandler() {
		public void onStart() {
			showProgressDialog();
		};
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
					new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(message).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
						}
					}).create().show();
				}else{
					MsgTools.toast(getActivity(), message, Toast.LENGTH_LONG);
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
	
	/**团队名称回调*/
	AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler() {
		public void onStart() {
			super.onStart();
			showProgressDialog();
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
				CompanyInfoBean companyInfoBean=JSON.parseObject(content, CompanyInfoBean.class);
				teamNameTv.setText(companyInfoBean.getName());
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
	
	/**弹出进度框*/
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

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
