package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.android.yijiang.kzx.adapter.KzxInvitePartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxMessageAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicInfoAdapter;
import com.android.yijiang.kzx.bean.Contacts;
import com.android.yijiang.kzx.bean.KzxTokenBean;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.MemberAddBean;
import com.android.yijiang.kzx.bean.MemberBean;
import com.android.yijiang.kzx.fragment.KzxExecutionSelectedFragment.TimeCount;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
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
import com.android.yijiang.kzx.widget.IndexableListView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 邀请新成员
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxInviteFragment extends BaseFragment {

	private String TAG = KzxInviteFragment.class.getName();
	private IndexableListView dateCustomList;
	private ReadContactTask readContactTask;
	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };

	private static final int PHONES_DISPLAY_NAME_INDEX = 0; // 联系人显示名称
	private static final int PHONES_NUMBER_INDEX = 1; // 电话号码
	private static final int PHONES_CONTACT_ID_INDEX = 2;// 联系人的ID

	private HashMap<String, LeaderBean> isLeaderHash;// 汇报人数据
	private AsyncHttpClient asyncHttpClient;
	private KzxInvitePartnerAdapter kzxInvitePartnerAdapter;
	private MessageReceiver mMessageReceiver;
	private MessageReceiver2 mMessageReceiver2;

	// 头部View
	private LinearLayout reportBtn;
	private ImageButton backBtn;
	private TextView reportNameTv;// 汇报人
	private EditText searchEt;

	private Dialog dialog;
	private String leaderId;// 汇报人ID

	public static KzxInviteFragment newInstance() {
		KzxInviteFragment fragment = new KzxInviteFragment();
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
		View contentView = inflater.inflate(R.layout.kzx_invite_partner_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		reportBtn = (LinearLayout) view.findViewById(R.id.reportBtn);
		reportNameTv = (TextView) view.findViewById(R.id.reportNameTv);
		searchEt = (EditText) view.findViewById(R.id.searchEt);
		kzxInvitePartnerAdapter = new KzxInvitePartnerAdapter(getActivity(), "partner");
		dateCustomList = (IndexableListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.setFastScrollEnabled(true);
		dateCustomList.setAdapter(kzxInvitePartnerAdapter);
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		kzxInvitePartnerAdapter.setOnItemClickListener(new KzxInvitePartnerAdapter.OnItemClickListener() {
			public void onItemClick(int position, Contacts contacts) {
				postAddMember(contacts.getPhoneNumber().replaceAll(" ", "").replaceAll("-", "").replaceAll("[+]86", ""), contacts.getName(), leaderId);
			}
		});
		// 搜索框实时搜索
		searchEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				kzxInvitePartnerAdapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		// 选择汇报人
		reportBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isLeaderHash);
				mIntent.putExtra("action", "report_selected");
				startActivity(mIntent);
			}
		});
		// 初始化默认汇报人
		initReportData(reportNameTv);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		readContactTask = new ReadContactTask();
		readContactTask.execute();
		registerMessageReceiver2();
	}

	private void initReportData(TextView reportNameTv) {
		KzxTokenBean kzxTokenBean = getContext().getKzxTokenBean();
		if (kzxTokenBean != null) {
			leaderId = kzxTokenBean.getEncryptMemberId();
			reportNameTv.setText(kzxTokenBean.getMemberName());
		}
		postLoadMaxMember();
		registerMessageReceiver();
	}

	/** 成员邀请 */
	private void postAddMember(final String phone, final String name, final String leader) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("phone", phone);
		rp.put("name", name);
		if (!StringUtils.isEmpty(leader)) {
			rp.put("leader", leader);
		}
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.addMemberAPI, rp, new AsyncHttpResponseHandler() {
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
					if (success) {
						MsgTools.toast(getActivity(), message, Toast.LENGTH_LONG);
						MemberBean memberBean = new MemberBean();
						memberBean.setPhone(phone);
						memberBean.setState(1);
						kzxInvitePartnerAdapter.setDataForOneMember(memberBean);
					}else if(!success&&"1".equals(data)){
						showAcceptanceDialog();
					} else {
						showMessageDialog(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
					MsgTools.toast(getActivity(), getString(R.string.request_error), Toast.LENGTH_SHORT);
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

	/**弹出首次验证Dialog*/ 
	private TimeCount time;
	private EditText codeTv;
	private Dialog alertDialog;
	private void showAcceptanceDialog() {
		final String phone=getContext().getKzxTokenBean().getPhone();
		final View contentView = getActivity().getLayoutInflater().inflate(R.layout.kzx_first_validate_dialog_fragment, null);
		final EditText nameTv = (EditText) contentView.findViewById(R.id.nameTv);
		codeTv = (EditText) contentView.findViewById(R.id.codeTv);
		final EditText phoneTv=(EditText)contentView.findViewById(R.id.phoneTv);
		final TextView codeBtn=(TextView)contentView.findViewById(R.id.codeBtn);
		final TextView btnSure=(TextView)contentView.findViewById(R.id.btnSure);
		time = new TimeCount(60000, 1000,codeBtn);// 构造CountDownTimer对象
		time.start();
		phoneTv.setText(phone);
		alertDialog=new Dialog(getActivity(),R.style.mystyle);
		alertDialog.setContentView(contentView);
		alertDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nameTvStr = nameTv.getText().toString();
				String codeTvStr = codeTv.getText().toString();
				if (StringUtils.isEmpty(nameTvStr)) {
					MsgTools.toast(getActivity(), getString(R.string.name_edit_hint), Toast.LENGTH_SHORT);
					return;
				}
				if (StringUtils.isEmpty(codeTvStr)) {
					MsgTools.toast(getActivity(), getString(R.string.input_code_hint), Toast.LENGTH_SHORT);
					return;
				}
				postFirstValidate(codeTvStr,nameTvStr);
			}
		});
		nameTv.requestFocus();
		codeTv.clearFocus();
		nameTv.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!StringUtils.isEmpty(phone)){
					postSmsCode(phone);
				}
				KeyBoardUtils.openKeybord(nameTv, getActivity());
			}
		}, 300);
	}
	
	/** 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		
		private TextView codeBtn;
		
		public TimeCount(long millisInFuture, long countDownInterval,TextView codeBtn) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
			this.codeBtn=codeBtn;
		}
		@Override
		public void onFinish() {// 计时完毕时触发
			codeBtn.setText("重新获取");
			codeBtn.setEnabled(true);
		}
		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			codeBtn.setEnabled(false);
			codeBtn.setText("已发送("+millisUntilFinished / 1000+")");
		}
	}
	
	
	/** 第一次验证邀请 */
	private void postFirstValidate(final String code, final String name) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("code", code);
		rp.put("name", name);
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.phoneFirstValidateAPI, rp, validateResponseHandler);
	}

	/** 第一次验证邀请回调 */
	AsyncHttpResponseHandler validateResponseHandler = new AsyncHttpResponseHandler() {
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
				if(success){
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
					if(codeTv!=null){
						KeyBoardUtils.closeKeybord(codeTv, getActivity());
					}
					alertDialog.dismiss();
				}else{
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
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
		@Override
		public void onFinish() {
			super.onFinish();
			dialog.dismiss();
		}
	};
	
	/** 获取验证码 */
	private void postSmsCode(final String phone) {
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("phone", phone);
		rp.put("type", "3");
		rp.put("accountId", getContext().getKzxTokenBean().getAccountId());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.sendCodeByPhoneAPI, rp, smsResponseHandler);
	}

	/** 获取验证码回调 */
	AsyncHttpResponseHandler smsResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			time.start();
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
	
	/** 弹出进度框 */
	private void showProgressDialog() {
		dialog = new Dialog(getActivity(), R.style.mystyle);
		dialog.setContentView(R.layout.loading_dialog);
		dialog.setCancelable(true);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (asyncHttpClient != null) {
					asyncHttpClient.cancelRequests(getActivity(), true);
				}
			}
		});
		dialog.show();
	}

	/** 弹出框提醒 */
	private void showMessageDialog(String message) {
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(message).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).create().show();
	}

	/** 读取联系人的信息 */
	public List<Contacts> readAllContacts() {
		List<Contacts> contactsList = new ArrayList<Contacts>();
		Cursor phoneCursor = this.getContext().getContentResolver().query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, "sort_key_alt asc");
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				contactsList.add(new Contacts(contactid.toString(), contactName, phoneNumber, null));
			}

			phoneCursor.close();
		}
		return contactsList;
	}

	/**读取通讯录*/
	class ReadContactTask extends AsyncTask<Integer, Integer, List<Contacts>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Contacts> doInBackground(Integer... params) {
			return readAllContacts();
		}

		@Override
		protected void onPostExecute(List<Contacts> contactsList) {
			super.onPostExecute(contactsList);
			kzxInvitePartnerAdapter.setDataForLoad(contactsList);
		}

	}

	/** 获取所有全体成员 */
	private void postLoadMaxMember() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageSize", Integer.MAX_VALUE);// 直接获取最大成员数
		asyncHttpClient.post(getActivity(), Constants.queryAllByCompanyAPI, rp, responseHandler);
	}

	/** 获取所有全体成员回调 */
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
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
				long pageSize = new JSONObject(content).optLong("pageSize", 0);
				long rowCount = new JSONObject(content).optLong("rowCount", 0);
				long totalPage = (rowCount + pageSize - 1) / pageSize;
				String records = new JSONObject(content).optString("records");
				if (!StringUtils.isEmpty(records) && !"[]".equals(records)) {
					List<MemberBean> memberList = new ArrayList<MemberBean>();
					memberList.addAll((List<MemberBean>) new Gson().fromJson(records, new TypeToken<List<MemberBean>>() {}.getType()));
					Map<String, String> memberMap = new HashMap<String, String>();
					for (MemberBean memberBean : memberList) {
						memberMap.put(memberBean.getPhone(), memberBean.getState() + "");
					}
					kzxInvitePartnerAdapter.setDataForMember(memberMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MsgTools.toast(getActivity(), getString(R.string.request_error), 3000);
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

	/**注册广播*/ 
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".INVITE_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	/**回调广播*/ 
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".INVITE_RECEIVED_ACTION").equals(intent.getAction())) {
				String showName = null;
				isLeaderHash = (HashMap<String, LeaderBean>) intent.getSerializableExtra("data");
				for (Map.Entry<String, LeaderBean> entry : isLeaderHash.entrySet()) {
					LeaderBean leaderBean = entry.getValue();
					showName = leaderBean.getName();
					leaderId = leaderBean.getId();
				}
				reportNameTv.setText(showName);
			}
		}
	}
	
	/** 注册广播 */
	public void registerMessageReceiver2() {
		mMessageReceiver2 = new MessageReceiver2();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".REGISTER_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver2, filter);
	}

	/** 回调广播 */
	public class MessageReceiver2 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".REGISTER_RECEIVED_ACTION").equals(intent.getAction())) {
				String smsMessage = intent.getStringExtra("message");
				if (smsMessage.indexOf(getString(R.string.app_name)) != -1) {
					if(codeTv!=null){
						codeTv.setText(smsMessage.substring(0,6));
					}
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(time!=null)
			time.cancel();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
		if (readContactTask != null && readContactTask.getStatus() != AsyncTask.Status.FINISHED)
			readContactTask.cancel(true);
		if (mMessageReceiver != null)
			getActivity().unregisterReceiver(mMessageReceiver);
		if (mMessageReceiver2 != null)
			getActivity().unregisterReceiver(mMessageReceiver2);
	}

}
