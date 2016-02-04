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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.bean.AmountMemberBean;
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
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.nineoldandroids.view.ViewHelper;
import com.android.yijiang.kzx.widget.parallaxheader.ScrollTabHolder;
import com.android.yijiang.kzx.widget.parallaxheader.ScrollTabHolderFragment;
import com.android.yijiang.kzx.widget.tab.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 全体成员
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxMemberInfoFragment extends BaseFragment {

	private SwipeRefreshLayout swipeLayout;

	private CircleImageView user_bg;
	private TextView nameTv;
	private TextView phoneTv;
	private TextView emailTv;
	private TextView growUpTv;
	private ImageButton callBtn;
	private TextView departmentTv;
	private TextView leaderNameTv;

	private ScrollView container;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;
	private String memberId;
	private LinearLayout strategicBtn;
	private ImageView bottomDivider;

	private boolean notDoDecrypt;
	private AmountMemberBean amountMemberBean;

	/** 统计视图 */
	private LinearLayout medalBtn;
	private TextView medalAmountTv;
	private LinearLayout leaderBtn;
	private TextView leaderAmountTv;
	private LinearLayout clientBtn;
	private TextView clientAmountTv;

	private AsyncHttpClient asyncHttpClient;
	private DisplayImageOptions options;

	// 头部View
	private ImageButton backBtn;

	public static KzxMemberInfoFragment newInstance(String memberId, boolean notDoDecrypt) {
		KzxMemberInfoFragment fragment = new KzxMemberInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putString("memberId", memberId);
		bundle.putBoolean("notDoDecrypt", notDoDecrypt);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		memberId = getArguments().getString("memberId");
		notDoDecrypt = getArguments().getBoolean("notDoDecrypt", false);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_avatar_120)
		.showImageForEmptyUri(R.drawable.ic_avatar_120)
		.showImageOnFail(R.drawable.ic_avatar_120)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_member_info_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		strategicBtn = (LinearLayout) view.findViewById(R.id.strategicBtn);
		bottomDivider = (ImageView) view.findViewById(R.id.bottomDivider);
		container = (ScrollView) view.findViewById(R.id.container);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed = (TextView) view.findViewById(R.id.img_empty_feed);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				postLoad();
			}
		});
		medalBtn = (LinearLayout) view.findViewById(R.id.medalBtn);
		medalAmountTv = (TextView) view.findViewById(R.id.medalAmountTv);
		leaderBtn = (LinearLayout) view.findViewById(R.id.leaderBtn);
		leaderAmountTv = (TextView) view.findViewById(R.id.leaderAmountTv);
		growUpTv = (TextView) view.findViewById(R.id.growUpTv);
		clientBtn = (LinearLayout) view.findViewById(R.id.clientBtn);
		clientAmountTv = (TextView) view.findViewById(R.id.clientAmountTv);
		user_bg = (CircleImageView) view.findViewById(R.id.user_bg);
		nameTv = (TextView) view.findViewById(R.id.nameTv);
		phoneTv = (TextView) view.findViewById(R.id.phoneTv);
		emailTv = (TextView) view.findViewById(R.id.emailTv);
		callBtn = (ImageButton) view.findViewById(R.id.callBtn);
		departmentTv = (TextView) view.findViewById(R.id.departmentTv);
		leaderNameTv = (TextView) view.findViewById(R.id.leaderNameTv);
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		medalBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (amountMemberBean == null)
					return;
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "member_medal");
				mIntent.putExtra("amountMemberBean", amountMemberBean);
				startActivity(mIntent);
			}
		});

		leaderBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (amountMemberBean == null)
					return;
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "member_leader");
				mIntent.putExtra("amountMemberBean", amountMemberBean);
				startActivity(mIntent);
			}
		});

		clientBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (amountMemberBean == null)
					return;
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "member_client");
				mIntent.putExtra("amountMemberBean", amountMemberBean);
				startActivity(mIntent);
			}
		});

	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoad();
	}

	/**成员个人信息加载*/ 
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("memberId", memberId);

		if (notDoDecrypt) {
			rp.put("notDoDecrypt", notDoDecrypt);
		}

		asyncHttpClient.post(getActivity(), Constants.memberDetailAPI, rp, responseHandler);
	}

	/**成员个人信息加载回调*/ 
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			super.onStart();
			if (!StringUtils.isEmpty(nameTv.getText().toString())) {
				swipeLayout.setRefreshing(true);
			} else {
				default_load_view.setVisibility(View.VISIBLE);
				strategicBtn.setVisibility(View.GONE);
				bottomDivider.setVisibility(View.GONE);
				container.setVisibility(View.GONE);
			}
		}

		@Override
		public void onFinish() {
			super.onFinish();
			swipeLayout.setRefreshing(false);
			container.setVisibility(View.VISIBLE);
			default_load_view.setVisibility(View.GONE);
			strategicBtn.setVisibility(View.VISIBLE);
			bottomDivider.setVisibility(View.VISIBLE);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success");
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (success) {
					amountMemberBean = JSON.parseObject(data, AmountMemberBean.class);
					initData(amountMemberBean);
				} else {
					MsgTools.toast(getActivity(), message, Toast.LENGTH_SHORT);
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

	// 初始化更新个人信息
	private void initData(final AmountMemberBean amountMemberBean) {
		String leaderName = StringUtils.isEmpty(amountMemberBean.getLeaderName()) ? "" : amountMemberBean.getLeaderName();
		nameTv.setText(amountMemberBean.getName());
		phoneTv.setText(amountMemberBean.getPhone());
		emailTv.setText(amountMemberBean.getEmail());
		departmentTv.setText(amountMemberBean.getDepartment());
		leaderNameTv.setText(getString(R.string.account_report_hint) + leaderName);
		growUpTv.setText("LV " + amountMemberBean.getGrowUp());
		medalAmountTv.setText(String.valueOf(amountMemberBean.getMedalCount()));
		leaderAmountTv.setText(String.valueOf(amountMemberBean.getTaskLeaderGoodCount()));
		clientAmountTv.setText(String.valueOf(amountMemberBean.getTaskClientGoodCount()));
//		Picasso.with(getActivity()).load(amountMemberBean.getIcon()) //
//				.placeholder(R.drawable.default_bg) //
//				.error(R.drawable.default_bg) //
//				.into(uiTarget);
		ImageLoader.getInstance().displayImage(amountMemberBean.getIcon(), user_bg, options);
		callBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!StringUtils.isEmpty(amountMemberBean.getPhone())) {
					startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + amountMemberBean.getPhone())));
				}
			}
		});
		strategicBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MedalBean medalBean = new MedalBean();
				medalBean.setName(amountMemberBean.getName());
				medalBean.setIcon(amountMemberBean.getIcon());
				medalBean.setId(amountMemberBean.getMemberId());
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "add_medal");
				mIntent.putExtra("medalBean", medalBean);
				startActivity(mIntent);
			}
		});
	}

	Target uiTarget = new Target() {
		@Override
		public void onPrepareLoad(Drawable arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onBitmapLoaded(Bitmap bitmp, LoadedFrom arg1) {
			user_bg.setImageBitmap(bitmp);
		}

		@Override
		public void onBitmapFailed(Drawable arg0) {
			// TODO Auto-generated method stub
		}
	};

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
	}
}
