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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxIncentiveAdapter;
import com.android.yijiang.kzx.adapter.KzxMemberAdapter;
import com.android.yijiang.kzx.adapter.KzxServiceAdapter;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MemberBean;
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
import com.android.yijiang.kzx.widget.AutoLoadListener;
import com.android.yijiang.kzx.widget.AutoLoadListener.AutoLoadCallBack;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 全体成员
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxMemberResultFragment extends BaseFragment {

	private boolean mHasRequestedMore;
	private long pageNow = 1;
	private ProgressBar footerView;

	private GridView dateCustomList;
	private SwipeRefreshLayout swipeLayout;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;

	private Gson gson;
	private Type tt;
	private AsyncHttpClient asyncHttpClient;
	private KzxMemberAdapter kzxMemberAdapter;

	// 头部View
	private ImageView stateTv;
	private ImageButton btn_list;
	private TextView btn_add_new;

	public static KzxMemberResultFragment newInstance() {
		KzxMemberResultFragment fragment = new KzxMemberResultFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new GsonBuilder().create();
		tt = new TypeToken<List<MemberBean>>() {}.getType();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_member_fragment, null);
		return contentView;
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
				pageNow=1;
				postLoad(true);
			}
		});
		footerView = (ProgressBar) view.findViewById(R.id.footer_load_view);
		footerView.setVisibility(View.INVISIBLE);
		kzxMemberAdapter = new KzxMemberAdapter(getActivity());
		dateCustomList = (GridView) view.findViewById(R.id.dateCustomGrid);

		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(kzxMemberAdapter);
		swingBottomInAnimationAdapter.setAbsListView(dateCustomList);

		dateCustomList.setAdapter(kzxMemberAdapter);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		dateCustomList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!mHasRequestedMore) {
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if (lastInScreen >= totalItemCount && totalItemCount != 0  && kzxMemberAdapter.getCount() > 0) {
						postLoad(false);
						mHasRequestedMore = true;
					}
				}
			}
		});
		dateCustomList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MemberBean memberBean = (MemberBean) parent.getItemAtPosition(position);
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("memberId", memberBean.getId());
				mIntent.putExtra("action", "member_info");
				startActivity(mIntent);
			}
		});
		btn_list = (ImageButton) view.findViewById(R.id.btn_list);
		btn_add_new = (TextView) view.findViewById(R.id.btn_add_new);
		btn_list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity().getPackageName() + ".HOME_RECEIVED_ACTION");
				mIntent.putExtra("action", "close_drawer");
				getActivity().sendBroadcast(mIntent);
			}
		});
		// 邀请成员
		btn_add_new.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(v.getContext(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "invite");
				startActivity(mIntent);
			}
		});
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState==null){
			postLoad(true);
		}else{
			pageNow=savedInstanceState.getLong("pageNow");
			List<MemberBean> memberList=(List<MemberBean>) savedInstanceState.getSerializable("memberList");
			kzxMemberAdapter.setDataForLoader(memberList, true);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("pageNow", pageNow);
		outState.putSerializable("memberList", (Serializable) kzxMemberAdapter.getMemberList());
	}
	
	/**获取全体成员数据*/
	private void postLoad(final boolean isFirstLoad) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageNow", pageNow + "");// 页码
		asyncHttpClient.post(getActivity(), Constants.queryAllByCompanyAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				if (isFirstLoad) {
					if(kzxMemberAdapter.getCount()>0){
						swipeLayout.setRefreshing(true);
					}else{
						default_load_view.setVisibility(View.VISIBLE);
						footerView.setVisibility(View.GONE);
						img_empty_feed.setVisibility(View.GONE);
					}
				} else {
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
					long pageSize = new JSONObject(content).optLong("pageSize", 0);
					long rowCount = new JSONObject(content).optLong("rowCount", 0);
					long totalPage = (rowCount + pageSize - 1) / pageSize;
					String records = new JSONObject(content).optString("records");
					if (!StringUtils.isEmpty(records) && !"[]".equals(records)) {
						List<MemberBean> memberList = new ArrayList<MemberBean>();
						memberList.addAll((List<MemberBean>) gson.fromJson(records, tt));
						kzxMemberAdapter.setDataForLoader(memberList, isFirstLoad);
						if (pageNow == totalPage) {
							// 最后一页
							footerView.setVisibility(View.GONE);
							mHasRequestedMore = true;
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
					MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
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
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
		super.onDestroy();
	}

}
