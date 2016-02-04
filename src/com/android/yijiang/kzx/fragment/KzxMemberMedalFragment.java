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
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxIncentiveAdapter;
import com.android.yijiang.kzx.adapter.KzxMemberMedalAdapter;
import com.android.yijiang.kzx.adapter.KzxNoticeAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.bean.AmountMemberBean;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MemberBean;
import com.android.yijiang.kzx.bean.MemberMedalBean;
import com.android.yijiang.kzx.bean.NoticeBean;
import com.android.yijiang.kzx.bean.PastCompanyBean;
import com.android.yijiang.kzx.bean.PastMemberListBean;
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
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.android.yijiang.kzx.widget.imageview.RoundedCornerImageView;
import com.android.yijiang.kzx.widget.parallaxheader.ScrollTabHolderFragment;
import com.android.yijiang.kzx.widget.rotate.RotateImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 成员勋章信息(按公司分组)
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxMemberMedalFragment extends BaseFragment {

	private SwipeRefreshLayout swipeLayout;
	
	private boolean mHasRequestedMore;
	private long pageNow=1;
	private View footerView;
	private View headerView;
	private LinearLayout headerLayout;
	
	private ListView dateCustomList;
	private ProgressBar default_load_view;
	private TextView img_empty_feed;
	private AmountMemberBean amountMemberBean;
	private List<PastCompanyBean> pastCompanyList=new ArrayList<PastCompanyBean>();

	private String companyId,memberId;
	private Gson gson ;
	private Type tt ;
	private KzxMemberMedalAdapter kzxMemberMedalAdapter;
	private AsyncHttpClient asyncHttpClient;
	
	private ImageButton backBtn;
	private DisplayImageOptions options;
	
	public static KzxMemberMedalFragment newInstance(AmountMemberBean amountMemberBean) {
		KzxMemberMedalFragment fragment = new KzxMemberMedalFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("amountMemberBean", amountMemberBean);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new GsonBuilder().create();
		tt=new TypeToken<List<MemberMedalBean>>() {}.getType();
		amountMemberBean=(AmountMemberBean) getArguments().getSerializable("amountMemberBean");
		pastCompanyList.add(new PastCompanyBean(amountMemberBean.getCompanyIcon(), amountMemberBean.getCompanyId(),amountMemberBean.getMemberId(), amountMemberBean.getCompanyName(),amountMemberBean.getMedalCount(),null,null));
		for (PastMemberListBean pastMemberListBean : amountMemberBean.getPastMemberList()) {
			pastCompanyList.add(new PastCompanyBean(pastMemberListBean.getCompanyIcon(), pastMemberListBean.getCompanyId(),pastMemberListBean.getMember().getId(), pastMemberListBean.getCompanyName(),pastMemberListBean.getMember().getMedalCount(),null,null));
		}
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
		View contentView = inflater.inflate(R.layout.kzx_member_medal_fragment, null);
		return contentView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				pageNow=1;
				postLoad(true);
			}
		});
		headerView=getLayoutInflater(savedInstanceState).inflate(R.layout.kzx_passcompany_header_fragment, null);
		headerLayout=(LinearLayout) headerView.findViewById(R.id.headerLayout);
		footerView=getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_lv_footer, null);
		footerView.setVisibility(View.INVISIBLE);
		kzxMemberMedalAdapter=new KzxMemberMedalAdapter(getActivity());
		default_load_view=(ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed=(TextView) view.findViewById(R.id.img_empty_feed);
		dateCustomList=(ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.addFooterView(footerView, null, false);
		dateCustomList.addHeaderView(headerView, null, false);
		dateCustomList.setAdapter(kzxMemberMedalAdapter);
		dateCustomList=(ListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!mHasRequestedMore) {
					int lastInScreen = firstVisibleItem + visibleItemCount;
					if (lastInScreen >= totalItemCount && totalItemCount != 0 && totalItemCount != dateCustomList.getHeaderViewsCount() + dateCustomList.getFooterViewsCount() && kzxMemberMedalAdapter.getCount() > 0) {
						postLoad(false);
						mHasRequestedMore = true;
					}
				}
			}
		});		
		dateCustomList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			}
		});
		//返回上一級
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		createHeaderViewUI();
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoad(true);
	}
	
	/**构造ListView HeaderView 数据*/
	private void createHeaderViewUI(){
		for (final PastCompanyBean pastCompanyBean : pastCompanyList) {
			final View uiView=LayoutInflater.from(getActivity()).inflate(R.layout.kzx_passcompany_header_item_fragment, null);
			CircleImageView company_bg=(CircleImageView) uiView.findViewById(R.id.company_bg);
			TextView nameTv=(TextView) uiView.findViewById(R.id.nameTv);
			TextView amountTv=(TextView) uiView.findViewById(R.id.amountTv);
			ImageView flagIv=(ImageView) uiView.findViewById(R.id.flagIv);
//			Picasso.with(getActivity()) //
//			.load(pastCompanyBean.getCompanyIcon()) //
//			.placeholder(R.drawable.default_bg) //
//			.error(R.drawable.default_bg) //
//			.into(company_bg);
			ImageLoader.getInstance().displayImage(pastCompanyBean.getCompanyIcon(), company_bg, options);
			nameTv.setText(pastCompanyBean.getCompanyName());
			amountTv.setText(pastCompanyBean.getCompanyAmount()+"");
			if(pastCompanyBean.getCompanyId().equals(amountMemberBean.getCompanyId())){
				flagIv.getDrawable().setLevel(1);
				headerLayout.setTag(pastCompanyBean.getCompanyId());
				setParamsForLoad(pastCompanyBean);
			}
			uiView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					View oldView=headerLayout.findViewWithTag(companyId);
					((ImageView)oldView.findViewById(R.id.flagIv)).getDrawable().setLevel(0);
					((ImageView)uiView.findViewById(R.id.flagIv)).getDrawable().setLevel(1);
					uiView.setTag(pastCompanyBean.getCompanyId());
					setParamsForLoad(pastCompanyBean);
					pageNow=1;
					postLoad(true);
				}
			});
			headerLayout.addView(uiView);
		}
	}
	
	/**查询参数的设置*/
	private void setParamsForLoad(PastCompanyBean pastCompanyBean){
		companyId=pastCompanyBean.getCompanyId();
		memberId=pastCompanyBean.getMemberId();
	}
	
	/**加载勋章信息*/
	private void postLoad(final boolean isFirstLoad) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("pageNow", pageNow+"");//页码
		rp.put("memberId",memberId);
		rp.put("companyId",companyId);
		rp.put("sortType", "CreateTime desc");
		asyncHttpClient.post(getActivity(), Constants.queryMedalByMemberAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				if(isFirstLoad){
					if(kzxMemberMedalAdapter.getCount()>0){
						swipeLayout.setRefreshing(true);
					}else{
						footerView.setVisibility(View.GONE);
						img_empty_feed.setVisibility(View.GONE);
						default_load_view.setVisibility(View.VISIBLE);
					}
				}else{
					footerView.setVisibility(View.VISIBLE);
					((ProgressBar)footerView.findViewById(R.id.default_load)).setVisibility(View.VISIBLE);
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
				String content=new String(responseBody);
				try {
					long pageSize=new JSONObject(content).optLong("pageSize",0);
					long rowCount=new JSONObject(content).optLong("rowCount",0);
					long totalPage = (rowCount + pageSize -1) / pageSize;
					String records=new JSONObject(content).optString("records");
					if (!StringUtils.isEmpty(records)&&!"[]".equals(records)) {
						List<MemberMedalBean> noticeList = new ArrayList<MemberMedalBean>();
						noticeList.addAll((List<MemberMedalBean>)gson.fromJson(records, tt));
						kzxMemberMedalAdapter.setDataForLoad(noticeList,isFirstLoad);
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
		super.onDestroy();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
	}

}
