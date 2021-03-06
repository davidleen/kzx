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
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toast;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
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
import com.android.yijiang.kzx.ui.LoginActivity;
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
 * 创建团队
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxCreateTeamFragment extends BaseFragment {

	private Button btnSure;
	private EditText teamTv;
	private ImageButton backBtn;
	private Dialog dialog;
	private AsyncHttpClient asyncHttpClient;

	private InputMethodManager mInputMethodManager;
	
	public static KzxCreateTeamFragment newInstance() {
		KzxCreateTeamFragment fragment = new KzxCreateTeamFragment();
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
		View contentView = inflater.inflate(R.layout.kzx_create_team_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// 获取键盘
		mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		backBtn=(ImageButton)view.findViewById(R.id.backBtn);
		btnSure = (Button) view.findViewById(R.id.btnSure);
		teamTv = (EditText) view.findViewById(R.id.teamTv);
		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String teamTvStr = teamTv.getText().toString();
				if (StringUtils.isEmpty(teamTvStr)) {
					return;
				}
				postCreateTeam(teamTvStr);
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftInputMode();
				getActivity().finish();
			}
		});
		teamTv.requestFocus(); // EditText获得焦点
		getActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE); // 显示软键盘
	}
	
	/**关闭键盘*/ 
	private void hideSoftInputMode(){
		mInputMethodManager.hideSoftInputFromWindow(teamTv.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
	}

	/**创建团队*/ 
	private void postCreateTeam(final String name) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("name", name);
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.createCompanyAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				showProgressDialog();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String content = new String(responseBody);
				try {
					boolean success = new JSONObject(content).optBoolean("success", false);
					String message = new JSONObject(content).optString("message");
					if (success) {
						new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
						.setIcon(null)
						.setMessage(message)
						.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								hideSoftInputMode();
								dialog.dismiss();
								successMain();
								//提示
								SharedPreferences sp = getActivity().getSharedPreferences("invite_tip_info", 0);
								sp.edit().putString("isInviteTip", "invite").commit();
							}
						}).create().show();
					} else  {
						MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
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
		});
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
	
	/**成功跳转*/
	private void successMain(){
		Intent i = new Intent(getActivity(), MainActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    	getActivity().startActivity(i);
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
	}

}
