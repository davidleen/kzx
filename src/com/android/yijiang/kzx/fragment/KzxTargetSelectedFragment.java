package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxCopySelectedAdapter;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxIncentiveAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxTargetSelectedAdapter;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.TargetCanRelateBean;
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
 * 选择关联目标
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxTargetSelectedFragment extends BaseFragment {

	private boolean mHasRequestedMore;
	private long pageNow=1;
	private View footerView;
	
	private ListView dateCustomList;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;
	private TextView doneBtn;//选中事件
	
	private HashMap<String, TargetCanRelateBean> isLeaderHash;
	private Gson gson ;
	private Type tt ;
	private AsyncHttpClient asyncHttpClient;
	private KzxTargetSelectedAdapter kzxTargetSelectedAdapter;
	
	//头部View
	private ImageButton backBtn;
	private EditText searchEt;
	
	public static KzxTargetSelectedFragment newInstance(HashMap<String, TargetCanRelateBean> isLeaderHash) {
		KzxTargetSelectedFragment fragment = new KzxTargetSelectedFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("isLeaderHash", isLeaderHash);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLeaderHash=(HashMap<String, TargetCanRelateBean>) getArguments().getSerializable("isLeaderHash");
		gson = new GsonBuilder().create();
		tt=new TypeToken<List<TargetCanRelateBean>>() {}.getType();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_target_selected_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		footerView=getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer, null);
		footerView.setVisibility(View.INVISIBLE);
		kzxTargetSelectedAdapter=new KzxTargetSelectedAdapter(getActivity());
		searchEt=(EditText)view.findViewById(R.id.searchEt);
		dateCustomList=(ListView) view.findViewById(R.id.dateCustomList);
		default_load_view=(ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed=(TextView)view.findViewById(R.id.img_empty_feed);
		doneBtn=(TextView)view.findViewById(R.id.doneBtn);
		dateCustomList.addFooterView(footerView, null, false);
		dateCustomList.setAdapter(kzxTargetSelectedAdapter);
		dateCustomList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!mHasRequestedMore) {
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if (lastInScreen >= totalItemCount && totalItemCount != 0 && totalItemCount != dateCustomList.getHeaderViewsCount() + dateCustomList.getFooterViewsCount() && kzxTargetSelectedAdapter.getCount() > 0) {
						postLoad();
						mHasRequestedMore = true;
					}
				}
			}
		});		
		//选中确定
		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, TargetCanRelateBean> isSelected=kzxTargetSelectedAdapter.getIsSelected();
				Intent mIntent=new Intent(getActivity().getPackageName()+".ADD_TASK_RECEIVED_ACTION");
				mIntent.putExtra("data", isSelected);
				mIntent.putExtra("action", "target_selected");
				getActivity().sendBroadcast(mIntent);
				getActivity().finish();
			}
		});
		//返回上一級
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)). 
			     hideSoftInputFromWindow(searchEt.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				getActivity().finish();
			}
		});
		searchEt.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEARCH) {
					 ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					 kzxTargetSelectedAdapter.clearDataForLoader();
                     pageNow=1;
                     postLoad();
                 }
                 return true;
			}
		});
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoad();
	}
	
	/**相关目标数据查询*/
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageNow", pageNow+"");//页码
		rp.put("searchContent",searchEt.getText().toString());
		asyncHttpClient.post(getActivity(), Constants.queryCanRelateAPI, rp, responseHandler);
	}
	
	/**相关目标数据查询回调*/
	AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler() {
		public void onStart() {
			super.onStart();
			if(kzxTargetSelectedAdapter.isEmpty()){
				footerView.setVisibility(View.GONE);
				default_load_view.setVisibility(View.VISIBLE);
			}else{
				footerView.setVisibility(View.VISIBLE);
				((ProgressBar)footerView.findViewById(R.id.default_load)).setVisibility(View.VISIBLE);
			}
		}
		@Override
		public void onFinish() {
			default_load_view.setVisibility(View.GONE);
			((ProgressBar)footerView.findViewById(R.id.default_load)).setVisibility(View.INVISIBLE);
			super.onFinish();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content=new String(responseBody);
			try {
				long pageSize=new JSONObject(content).optLong("pageSize",0);
				long rowCount=new JSONObject(content).optLong("rowCount",0);
				long totalPage = (rowCount + pageSize -1) / pageSize;
				String records=new JSONObject(content).optString("records");
				if (!StringUtils.isEmpty(records)&&!"[]".equals(records)) {
					List<TargetCanRelateBean> leaderList = new ArrayList<TargetCanRelateBean>();
					leaderList.addAll((List<TargetCanRelateBean>)gson.fromJson(records, tt));
					kzxTargetSelectedAdapter.setDataForLoader(leaderList,isLeaderHash);
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
