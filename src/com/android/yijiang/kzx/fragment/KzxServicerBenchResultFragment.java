package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.CookieStore;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.graphics.drawable.BitmapDrawable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView.OnScrollListener;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxServicerExecutionAdapter;
import com.android.yijiang.kzx.bean.ServicerBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.PersistentCookieStore;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
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
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 服务商工作台数据
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxServicerBenchResultFragment extends BaseFragment {

	private String TAG = KzxServicerBenchResultFragment.class.getName();

	private SwipeRefreshLayout swipeLayout;
	private ListView dateCustomList;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;
	private TextView currentTitle;
	private View footerView;
	private boolean mHasRequestedMore;

	private ServicerBean servicerBean;
	private boolean isLoaded = true;
	private AsyncHttpClient asyncHttpClient;
	private MessageReceiver mMessageReceiver;
	private KzxServicerExecutionAdapter kzxExecutionAdapter;

	private long pageNow = 1;
	private Gson gson;
	private Type tt;

	// 头部View
	private ImageButton backBtn;

	public static KzxServicerBenchResultFragment newInstance(ServicerBean servicerBean) {
		KzxServicerBenchResultFragment fragment = new KzxServicerBenchResultFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("servicerBean", servicerBean);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new GsonBuilder().create();
		tt = new TypeToken<List<TaskBean>>() { }.getType();
		servicerBean = (ServicerBean) getArguments().getSerializable("servicerBean");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_servicer_fragment, null);
		return contentView;
	}

	// 注册广播
	public void registerMessageReceiver() {
		postLoad(1);
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".SERVICER_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		footerView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer, null);
		footerView.setVisibility(View.INVISIBLE);
		kzxExecutionAdapter = new KzxServicerExecutionAdapter(getActivity());
		dateCustomList = (ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.addFooterView(footerView, null, false);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		currentTitle = (TextView) view.findViewById(R.id.currentTitle);
		dateCustomList.setAdapter(kzxExecutionAdapter);
		dateCustomList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TaskBean taskBean = (TaskBean) parent.getItemAtPosition(position);
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "task_detail");
				mIntent.putExtra("type", "servicer");
				mIntent.putExtra("taskId", taskBean.getId());
				getActivity().startActivity(mIntent);
			}
		});
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				pageNow = 1;// 上拉刷新重置
				postLoad(0);
			}
		});
		kzxExecutionAdapter.setOnItemClickListener(new KzxServicerExecutionAdapter.OnItemClickListener() {
			public void onItemClick(int position, TaskBean taskBean) {
				DialogFragment myFragment = KzxFaceBackDialogFragment.newInstance(taskBean, null, new KzxFaceBackDialogFragment.OnRefreshSchedule() {
					@Override
					public void onRefreshSchedule(TaskIdsBean taskIdsBean) {
						onRereshTaskBean(taskIdsBean);
					}
				});
				myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
				myFragment.show(getFragmentManager(), TAG);
			}
		});
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
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
					if (lastInScreen >= totalItemCount && totalItemCount != 0 && totalItemCount != dateCustomList.getHeaderViewsCount() + dateCustomList.getFooterViewsCount() && kzxExecutionAdapter.getCount() > 0) {
						postLoad(2);
						mHasRequestedMore = true;
					}
				}
			}
		});
		currentTitle.setText(servicerBean.getCompanyName());
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerMessageReceiver();
	}

	/** 请求服务商任务工作台数据 */
	private void postLoad(final int isFirstLoad) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageNow", pageNow + "");// 页码
		rp.put("companyId", servicerBean.getCompanyId());
		rp.put("relateClient", servicerBean.getRelateClient());
		asyncHttpClient.post(getActivity(), Constants.queryServicerTasksAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				switch (isFirstLoad) {
				case 0:
					swipeLayout.setRefreshing(true);
					img_empty_feed.setVisibility(View.GONE);
					break;
				case 1:
					img_empty_feed.setVisibility(View.GONE);
					default_load_view.setVisibility(View.VISIBLE);
					break;
				case 2:
					footerView.findViewById(R.id.default_load).setVisibility(View.VISIBLE);
					footerView.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}

			@Override
			public void onFinish() {
				default_load_view.setVisibility(View.GONE);
				swipeLayout.setRefreshing(false);
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
						List<TaskBean> taskBeanList = new ArrayList<TaskBean>();
						taskBeanList.addAll((List<TaskBean>) gson.fromJson(records, tt));
						kzxExecutionAdapter.setDataForLoader(taskBeanList, isFirstLoad == 0 ? true : false);
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
						((ProgressBar) footerView.findViewById(R.id.default_load)).setVisibility(View.GONE);
						kzxExecutionAdapter.clearDataForLoader();
						img_empty_feed.setVisibility(View.VISIBLE);
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
		});
	}

	// 回调广播
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".SERVICER_RECEIVED_ACTION").equals(intent.getAction())) {
				String action = intent.getStringExtra("action");
				if ("reload".equals(action)) {
					pageNow = 1;// 上拉刷新重置
					postLoad(0);
				}
			}
		}
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && isLoaded) {
			postLoad(1);
			isLoaded = !isLoaded;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		TaskIdsBean taskIdsBean = ApplicationController.getTaskIdsBean();
		onRereshTaskBean(taskIdsBean);
	}

	private void onRereshTaskBean(TaskIdsBean taskIdsBean) {
		if (taskIdsBean != null) {
			kzxExecutionAdapter.refreshSchedule(taskIdsBean);
			ApplicationController.setTaskIdsBean(null);
		}
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
