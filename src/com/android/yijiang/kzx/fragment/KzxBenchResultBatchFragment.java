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
import android.support.annotation.Nullable;
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
import com.android.yijiang.kzx.bean.KzxTokenBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.fragment.KzxDrawerFragment.MessageReceiver;
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
 * 工作台
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxBenchResultBatchFragment extends Fragment {

	public ViewPager content_pager;
	private PagerSlidingTabStrip tabStrip;
	private int currentData=0;
	private int number=0;//要销毁的position
	private FragmentPagerAdapter adapter;
	private MessageReceiver mMessageReceiver;
	
	//头部View
	private ImageButton btn_list;
	private ImageView stateTv;
	private TextView btn_add_task;
	
	private ImageView threePositionIv;
	
	public static KzxBenchResultBatchFragment newInstance() {
		KzxBenchResultBatchFragment fragment = new KzxBenchResultBatchFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_bench_result_batch_fragment, null);
		return contentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerMessageReceiver();
	}
	
	/**注册广播*/ 
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".BENCH_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final String[] tabs={"执行","发起","抄送","订阅"};
		threePositionIv=(ImageView)view.findViewById(R.id.threePositionIv);
		stateTv=(ImageView)view.findViewById(R.id.stateTv);
		if(!StringUtils.isEmpty(getContext().getKzxTokenBean().getNoReadMessageNum())){
			stateTv.setVisibility(Integer.valueOf(getContext().getKzxTokenBean().getNoReadMessageNum())>0?View.VISIBLE:View.GONE);
		}else{
			stateTv.setVisibility(View.GONE);
		}
		tabStrip=(PagerSlidingTabStrip)view.findViewById(R.id.tabs);
		content_pager = (ViewPager) view.findViewById(R.id.content_pager);
		adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				switch (position) {
				case 0:
					return KzxBenchResultFragment.newInstance("executor");//发起人头像
				case 1:
					return KzxBenchResultFragment.newInstance("sponsor");//执行人头像
				case 2:
					return KzxBenchResultFragment.newInstance("copyto");//执行人头像
				case 3:
					return KzxBenchResultFragment.newInstance("subscribe");//执行人头像
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
				if(position!=number){
					return;
				}
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
//				Fragment fragment = (Fragment) adapter.instantiateItem(content_pager, position);
//				adapter.setPrimaryItem(content_pager, 0, fragment);
//				adapter.finishUpdate(content_pager);
				switch (position) {
				case 3:
					threePositionIv.setColorFilter(Color.parseColor("#ffffff"));
					break;
				default:
					threePositionIv.setColorFilter(Color.parseColor("#6799d0"));
					break;
				}
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
		btn_add_task=(TextView)view.findViewById(R.id.btn_add_task);
		btn_list=(ImageButton)view.findViewById(R.id.btn_list);
		btn_list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(getActivity().getPackageName()+".HOME_RECEIVED_ACTION");
				mIntent.putExtra("action", "close_drawer");
				getActivity().sendBroadcast(mIntent);
			}
		});
		btn_add_task.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(v.getContext(),ContentFragmentActivity.class);
				mIntent.putExtra("action", "add_task");
				startActivity(mIntent);
			}
		});
	}
	
	protected String mToken = UUID.randomUUID().toString();

	protected String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index + ":" + mToken;
	}
	
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName()+".BENCH_RECEIVED_ACTION").equals(intent.getAction())) {
				if("destroy".equals(intent.getStringExtra("action"))){
					number=intent.getIntExtra("content_pager", 0);
					Object objectobject = adapter.instantiateItem(content_pager, number);
					if (objectobject != null){
						adapter.destroyItem(content_pager, number, objectobject);
					}
					adapter.notifyDataSetChanged();
				}else if("get_message_amount".equals(intent.getStringExtra("action"))){
					String noReadMessageNum= getContext().getKzxTokenBean().getNoReadMessageNum();
					if(!StringUtils.isEmpty(noReadMessageNum)){
						stateTv.setVisibility(Integer.valueOf(getContext().getKzxTokenBean().getNoReadMessageNum())>0?View.VISIBLE:View.GONE);
					}
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		List<Fragment> mFragmentList=getChildFragmentManager().getFragments(); 
		//特殊情况处理(只要涉及到关注业务重新刷新关注页数据)
		TaskIdsBean taskIdsBean=ApplicationController.getTaskIdsBean();
		if(taskIdsBean==null) return;
		if("isOrder".equals(taskIdsBean.getTypeStr())){
			number=3;//(0:执行,1:发起,2:关注,3:抄送)
			Object objectobject = adapter.instantiateItem(content_pager, number);
			if (objectobject != null){
				adapter.destroyItem(content_pager, number, objectobject);
			}
			adapter.notifyDataSetChanged();
		} 
		if(!StringUtils.isNullOrEmpty(mFragmentList)){
			for (Fragment mFragment : mFragmentList) {
				if(mFragment.getUserVisibleHint()){
					mFragment.onResume();
					break;
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mMessageReceiver!=null){
			getActivity().unregisterReceiver(mMessageReceiver);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = (Fragment) getChildFragmentManager().findFragmentById(R.id.content_pager);
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

}
