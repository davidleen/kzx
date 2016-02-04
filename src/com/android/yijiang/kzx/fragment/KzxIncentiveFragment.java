package com.android.yijiang.kzx.fragment;

import java.io.File;
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

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxIncentiveAdapter;
import com.android.yijiang.kzx.adapter.KzxNoticeAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.NoticeBean;
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
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 激励认可
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxIncentiveFragment extends BaseFragment {

	private boolean mHasRequestedMore;
	private long pageNow = 1;
	private View footerView;

	private SwipeRefreshLayout swipeLayout;
	private ListView dateCustomList;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;

	private Gson gson;
	private Type tt;
	private KzxIncentiveAdapter kzxIncentiveAdapter;
	private AsyncHttpClient asyncHttpClient;
	private MessageReceiver mMessageReceiver;
	// 头部View
	private ImageView stateTv;
	private ImageButton btn_list;

	public static KzxIncentiveFragment newInstance() {
		KzxIncentiveFragment fragment = new KzxIncentiveFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new GsonBuilder().create();
		tt = new TypeToken<List<MedalBean>>() { }.getType();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_incentive_fragment, null);
		return contentView;
	}

	/**注册广播*/ 
	public void registerMessageReceiver() {
		postLoad(true);
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".INCENTIVE_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		stateTv=(ImageView)view.findViewById(R.id.stateTv);
		if(!StringUtils.isEmpty(getContext().getKzxTokenBean().getNoReadMessageNum())){
			stateTv.setVisibility(Integer.valueOf(getContext().getKzxTokenBean().getNoReadMessageNum())>0?View.VISIBLE:View.GONE);
		}else{
			stateTv.setVisibility(View.GONE);
		}
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				pageNow = 1;
				postLoad(true);
			}
		});
		footerView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer, null);
		footerView.setVisibility(View.INVISIBLE);
		kzxIncentiveAdapter = new KzxIncentiveAdapter(getActivity());
		dateCustomList = (ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.addFooterView(footerView, null, false);
		dateCustomList.setAdapter(kzxIncentiveAdapter);
		dateCustomList = (ListView) view.findViewById(R.id.dateCustomList);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		dateCustomList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!mHasRequestedMore) {
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if (lastInScreen >= totalItemCount && totalItemCount != 0 && totalItemCount != dateCustomList.getHeaderViewsCount() + dateCustomList.getFooterViewsCount() && kzxIncentiveAdapter.getCount() > 0) {
						postLoad(false);
						mHasRequestedMore = true;
					}
				}
			}
		});
		dateCustomList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MedalBean medalBean = (MedalBean) parent.getItemAtPosition(position);
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("memberId", medalBean.getId());
				mIntent.putExtra("action", "member_info");
				startActivity(mIntent);
			}
		});
		kzxIncentiveAdapter.setOnItemClickListener(new KzxIncentiveAdapter.OnItemClickListener() {
			public void onItemClick(int position, MedalBean medalBean) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "add_medal");
				mIntent.putExtra("medalBean", medalBean);
				mIntent.putExtra("position", position);
				startActivity(mIntent);
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
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerMessageReceiver();
	}

	/**回调广播*/ 
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".INCENTIVE_RECEIVED_ACTION").equals(intent.getAction())) {
				String action = intent.getStringExtra("action");
				if ("reload".equals(action)) {
					int position = intent.getIntExtra("position", 0);
					MedalBean medalBean = (MedalBean) intent.getSerializableExtra("medalBean");
					kzxIncentiveAdapter.updateData(position, medalBean);
				}
			}
		}
	}

	/**激励认可*/
	private void postLoad(final boolean isFirstLoad) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageNow", pageNow + "");// 页码
		rp.put("state", "3");
		asyncHttpClient.post(getActivity(), Constants.queryMedalAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				if (isFirstLoad) {
					if (kzxIncentiveAdapter.getCount() > 0) {
						swipeLayout.setRefreshing(true);
					} else {
						default_load_view.setVisibility(View.VISIBLE);
						footerView.setVisibility(View.GONE);
						img_empty_feed.setVisibility(View.GONE);
					}
				} else {
					footerView.setVisibility(View.VISIBLE);
					((ProgressBar) footerView.findViewById(R.id.default_load)).setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onFinish() {
				swipeLayout.setRefreshing(false);
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
						List<MedalBean> noticeList = new ArrayList<MedalBean>();
						noticeList.addAll((List<MedalBean>) gson.fromJson(records, tt));
						kzxIncentiveAdapter.setDataForLoader(noticeList, isFirstLoad);
						if (pageNow == totalPage) {
							// 最后一页
							footerView.setVisibility(View.VISIBLE);
							mHasRequestedMore = true;
							((ProgressBar) footerView.findViewById(R.id.default_load)).setVisibility(View.INVISIBLE);
						} else {
							// 新增页码
							footerView.setVisibility(View.INVISIBLE);
							pageNow++;
							mHasRequestedMore = false;
						}
					} else {
						// 空数据
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
		});
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
		if (mMessageReceiver != null)
			getActivity().unregisterReceiver(mMessageReceiver);
	}

}
