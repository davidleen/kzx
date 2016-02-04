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
import java.util.HashMap;
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
import android.graphics.Color;
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
import com.android.yijiang.kzx.bean.ClientBean;
import com.android.yijiang.kzx.bean.Contacts;
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
 * 关联客户
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxClientResultBatchFragment extends BaseFragment {

	public ViewPager content_pager;
	private PagerSlidingTabStrip tabStrip;
	private int currentData = 0;
	private FragmentPagerAdapter adapter;
	private MessageReceiver mMessageReceiver;

	// 头部View
	private ImageButton backBtn;
	private TextView doneBtn;

	private KzxClientSelectedFragment kzxClientSelectedFragment;
	private KzxClientAddFragment kzxClientAddFragment;

	private HashMap<String, ClientBean> isClientHash;
	private ClientBean clientBean;

	public static KzxClientResultBatchFragment newInstance(HashMap<String, ClientBean> isClientHash, ClientBean clientBean) {
		KzxClientResultBatchFragment fragment = new KzxClientResultBatchFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("isClientHash", isClientHash);
		bundle.putSerializable("clientBean", clientBean);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clientBean = (ClientBean) getArguments().getSerializable("clientBean");
		isClientHash = (HashMap<String, ClientBean>) getArguments().getSerializable("isClientHash");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_client_result_batch_fragment, null);
		return contentView;
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".BENCH_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		final String[] tabs = { "通讯录", "手动输入" };
		tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		content_pager = (ViewPager) view.findViewById(R.id.content_pager);
		adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				switch (position) {
				case 0:
					if (kzxClientSelectedFragment == null) {
						kzxClientSelectedFragment = KzxClientSelectedFragment.newInstance(isClientHash);
					}
					return kzxClientSelectedFragment;
				case 1:
					if (kzxClientAddFragment == null) {
						kzxClientAddFragment = KzxClientAddFragment.newInstance(clientBean);
					}
					return kzxClientAddFragment;
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

			public void notifyDataSetChanged() {
				mToken = UUID.randomUUID().toString();
				super.notifyDataSetChanged();
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				// TODO Auto-generated method stub
				FragmentManager manager = ((Fragment) object).getFragmentManager();
				FragmentTransaction trans = manager.beginTransaction();
				trans.remove((Fragment) object);
				trans.commitAllowingStateLoss();
			}
		};
		content_pager.setOffscreenPageLimit(tabs.length);
		content_pager.setAdapter(adapter);
		content_pager.setCurrentItem(currentData);
		tabStrip.setViewPager(content_pager);
		tabStrip.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Fragment fragment = (Fragment) adapter.instantiateItem(content_pager, position);
				adapter.setPrimaryItem(content_pager, 0, fragment);
				adapter.finishUpdate(content_pager);
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
		doneBtn = (TextView) view.findViewById(R.id.doneBtn);
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (content_pager.getCurrentItem()) {
				case 0:
					kzxClientSelectedFragment.postSelectedClient();
					break;
				case 1:
					kzxClientAddFragment.postSelectedClient();
					break;
				default:
					break;
				}
			}
		});
		registerMessageReceiver();
		super.onViewCreated(view, savedInstanceState);
	}

	protected String mToken = UUID.randomUUID().toString();

	protected String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index + ":" + mToken;
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".CLIENT_RECEIVED_ACTION").equals(intent.getAction())) {
				if ("destroy".equals(intent.getStringExtra("action"))) {
					int number = intent.getIntExtra("content_pager", 0);
					Object objectobject = adapter.instantiateItem(content_pager, number);
					if (objectobject != null) {
						adapter.destroyItem(content_pager, number, objectobject);
					}
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMessageReceiver != null)
			getActivity().unregisterReceiver(mMessageReceiver);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

}
