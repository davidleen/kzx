package com.android.yijiang.kzx.fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.PartnerBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.transformer.ZoomOutPageTransformer;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.tab.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 我的伙伴任务详情
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxPartnerBatchFragment extends BaseFragment {

	private ViewPager content_pager;
	private PagerSlidingTabStrip tabStrip;
	private int currentData = 0;
	private int number = 0;// 要销毁的position
	private FragmentPagerAdapter adapter;
	private PartnerBean partnerBean;

	// 头部View
	private TextView currentTitle;
	private ImageButton backBtn;
	private TextView btn_add_task;

	public static KzxPartnerBatchFragment newInstance(PartnerBean partnerBean) {
		KzxPartnerBatchFragment fragment = new KzxPartnerBatchFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("partnerBean", partnerBean);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		partnerBean = (PartnerBean) getArguments().getSerializable("partnerBean");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_partner_result_batch_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final String[] tabs = { "执行", "发起" };
		currentTitle = (TextView) view.findViewById(R.id.currentTitle);
		currentTitle.setText(getString(R.string.name_workspace, partnerBean.getPartnerName()));
		tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		content_pager = (ViewPager) view.findViewById(R.id.content_pager);
		if (savedInstanceState == null) {
			adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
				@Override
				public Fragment getItem(int position) {
					switch (position) {
					case 0:
						return KzxPartnerResultFragment.newInstance(partnerBean, "executor");// 发起人头像
					case 1:
						return KzxPartnerResultFragment.newInstance(partnerBean, "sponsor");// 执行人头像
					}
					return null;
				}

				@Override
				public int getCount() {
					return tabs.length;
				}

				@Override
				public int getItemPosition(Object object) {
					return POSITION_NONE;
				}

				@Override
				public CharSequence getPageTitle(int position) {
					return tabs[position];
				}

				public void destroyItem(View container, int position, Object object) {
					if (position != number) {
						return;
					}
					// TODO Auto-generated method stub
					FragmentManager manager = ((Fragment) object).getFragmentManager();
					FragmentTransaction trans = manager.beginTransaction();
					trans.remove((Fragment) object);
					trans.commitAllowingStateLoss();
				};
			};
		}
		content_pager.setOffscreenPageLimit(tabs.length);
		content_pager.setAdapter(adapter);
		content_pager.setCurrentItem(currentData);
		tabStrip.setViewPager(content_pager);
		tabStrip.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		btn_add_task = (TextView) view.findViewById(R.id.btn_add_task);
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		btn_add_task.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LeaderBean leaderBean=new LeaderBean();
				leaderBean.setId(partnerBean.getPartnerId());
				leaderBean.setName(partnerBean.getPartnerName());
				Intent mIntent = new Intent(v.getContext(), ContentFragmentActivity.class);
				mIntent.putExtra("action", "add_task");
				mIntent.putExtra("leaderBean", leaderBean);
				startActivity(mIntent);
			}
		});
	}

	protected String mToken = UUID.randomUUID().toString();

	protected String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index + ":" + mToken;
	}

	@Override
	public void onResume() {
		super.onResume();
		List<Fragment> mFragmentList = getChildFragmentManager().getFragments();
		// 特殊情况处理(只要涉及到关注业务重新刷新关注页数据)
		TaskIdsBean taskIdsBean = ApplicationController.getTaskIdsBean();
		if (taskIdsBean == null)
			return;
		if (!StringUtils.isNullOrEmpty(mFragmentList)) {
			for (Fragment mFragment : mFragmentList) {
				if (mFragment.getUserVisibleHint()) {
					mFragment.onResume();
					break;
				}
			}
		}
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

}
