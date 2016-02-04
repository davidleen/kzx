package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import android.text.Editable;
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

import cn.jpush.android.api.JPushInterface;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxMessageAdapter;
import com.android.yijiang.kzx.adapter.KzxMessageAmountAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicInfoAdapter;
import com.android.yijiang.kzx.bean.AmountMessageBean;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MessageAmountBean;
import com.android.yijiang.kzx.bean.MessageBean;
import com.android.yijiang.kzx.bean.MessageBoxBean;
import com.android.yijiang.kzx.bean.TargetDutyersBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.AppManager;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
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
 * 我的账号消息
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxMessageAmountFragment extends BaseFragment {

	private String TAG = KzxMessageAmountFragment.class.getName();
	private SwipeRefreshLayout swipeLayout;
	private ListView dateCustomList;
	private TextView currentTitle;
	private boolean mHasRequestedMore;
	private long pageNow=1;
	private View footerView;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;
	private EditText searchEt;
	
	private int position=0;
	private MessageBean messageBean;
	private AsyncHttpClient asyncHttpClient;
	private KzxMessageAmountAdapter kzxMessageAdapter;

	private static final String TASK_TAG="task";
	private static final String NOTICE_TAG="notice";
	private static final String MEDAL_TAG="medal";
	private static final String INDEX_TAG="index";
	private static final String TARGET_TAG="target";
	
	// 头部View
	private ImageButton backBtn;

	public static KzxMessageAmountFragment newInstance(MessageBean messageBean,int position) {
		KzxMessageAmountFragment fragment = new KzxMessageAmountFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("messageBean", messageBean);
		bundle.putInt("posItion", position);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		messageBean=(MessageBean) getArguments().getSerializable("messageBean");
		position=getArguments().getInt("posItion", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_message_amount_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		searchEt= (EditText) view.findViewById(R.id.searchEt);
		footerView=getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer, null);
		footerView.setVisibility(View.INVISIBLE);
		currentTitle=(TextView) view.findViewById(R.id.currentTitle);
		currentTitle.setText(getString(R.string.name_message,messageBean.getName()));
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				pageNow=1;
				postLoad();
			}
		});
		kzxMessageAdapter = new KzxMessageAmountAdapter(getActivity(),messageBean);
		dateCustomList = (ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.addFooterView(footerView, null, false);
		dateCustomList.setAdapter(kzxMessageAdapter);
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KeyBoardUtils.closeKeybord(searchEt, getActivity());
				getActivity().finish();
			}
		});
		kzxMessageAdapter.setOnItemClickListener(new KzxMessageAmountAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, MessageAmountBean messageAmountBean,int typeCurrent) {
				switch (typeCurrent) {
				case KzxMessageAmountAdapter.VALUE_RIGHT_TEXT:
					postAcceptInvite(messageAmountBean);
					break;
				case KzxMessageAmountAdapter.VALUE_LEFT_TEXT:
					break;
				case KzxMessageAmountAdapter.VALUE_TEXT:
					gotoMessageInfo(messageAmountBean);
					break;
				default:
					break;
				}
			}
		});
		dateCustomList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!mHasRequestedMore) {
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if (lastInScreen >= totalItemCount && totalItemCount != 0 && totalItemCount != dateCustomList.getHeaderViewsCount() + dateCustomList.getFooterViewsCount() && kzxMessageAdapter.getCount() > 0) {
						postLoad();
						mHasRequestedMore = true;
					}
				}
			}
		});
		searchEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				kzxMessageAdapter.getFilter().filter(s);
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
		searchEt.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEARCH) {
					final String contentStr = v.getText().toString();
					KeyBoardUtils.closeKeybord(searchEt, getActivity());
					kzxMessageAdapter.getFilter().filter(contentStr);
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
	
	/**关闭页面传递未读数u*/
	private void closeFragment(){
		Intent mIntent=new Intent(getActivity().getPackageName()+".MESSAGE_RECEIVED_ACTION");
		mIntent.putExtra("position", position);
		mIntent.putExtra("readNum", kzxMessageAdapter.getReadNum());
		getActivity().sendBroadcast(mIntent);
	}
	
	/**点击气泡跳转到指定页面*/
	private void gotoMessageInfo(MessageAmountBean messageAmountBean){
		if(StringUtils.isEmpty(messageAmountBean.getContent().getAction())) return;
		if(TASK_TAG.equals(messageAmountBean.getContent().getAction())){
			Intent i = new Intent(getActivity(), ContentFragmentActivity.class);
        	i.putExtra("action","task_detail");
        	i.putExtra("type", "message_amount");
    		i.putExtra("taskId", messageAmountBean.getContent().getActionParam());
        	getActivity().startActivity(i);
		}else if(NOTICE_TAG.equals(messageAmountBean.getContent().getAction())){
			Intent i = new Intent(getActivity(), ContentFragmentActivity.class);
        	i.putExtra("action","notice_info");
    		i.putExtra("noticeId", messageAmountBean.getContent().getActionParam());
        	getActivity().startActivity(i);
		}else if(MEDAL_TAG.equals(messageAmountBean.getContent().getAction())){
			Intent i=new Intent(getActivity(),ContentFragmentActivity.class);
			i.putExtra("action", "member_info");
			i.putExtra("memberId", messageAmountBean.getContent().getActionParam());
			getActivity().startActivity(i);
		}else if(TARGET_TAG.equals(messageAmountBean.getContent().getAction())){
			Intent i=new Intent(getActivity(),ContentFragmentActivity.class);
			i.putExtra("action", "strategic_info");
			i.putExtra("targetId", messageAmountBean.getContent().getActionParam());
			getActivity().startActivity(i);
		}else if(INDEX_TAG.equals(messageAmountBean.getContent().getAction())){
			Intent i = new Intent(getActivity(), MainActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        	getActivity().startActivity(i);
		}
	}

	/**指定账号消息数据请求*/
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageNow", pageNow+"");//页码
		rp.put("accountId", messageBean.getId());
		asyncHttpClient.post(getActivity(), Constants.queryByAccountAPI, rp, responseHandler);
	}

	/**指定账号消息数据请求回调*/
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			if(kzxMessageAdapter.isEmpty()){
				default_load_view.setVisibility(View.VISIBLE);
			}else if(kzxMessageAdapter.isEmpty()&&pageNow==1){
				swipeLayout.setRefreshing(true);
			}else{
				((ProgressBar)footerView.findViewById(R.id.default_load)).setVisibility(View.VISIBLE);
				footerView.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onFinish() {
			super.onFinish();
			swipeLayout.setRefreshing(false);
			default_load_view.setVisibility(View.GONE);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				long pageSize=new JSONObject(content).optLong("pageSize",0);
				long rowCount=new JSONObject(content).optLong("rowCount",0);
				long totalPage = (rowCount + pageSize -1) / pageSize;
				String records=new JSONObject(content).optString("records");
				String accountName=new JSONObject(content).optString("accountName");
				String accountIcon=new JSONObject(content).optString("accountIcon");
				int accountId=new JSONObject(content).optInt("accountId",0);
				if (!StringUtils.isEmpty(records)&&!"[]".equals(records)) {
					List<MessageAmountBean> noticeList = new ArrayList<MessageAmountBean>();
					noticeList.addAll((List<MessageAmountBean>)new Gson().fromJson(records, new TypeToken<List<MessageAmountBean>>() {}.getType()));
					kzxMessageAdapter.setDataForLoader(noticeList,pageNow==1?true:false,new AmountMessageBean(accountName,accountId,accountIcon));
					if (pageNow == totalPage) {
						//最后一页
						footerView.setVisibility(View.VISIBLE);
						mHasRequestedMore = true;
						((ProgressBar)footerView.findViewById(R.id.default_load)).setVisibility(View.INVISIBLE);
					} else {
						//新增页码
						footerView.setVisibility(View.INVISIBLE);
						pageNow++;
						mHasRequestedMore = false;
					}
				}else{
					//空数据
					img_empty_feed.setVisibility(View.VISIBLE);
					dateCustomList.setEmptyView(img_empty_feed);
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
	
	/**接受或者拒绝邀请*/ 
	private void postAcceptInvite(final MessageAmountBean messageAmountBean) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("newMemberId", messageAmountBean.getContent().getActionParam());
		asyncHttpClient.post(getActivity(), Constants.acceptInviteAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String content = new String(responseBody);
				try {
					boolean success = new JSONObject(content).optBoolean("success", false);
					String message = new JSONObject(content).optString("message");
					String data = new JSONObject(content).optString("data");
					if (success) {
						showMessageDialog(message);
					}else{
						MsgTools.toast(getActivity(), message, Toast.LENGTH_LONG);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				onFailureToast(error);
			}
		});
	}

	/**接受或者拒绝邀请弹出框*/
	private void showMessageDialog(String message){
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
		.setIcon(null)
		.setMessage(message)
		.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				gotoMain();
			}
		}).create().show();
	}
	
	/**跳转到主页面*/
	private void gotoMain(){
		JPushInterface.clearAllNotifications(getActivity());
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
		closeFragment();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
	}

}
