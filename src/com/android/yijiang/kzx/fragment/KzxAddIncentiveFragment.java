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
import java.util.Map;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.yijiang.kzx.adapter.KzxMedalConfigAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MedalConfigBean;
import com.android.yijiang.kzx.bean.ServicerBean;
import com.android.yijiang.kzx.fragment.KzxInviteFragment.TimeCount;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
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
 * 添加激励认可
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxAddIncentiveFragment extends BaseFragment {

	private String TAG = "KzxAddIncentiveFragment";
	private KzxMedalConfigAdapter kzxMedalConfigAdapter;
	private TextView nameTv;
	private CircleImageView user_bg;
	private GridView dateCustomGrid;
	private TextView btn_add_new;
	private SwipeRefreshLayout swipeLayout;
	private int position;
	
	public static final int resultCode=1;
	private HashMap<String, LeaderBean> isLeaderHash;// 获奖人数据
	private List<String> copytoList=new ArrayList<String>();
	private String leaderId;
	private String leaderName;
	
	private ProgressBar default_load_view;
	private TextView img_empty_feed;

	private AsyncHttpClient asyncHttpClient;
	private Gson gson;
	private Type tt;
	private LinearLayout personBtn;
	private MedalBean medalBean;
	private Dialog dialog;

	// 头部View
	private ImageButton backBtn;
	private DisplayImageOptions options;
	
	public static KzxAddIncentiveFragment newInstance(MedalBean medalBean, int position) {
		KzxAddIncentiveFragment fragment = new KzxAddIncentiveFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", 0);
		bundle.putSerializable("medalBean", medalBean);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new GsonBuilder().create();
		tt = new TypeToken<List<MedalConfigBean>>() { }.getType();
		medalBean = (MedalBean) getArguments().getSerializable("medalBean");
		position = getArguments().getInt("position", 0);
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
		View contentView = inflater.inflate(R.layout.kzx_incentive_add_medal_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		default_load_view=(ProgressBar) view.findViewById(R.id.default_load_view);
		img_empty_feed=(TextView)view.findViewById(R.id.img_empty_feed);
		btn_add_new=(TextView)view.findViewById(R.id.btn_add_new);
		personBtn=(LinearLayout) view.findViewById(R.id.personBtn);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark);
		kzxMedalConfigAdapter = new KzxMedalConfigAdapter(getActivity());
		dateCustomGrid = (GridView) view.findViewById(R.id.dateCustomGrid);
		nameTv = (TextView) view.findViewById(R.id.nameTv);
		user_bg = (CircleImageView) view.findViewById(R.id.user_bg);
		dateCustomGrid.setAdapter(kzxMedalConfigAdapter);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				postLoad();
			}
		});
		kzxMedalConfigAdapter.setOnItemClickListener(new KzxMedalConfigAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(MedalConfigBean medalConfigBean) {
//				DialogFragment myFragment = KzxAddMedalDialogFragment.newInstance(medalConfigBean, medalBean, position);
//				myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle);
//				myFragment.show(getFragmentManager(), TAG);
//				postAddMedal(title,content,medalConfigBean);
				showAddMedalDialog(medalConfigBean);
			}
		});
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		personBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(v.getContext(),ContentFragmentActivity.class);
				mIntent.putExtra("action", "medal_person_selected");
				mIntent.putExtra("data", isLeaderHash);
				mIntent.putExtra("typeStr", "single-select");
				getActivity().startActivityForResult(mIntent, resultCode);
			}
		});
		initHeaderViewData(medalBean.getName(),medalBean.getIcon());
		setLeaderId(medalBean.getId());
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoad();
	}
	
	/**初始化要显示的内容*/
	private void initHeaderViewData(String name,String icon){
//		Picasso.with(getActivity()) //
//				.load(icon) //
//				.placeholder(R.drawable.default_bg) //
//				.error(R.drawable.default_bg) //
//				.into(user_bg);
		ImageLoader.getInstance().displayImage(icon, user_bg, options);
		nameTv.setText(name);
	}


	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
		copytoList.clear();
		copytoList.add(leaderId);
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	/**勋章配置信息*/
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		asyncHttpClient.post(getActivity(), Constants.queryMedalconfigAPI, rp, responseHandler);
	}
	
	/**勋章配置信息回调*/
	AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			if(kzxMedalConfigAdapter.getCount()>0){
				swipeLayout.setRefreshing(true);
			}else{
				default_load_view.setVisibility(View.VISIBLE);
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
				String records = new JSONObject(content).optString("data");
				if (!StringUtils.isEmpty(records) && !"[]".equals(records)) {
					List<MedalConfigBean> servicerList = new ArrayList<MedalConfigBean>();
					servicerList.addAll((List<MedalConfigBean>) gson.fromJson(records, tt));
					kzxMedalConfigAdapter.setDataForLoader(servicerList);
				}else{
					dateCustomGrid.setEmptyView(img_empty_feed);
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
	
	private void showAddMedalDialog(final MedalConfigBean medalConfigBean) {
		final View contentView = getActivity().getLayoutInflater().inflate(R.layout.kzx_add_medal_holo_dialog_fragment, null);
		final ImageView medalIcon=(ImageView)contentView.findViewById(R.id.medalIcon);
		final EditText titleEt = (EditText) contentView.findViewById(R.id.titleEt);
		final EditText contentEt = (EditText) contentView.findViewById(R.id.contentEt);
		final TextView btnSure=(TextView)contentView.findViewById(R.id.btnSure);
		titleEt.setText(medalConfigBean.getName());
		Picasso.with(getActivity()) //
		.load(Constants.locationAPI+KzxMedalConfigAdapter.imageUrl+medalConfigBean.getIcon().replace("128", "256")) //
		.placeholder(R.drawable.default_bg) //
		.error(R.drawable.default_bg) //
		.into(medalIcon);
		final Dialog alertDialog=new Dialog(getActivity(),R.style.mystyle);
		alertDialog.setContentView(contentView);
		alertDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				KeyBoardUtils.closeKeybord(titleEt, getActivity());
				dialog.dismiss();
			}
		});
		alertDialog.show();
		btnSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String titleStr=titleEt.getText().toString();
				String contentStr=contentEt.getText().toString();
				if(StringUtils.isEmpty(titleStr)){
					return;
				}
				postAddMedal(titleStr,contentStr,medalConfigBean);
				KeyBoardUtils.closeKeybord(titleEt, getActivity());
				alertDialog.dismiss();
			}
		});
		titleEt.requestFocus();
		titleEt.postDelayed(new Runnable() {
			@Override
			public void run() {
				KeyBoardUtils.openKeybord(titleEt, getActivity());
			}
		}, 300);
	}
	
	/**添加勋章请求*/ 
	private void postAddMedal(String Name,String Reason,MedalConfigBean medalConfigBean){
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("copyto", new Gson().toJson(copytoList));
		rp.put("ConfigName", Name);
		rp.put("Reason", Reason);
		rp.put("ConfigId", medalConfigBean.getId());
		asyncHttpClient.post(getActivity(), Constants.addMedalAPI, rp, addMedalResponseHandler);
	}
	
	/**添加勋章请求回调*/ 
	private AsyncHttpResponseHandler addMedalResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			dialog = new Dialog(getActivity(), R.style.mystyle);
			dialog.setContentView(R.layout.loading_dialog);
			dialog.setCancelable(true);
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					asyncHttpClient.cancelRequests(getActivity(), true);
				}
			});
			dialog.show();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				if (success) {
					// 关闭当前页
					getActivity().finish();
					// 刷新发起信息页面
					medalBean.setMedalCount(medalBean.getMedalCount()+1);
					Intent mIntent=new Intent(getActivity().getPackageName()+".INCENTIVE_RECEIVED_ACTION");
					mIntent.putExtra("action", "reload");
					mIntent.putExtra("position", position);
					mIntent.putExtra("medalBean", medalBean);
					getActivity().sendBroadcast(mIntent);
				} 
				MsgTools.toast(getActivity(), message, 3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void onFinish() {
			if(dialog!=null&&dialog.isShowing()){
				dialog.dismiss();
			}
		};
		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==KzxAddIncentiveFragment.resultCode&&data!=null){
			isLeaderHash=(HashMap<String, LeaderBean>) data.getSerializableExtra("isSelected");
			for (Map.Entry<String, LeaderBean> entry : isLeaderHash.entrySet()) {
				LeaderBean leaderBean = entry.getValue();
				setLeaderId(leaderBean.getId());
				setLeaderName(leaderBean.getName());
				initHeaderViewData(leaderBean.getName(),leaderBean.getIcon());
			}
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
