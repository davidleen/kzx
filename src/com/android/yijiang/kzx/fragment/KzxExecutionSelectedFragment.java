package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

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
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
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
import com.android.yijiang.kzx.adapter.KzxCopySelectedAdapter;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxExecutionSelectedAdapter;
import com.android.yijiang.kzx.adapter.KzxIncentiveAdapter;
import com.android.yijiang.kzx.adapter.KzxInvitePartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.bean.Contacts;
import com.android.yijiang.kzx.bean.KzxTokenBean;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.MemberAddBean;
import com.android.yijiang.kzx.bean.MemberBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.fragment.KzxGetPwdFragment.TimeCount;
import com.android.yijiang.kzx.fragment.KzxInviteFragment.ReadContactTask;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringMatcher;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.AppManager;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.IndexableListView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 选择执行人
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxExecutionSelectedFragment extends BaseFragment {

	private String TAG = KzxExecutionSelectedFragment.class.getName();

	private boolean mHasRequestedMore;
	private long pageNow = 1;
	private View footerView;

	private ListView dateCustomList;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;
	private TextView doneBtn;// 选中事件

	private HashMap<String, LeaderBean> isLeaderHash;
	private Gson gson;
	private String typeStr;
	private Type tt;
	private AsyncHttpClient asyncHttpClient;
	private KzxExecutionSelectedAdapter kzxExecutionSelectedAdapter;

	// 通讯录
	private static HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();
	private IndexableListView dateContactList;
	private Dialog dialog;
	private ReadContactTask readContactTask;
	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };

	private static final int PHONES_DISPLAY_NAME_INDEX = 0; // 联系人显示名称
	private static final int PHONES_NUMBER_INDEX = 1; // 电话号码
	private static final int PHONES_CONTACT_ID_INDEX = 2;// 联系人的ID
	private KzxInvitePartnerAdapter kzxInvitePartnerAdapter;

	// 头部View
	private ImageButton backBtn;
	private ImageButton addExecutionBtn;
	private EditText searchEt;
	
	private MessageReceiver mMessageReceiver;

	public static KzxExecutionSelectedFragment newInstance(HashMap<String, LeaderBean> isLeaderHash, String typeStr) {
		KzxExecutionSelectedFragment fragment = new KzxExecutionSelectedFragment();
		Bundle bundle = new Bundle();
		bundle.putString("typeStr", typeStr);
		bundle.putSerializable("isLeaderHash", isLeaderHash);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLeaderHash = (HashMap<String, LeaderBean>) getArguments().getSerializable("isLeaderHash");
		typeStr = getArguments().getString("typeStr");
		gson = new GsonBuilder().create();
		tt = new TypeToken<List<LeaderBean>>() { }.getType();
		// 索引首字母格式
		spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_execution_selected_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		footerView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer, null);
		footerView.setVisibility(View.INVISIBLE);
		kzxInvitePartnerAdapter = new KzxInvitePartnerAdapter(getActivity(), "task");
		kzxExecutionSelectedAdapter = new KzxExecutionSelectedAdapter(getActivity(), typeStr);
		addExecutionBtn = (ImageButton) view.findViewById(R.id.addExecutionBtn);
		searchEt = (EditText) view.findViewById(R.id.searchEt);
		dateCustomList = (ListView) view.findViewById(R.id.dateCustomList);
		dateContactList = (IndexableListView) view.findViewById(R.id.dateContactList);
		dateContactList.setFastScrollEnabled(true);
		dateContactList.setAdapter(kzxInvitePartnerAdapter);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		doneBtn = (TextView) view.findViewById(R.id.doneBtn);
		dateCustomList.addFooterView(footerView, null, false);
		dateCustomList.setAdapter(kzxExecutionSelectedAdapter);
		dateCustomList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!mHasRequestedMore) {
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if (lastInScreen >= totalItemCount && totalItemCount != 0 && totalItemCount != dateCustomList.getHeaderViewsCount() + dateCustomList.getFooterViewsCount() && kzxExecutionSelectedAdapter.getCount() > 0) {
						postLoad(false);
						mHasRequestedMore = true;
					}
				}
			}
		});
		// 选中确定
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, LeaderBean> isSelected = kzxExecutionSelectedAdapter.getIsSelected();
				if (StringUtils.isNullOrEmpty(isSelected)) {
					MsgTools.toast(getActivity(), getString(R.string.please_selected), ResourceMap.LENGTH_SHORT);
					return;
				} else {
					Intent mIntent = new Intent(getActivity().getPackageName() + ".ADD_TASK_RECEIVED_ACTION");
					mIntent.putExtra("data", isSelected);
					mIntent.putExtra("action", "execution_selected");
					getActivity().sendBroadcast(mIntent);
					getActivity().finish();
				}
			}
		});
		// 返回上一級
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				getActivity().finish();
			}
		});
		searchEt.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEARCH) {
					((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					kzxExecutionSelectedAdapter.clearDataForLoader();
					pageNow = 1;
					postLoad(true);
				}
				return true;
			}
		});
		addExecutionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dateCustomList.getVisibility() == View.VISIBLE) {
					doneBtn.setVisibility(View.GONE);
					dateCustomList.setVisibility(View.GONE);
					dateContactList.setVisibility(View.VISIBLE);
					addExecutionBtn.setColorFilter(Color.parseColor("#397bc5"));
					if (kzxInvitePartnerAdapter.getCount() == 0) {
						// 开始检索通讯录
						readContactTask = new ReadContactTask();
						readContactTask.execute();
						postLoadMaxMember();
					}
				} else {
					doneBtn.setVisibility(View.VISIBLE);
					addExecutionBtn.setColorFilter(Color.parseColor("#00000000"));
					dateCustomList.setVisibility(View.VISIBLE);
					dateContactList.setVisibility(View.GONE);
				}
				searchEt.setText("");
			}
		});
		kzxInvitePartnerAdapter.setOnItemClickListener(new KzxInvitePartnerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, final Contacts contacts) {
				new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(R.string.alert_dialog_invite).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						postAddMember(contacts.getPhoneNumber().replaceAll(" ", "").replaceAll("-", "").replaceAll("[+]86", ""), contacts.getName());
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create().show();
			}
		});
		dateContactList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final Contacts contacts = (Contacts) parent.getItemAtPosition(position);
				new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(R.string.alert_dialog_invite).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						postAddMember(contacts.getPhoneNumber().replaceAll(" ", "").replaceAll("[+]86", ""), contacts.getName());
						dialog.dismiss();
					}
				}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create().show();
			}
		});
		// 搜索框实时搜索
		searchEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (dateContactList.getVisibility() == View.VISIBLE) {
					kzxInvitePartnerAdapter.getFilter().filter(s);
				}
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
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoad(true);
		registerMessageReceiver();
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

	/** 读取通讯录 */
	class ReadContactTask extends AsyncTask<Integer, Integer, List<Contacts>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			default_load_view.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<Contacts> doInBackground(Integer... params) {
			List<Contacts> contactsList = readAllContacts();
			return contactsList;
		}

		@Override
		protected void onPostExecute(List<Contacts> contactsList) {
			super.onPostExecute(contactsList);
			kzxInvitePartnerAdapter.setDataForLoad(contactsList);
			default_load_view.setVisibility(View.GONE);
		}

	}

	/** 弹出框提醒 */
	private void showMessageDialog(String message) {
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(message).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).create().show();
	}

	/** 成员邀请 */
	private void postAddMember(final String phone, final String name) {
		KzxTokenBean kzxTokenBean = getContext().getKzxTokenBean();
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("phone", phone);
		rp.put("name", name);
		if (kzxTokenBean != null && !StringUtils.isEmpty(kzxTokenBean.getEncryptMemberId())) {
			rp.put("leader", kzxTokenBean.getEncryptMemberId());
		}
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.addMemberAPI, rp, responseHandler);
	}

	/** 成员邀请回调 */
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
				final String data = new JSONObject(content).optString("data");
				if (success) {
					new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(message).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							MemberAddBean memberAddBean = new Gson().fromJson(data, MemberAddBean.class);
							HashMap<String, LeaderBean> isSelected = new HashMap<String, LeaderBean>();
							LeaderBean leaderBean = new LeaderBean();
							leaderBean.setDepartment(memberAddBean.getDepartment());
							leaderBean.setIcon(memberAddBean.getIcon());
							leaderBean.setId(memberAddBean.getId());
							leaderBean.setName(memberAddBean.getName());
							isSelected.put(memberAddBean.getId(), leaderBean);
							Intent mIntent = new Intent(getActivity().getPackageName() + ".ADD_TASK_RECEIVED_ACTION");
							mIntent.putExtra("data", isSelected);
							mIntent.putExtra("action", "execution_selected");
							getActivity().sendBroadcast(mIntent);
							getActivity().finish();
							dialog.dismiss();
						}
					}).create().show();
				} else if(!success&&"1".equals(data)){
					showAcceptanceDialog();
				} else {
					showMessageDialog(message);
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
	
	/**弹出首次验证Dialog*/ 
	private TimeCount time;
	private EditText codeTv;
	private Dialog alertDialog;
	private void showAcceptanceDialog() {
		SharedPreferences sp = getActivity().getSharedPreferences("kzx_token_info", 0);
		final String phone=getContext().getKzxTokenBean().getPhone();
		final View contentView = getActivity().getLayoutInflater().inflate(R.layout.kzx_first_validate_dialog_fragment, null);
		final EditText nameTv = (EditText) contentView.findViewById(R.id.nameTv);
		codeTv = (EditText) contentView.findViewById(R.id.codeTv);
		final EditText phoneTv=(EditText)contentView.findViewById(R.id.phoneTv);
		final TextView codeBtn=(TextView)contentView.findViewById(R.id.codeBtn);
		final TextView btnSure=(TextView)contentView.findViewById(R.id.btnSure);
		time = new TimeCount(60000, 1000,codeBtn);// 构造CountDownTimer对象
		time.start();
		codeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone=getContext().getKzxTokenBean().getPhone();
				if(!StringUtils.isEmpty(phone)){
					postSmsCode(phone);
				}
			}
		});
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
				dialog.dismiss();
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
	

	/** 进度弹出框 */
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

	/** 请求执行人数据 */
	private void postLoad(final boolean isFirstLoad) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageNow", pageNow + "");// 页码
		rp.put("searchContent", searchEt.getText().toString());
		asyncHttpClient.post(getActivity(), Constants.queryAllByLeaderAPI, rp, executionResponseHandler);
	}

	/** 请求执行人数据回调 */
	AsyncHttpResponseHandler executionResponseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			super.onStart();
			if (kzxExecutionSelectedAdapter.isEmpty()) {
				footerView.setVisibility(View.GONE);
				default_load_view.setVisibility(View.VISIBLE);
			} else {
				footerView.setVisibility(View.VISIBLE);
				((ProgressBar) footerView.findViewById(R.id.default_load)).setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onFinish() {
			default_load_view.setVisibility(View.GONE);
			((ProgressBar) footerView.findViewById(R.id.default_load)).setVisibility(View.INVISIBLE);
			super.onFinish();
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				long pageSize = new JSONObject(content).optLong("pageSize", 0);
				long rowCount = new JSONObject(content).optLong("rowCount", 0);
				long totalPage = (rowCount + pageSize - 1) / pageSize;
				String records = new JSONObject(content).optString("records");
				if (!StringUtils.isEmpty(records) && !"[]".equals(records)) {
					List<LeaderBean> leaderList = new ArrayList<LeaderBean>();
					leaderList.addAll((List<LeaderBean>) gson.fromJson(records, tt));
					kzxExecutionSelectedAdapter.setDataForLoader(leaderList, isLeaderHash);
					if (pageNow == totalPage) {
						// 最后一页
						footerView.setVisibility(View.VISIBLE);
						mHasRequestedMore = true;
						((ProgressBar) footerView.findViewById(R.id.default_load)).setVisibility(View.INVISIBLE);
						//只有一条数据的时候提醒邀请新成员
						if(kzxExecutionSelectedAdapter.getCount()==1){
							showNotifyMessage();
						}
					} else {
						// 新增页码
						footerView.setVisibility(View.INVISIBLE);
						pageNow++;
						mHasRequestedMore = false;
					}
				} else {
					// 空数据
					dateCustomList.setEmptyView(img_empty_feed);
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
	
	/**邀请新成员提醒**/
	private void showNotifyMessage(){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
		.setIcon(null)
		.setMessage(R.string.alert_dialog_invite_2)
		.setTitle(R.string.prompt).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				doneBtn.setVisibility(View.GONE);
				dateCustomList.setVisibility(View.GONE);
				dateContactList.setVisibility(View.VISIBLE);
				addExecutionBtn.setColorFilter(Color.parseColor("#397bc5"));
				if (kzxInvitePartnerAdapter.getCount() == 0) {
					// 开始检索通讯录
					readContactTask = new ReadContactTask();
					readContactTask.execute();
					postLoadMaxMember();
				}
				dialog.dismiss();
			}
		}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).create().show();
	}
	
	/** 获取所有全体成员 */
	private void postLoadMaxMember() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageSize", Integer.MAX_VALUE);// 直接获取最大成员数
		asyncHttpClient.post(getActivity(), Constants.queryAllByCompanyAPI, rp, mamberResponseHandler);
	}

	/** 获取所有全体成员回调 */
	AsyncHttpResponseHandler mamberResponseHandler = new AsyncHttpResponseHandler() {
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
	
	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}
	
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
		if (mMessageReceiver != null) {
			getActivity().unregisterReceiver(mMessageReceiver);
		}
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
		if (readContactTask != null && readContactTask.getStatus() != AsyncTask.Status.FINISHED)
			readContactTask.cancel(true);
	}

}
