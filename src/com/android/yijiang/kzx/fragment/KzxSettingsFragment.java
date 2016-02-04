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
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ApkVersionHelper;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.AppConstants;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 设置
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxSettingsFragment extends BaseFragment {

	private TextView aboutBtn;
	private TextView termBtn;
	private LinearLayout versionBtn;
	private TextView versionTv;
	private TextView changePwdBtn;
	private TextView clearCacheBtn;
	private CheckBox noticeSwitch;
	private CheckBox smsSwitch;

	private String TAG = KzxSettingsFragment.class.getName();
	private AsyncHttpClient asyncHttpClient;
	private Button logoutBtn;
	private Dialog dialog;

	private ImageButton btn_list;
	private ImageView stateTv;

	public static KzxSettingsFragment newInstance() {
		KzxSettingsFragment fragment = new KzxSettingsFragment();
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
		View contentView = inflater.inflate(R.layout.kzx_settings_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		stateTv = (ImageView) view.findViewById(R.id.stateTv);
		if (!StringUtils.isEmpty(getContext().getKzxTokenBean().getNoReadMessageNum())) {
			stateTv.setVisibility(Integer.valueOf(getContext().getKzxTokenBean().getNoReadMessageNum()) > 0 ? View.VISIBLE : View.GONE);
		} else {
			stateTv.setVisibility(View.GONE);
		}
		noticeSwitch = (CheckBox) view.findViewById(R.id.noticeSwitch);
		smsSwitch = (CheckBox) view.findViewById(R.id.smsSwitch);
		versionBtn = (LinearLayout) view.findViewById(R.id.versionBtn);
		versionTv = (TextView) view.findViewById(R.id.versionTv);
		changePwdBtn = (TextView) view.findViewById(R.id.changePwdBtn);
		clearCacheBtn= (TextView) view.findViewById(R.id.clearCacheBtn);
		aboutBtn = (TextView) view.findViewById(R.id.aboutBtn);
		termBtn = (TextView) view.findViewById(R.id.termBtn);
		logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
		aboutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(v.getContext(), SingleFragmentActivity.class);
				mIntent.putExtra("action", "about");
				startActivity(mIntent);
			}
		});
		clearCacheBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Picasso.with(getActivity()).clearCache();
				ImageLoader.getInstance().clearDiscCache();
				ImageLoader.getInstance().clearDiskCache();
				ImageLoader.getInstance().clearMemoryCache();
				MsgTools.toast(getActivity(), getString(R.string.settings_clear_cache_success_hint), Toast.LENGTH_SHORT);
			}
		});
		termBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(v.getContext(), SingleFragmentActivity.class);
				mIntent.putExtra("action", "term_settings");
				startActivity(mIntent);
			}
		});
		changePwdBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment myFragment = KzxChangePwdDialogFragment.newInstance();
				myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
				myFragment.show(getFragmentManager(), TAG);
			}
		});
		// 注销
		logoutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(R.string.alert_dialog_logout).setTitle(R.string.prompt).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						gotoLogin();
					}
				}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create().show();
			}
		});
		btn_list = (ImageButton) view.findViewById(R.id.btn_list);
		btn_list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity().getPackageName() + ".HOME_RECEIVED_ACTION");
				mIntent.putExtra("action", "close_drawer");
				getActivity().sendBroadcast(mIntent);
			}
		});
		versionTv.setText("v" + ApkVersionHelper.getInstalledApkVersion(getActivity().getPackageName()));
		versionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ApkVersionHelper apkVersionHelper = new ApkVersionHelper(getActivity());
				apkVersionHelper.checkInstalledApkVersion(true);
			}
		});
		// 推送的开关
		final SharedPreferences sp = getActivity().getSharedPreferences("push_info", 0);
		noticeSwitch.setChecked(sp.getBoolean("push", true));
		noticeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					JPushInterface.resumePush(getActivity());
				} else {
					JPushInterface.stopPush(getActivity());
				}
				sp.edit().putBoolean("push", isChecked).commit();
			}
		});
		smsSwitch.setChecked(getContext().getKzxTokenBean().getIsSendSms() == 0 ? false : true);
		smsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					postControlSms(1);
				} else {
					postControlSms(0);
				}
			}
		});
	}

	/** 弹出进度框 */
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

	/** 短信开关 */
	private void postControlSms(final int isSendSms) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("isSendSms", isSendSms);
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.uploadAccountAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				showProgressDialog();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String content = new String(responseBody);
				try {
					String message = new JSONObject(content).optString("message");
					boolean success = new JSONObject(content).optBoolean("success");
					final SharedPreferences kzxSp = getActivity().getSharedPreferences("kzx_token_info", 0);
					if (success) {
						kzxSp.edit().putInt("isSendSms", isSendSms).commit();
					} else {
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

	/** 跳转到登录界面 */
	private void gotoLogin() {
		SharedPreferences sp = getActivity().getSharedPreferences("token_info", 0);
		boolean isSuccess = sp.edit().clear().commit();
		if (isSuccess) {
			SharedPreferences loginSp = getActivity().getSharedPreferences("login_info", 0);
			loginSp.edit().clear().commit();
			SharedPreferences TAG_ALIAS = getActivity().getSharedPreferences("TAG_ALIAS", 0);
			TAG_ALIAS.edit().clear().commit();
			JPushInterface.clearAllNotifications(getActivity());
			JPushInterface.stopPush(getActivity());
			Intent i = new Intent(getActivity(), LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			getActivity().startActivity(i);
		}
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
