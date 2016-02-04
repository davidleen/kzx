package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
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
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerExecutionAdapter;
import com.android.yijiang.kzx.bean.PartnerBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
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
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.android.yijiang.kzx.widget.listviewanimations.itemmanipulation.OnDismissCallback;
import com.android.yijiang.kzx.widget.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 工作台数据
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxPartnerResultFragment extends BaseFragment {

	private String TAG = KzxBenchResultFragment.class.getName();

	private SwipeRefreshLayout swipeLayout;
	private ListView dateCustomList;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;
	private View footerView;
	private LinearLayout filterTypeBtn;
	private TextView filterTypeTv;
	private ImageView filterTypeIv;
	private TextView filterSearchTv;
	private PopupWindow popupWindow;
	private boolean mHasRequestedMore;

	private View headerView;
	private LinearLayout leftMenuView;
	private LinearLayout executeBtn;
	private LinearLayout acceptanceBtn;
	private LinearLayout unEvaluationBtn;
	private LinearLayout evaluationBtn;

	private String typeStr, stateStr = "1";// 类型,状态
	private AsyncHttpClient asyncHttpClient;
	private KzxPartnerExecutionAdapter kzxExecutionAdapter;

	private long pageNow = 1;
	private Gson gson;
	private Type tt;

	private static final int INITIAL_DELAY_MILLIS = 300;
	private PartnerBean partnerBean;
	private ACache aCache;

	public static KzxPartnerResultFragment newInstance(PartnerBean partnerBean, String typeStr) {
		KzxPartnerResultFragment fragment = new KzxPartnerResultFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("partnerBean", partnerBean);
		bundle.putString("typeStr", typeStr);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		partnerBean = (PartnerBean) getArguments().getSerializable("partnerBean");
		gson = new GsonBuilder().create();
		tt = new TypeToken<List<TaskBean>>() { }.getType();
		typeStr = getArguments().getString("typeStr", "");
		aCache = ACache.get(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_execute_result_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headerView = getActivity().getLayoutInflater().inflate(R.layout.kzx_bench_execute_header_fragment, null);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		filterTypeBtn = (LinearLayout) headerView.findViewById(R.id.filterTypeBtn);
		filterTypeTv = (TextView) headerView.findViewById(R.id.filterTypeTv);
		filterTypeIv = (ImageView) headerView.findViewById(R.id.filterTypeIv);
		filterSearchTv = (TextView) headerView.findViewById(R.id.filterSearchTv);
		footerView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer, null);
		footerView.setVisibility(View.INVISIBLE);
		kzxExecutionAdapter = new KzxPartnerExecutionAdapter(getActivity(), typeStr);
		dateCustomList = (ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.addHeaderView(headerView, null, false);
		dateCustomList.addFooterView(footerView, null, false);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		// 设置滚动动画效果
		final SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(kzxExecutionAdapter);
		swingBottomInAnimationAdapter.setInitialDelayMillis(INITIAL_DELAY_MILLIS);
		swingBottomInAnimationAdapter.setAbsListView(dateCustomList);

		dateCustomList.setAdapter(swingBottomInAnimationAdapter);
		dateCustomList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TaskBean taskBean = (TaskBean) parent.getItemAtPosition(position);
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "task_detail");
				mIntent.putExtra("taskId", taskBean.getId());
				mIntent.putExtra("notDoDecrypt", false);
				getActivity().startActivity(mIntent);
			}
		});
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				pageNow = 1;// 上拉刷新重置
				postLoad(0);
			}
		});
		kzxExecutionAdapter.setOnItemClickListener(new KzxPartnerExecutionAdapter.OnItemClickListener() {
			public void onItemClick(int position, TaskBean taskBean, String typeStr, int type) {
				switch (type) {
				case 1:// 反馈
					DialogFragment myFragment = KzxFaceBackDialogFragment.newInstance(taskBean, typeStr, new KzxFaceBackDialogFragment.OnRefreshSchedule() {
						@Override
						public void onRefreshSchedule(TaskIdsBean taskIdsBean) {
							onRereshTaskBean(taskIdsBean);
						}
					});
					myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
					myFragment.show(getFragmentManager(), TAG);
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
					if (lastInScreen >= totalItemCount && totalItemCount != 0 && totalItemCount != dateCustomList.getHeaderViewsCount() + dateCustomList.getFooterViewsCount() && kzxExecutionAdapter.getCount() > 0) {
						postLoad(2);
						mHasRequestedMore = true;
					}
				}
			}
		});
		// popwindow弹出层设置
		View rightMenuView = getActivity().getLayoutInflater().inflate(R.layout.task_info_left_menu_fragment, null);
		popupWindow = new PopupWindow(rightMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.taskPopWindowAnim);
		popupWindow.update();
		// 还原箭头动画
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			public void onDismiss() {
				filterTypeIv.clearAnimation();
				Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.imageview_rotate_2);
				LinearInterpolator lin = new LinearInterpolator();
				operatingAnim.setInterpolator(lin);
				operatingAnim.setFillAfter(true);
				filterTypeIv.startAnimation(operatingAnim);
			}
		});
		leftMenuView = (LinearLayout) rightMenuView.findViewById(R.id.leftMenuView);
		executeBtn = (LinearLayout) rightMenuView.findViewById(R.id.executeBtn);
		acceptanceBtn = (LinearLayout) rightMenuView.findViewById(R.id.acceptanceBtn);
		unEvaluationBtn = (LinearLayout) rightMenuView.findViewById(R.id.unEvaluationBtn);
		evaluationBtn = (LinearLayout) rightMenuView.findViewById(R.id.evaluationBtn);
		leftMenuView.setTag(executeBtn);// 设置默认Tag,用于切换保存状态
		((TextView)acceptanceBtn.getChildAt(0)).setText(getString("sponsor".equals(typeStr)?R.string.task_filter_default_hint_2_1:R.string.task_filter_default_hint_2));
		executeBtn.setOnClickListener(stateListener);
		acceptanceBtn.setOnClickListener(stateListener);
		unEvaluationBtn.setOnClickListener(stateListener);
		evaluationBtn.setOnClickListener(stateListener);
		// 状态框的弹出
		filterTypeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.showAsDropDown(filterTypeTv);
				filterTypeIv.clearAnimation();
				Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.imageview_rotate);
				LinearInterpolator lin = new LinearInterpolator();
				operatingAnim.setInterpolator(lin);
				operatingAnim.setFillAfter(true);
				filterTypeIv.startAnimation(operatingAnim);
			}
		});
		filterSearchTv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				kzxExecutionAdapter.getFilter().filter(s);
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
		filterSearchTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					final String contentStr = v.getText().toString();
					kzxExecutionAdapter.getFilter().filter(contentStr);
				}
				return false;
			}
		});
	}

	/**popwindow 状态选择*/ 
	OnClickListener stateListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			resetPopWindow();
			leftMenuView.setTag(v);
			switch (v.getId()) {
			case R.id.executeBtn:
				stateStr = "1";
				filterTypeTv.setText(getString(R.string.task_filter_default_hint));
				executeBtn.getChildAt(0).setEnabled(true);
				executeBtn.getChildAt(1).setEnabled(true);
				break;
			case R.id.acceptanceBtn:
				stateStr = "2";
				filterTypeTv.setText(getString("sponsor".equals(typeStr)?R.string.task_filter_default_hint_2_1:R.string.task_filter_default_hint_2));
				acceptanceBtn.getChildAt(0).setEnabled(true);
				acceptanceBtn.getChildAt(1).setEnabled(true);
				break;
			case R.id.unEvaluationBtn:
				stateStr = "3";
				filterTypeTv.setText(getString(R.string.task_filter_default_hint_3));
				unEvaluationBtn.getChildAt(0).setEnabled(true);
				unEvaluationBtn.getChildAt(1).setEnabled(true);
				break;
			case R.id.evaluationBtn:
				stateStr = "4";
				filterTypeTv.setText(getString(R.string.task_filter_default_hint_4));
				evaluationBtn.getChildAt(0).setEnabled(true);
				evaluationBtn.getChildAt(1).setEnabled(true);
				break;
			default:
				break;
			}
			dismissPopWindow();
		}
	};

	/**统计数的更新*/
	private void updateAmountByPopWindow(long state1Count,long state2Count,long state3Count,long state4Count){
		((TextView)executeBtn.getChildAt(1)).setText(state1Count+"");
		((TextView)acceptanceBtn.getChildAt(1)).setText(state2Count+"");
		((TextView)unEvaluationBtn.getChildAt(1)).setText(state3Count+"");
		((TextView)evaluationBtn.getChildAt(1)).setText(state4Count+"");
	}
	
	
	/**popwindow 还原状态*/ 
	private void resetPopWindow() {
		LinearLayout lastLayout = (LinearLayout) leftMenuView.getTag();
		lastLayout.getChildAt(0).setEnabled(false);
		lastLayout.getChildAt(1).setEnabled(false);
	}

	/**popwindow 关闭*/ 
	private void dismissPopWindow() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();// 关闭
		}
		pageNow = 1;// 上拉刷新重置
		postLoad(0);
	}

	/**获取我的伙伴数据加载*/
	private void postLoad(final int isFirstLoad) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("type", typeStr);// 类型
		rp.put("pageNow", pageNow + "");// 页码
		rp.put("state", stateStr);// 状态
		rp.put("memberId", partnerBean.getPartnerId());// 指定伙伴
		if(!StringUtils.isEmpty(partnerBean.getTargetId())){
			rp.put("targetId", partnerBean.getTargetId());//战略目标
		}
		asyncHttpClient.post(getActivity(), Constants.queryByPartnerAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				switch (isFirstLoad) {
				case 0:
					swipeLayout.setRefreshing(true);
					img_empty_feed.setVisibility(View.GONE);
					break;
				case 1:
					default_load_view.setVisibility(View.VISIBLE);
					img_empty_feed.setVisibility(View.GONE);
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
					long state1Count=new JSONObject(content).optLong("state1Count",0);
					long state2Count=new JSONObject(content).optLong("state2Count",0);
					long state3Count=new JSONObject(content).optLong("state3Count",0);
					long state4Count=new JSONObject(content).optLong("state4Count",0);
					String records=new JSONObject(content).optString("records");
					updateAmountByPopWindow(state1Count,state2Count,state3Count,state4Count);
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
					MsgTools.toast(getActivity(),getString(R.string.request_error), Toast.LENGTH_SHORT);
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
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (kzxExecutionAdapter == null || kzxExecutionAdapter.isEmpty()) {
				pageNow = 1;
				postLoad(1);
			}
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
	}

}
