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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountManager;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSON;
import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.bean.AmountMemberBean;
import com.android.yijiang.kzx.bean.KzxTokenBean;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.MemberBean;
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
import com.android.yijiang.kzx.ui.AppManager;
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
 * 账户中心
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxAccountInfoFragment extends BaseFragment {

	private String TAG=KzxAccountInfoFragment.class.getName();
	
	private SwipeRefreshLayout swipeLayout;

	private static final String TAG_POSITION_1 = "1";
	private static final String TAG_POSITION_2 = "2";

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private Bitmap mBitmap;
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;

	private CircleImageView user_bg;
	private ImageView accountIv;
	private TextView accountTv;
	private LinearLayout accountBtn;
	private ImageView bottomDivider;
	private EditText nameTv;
	private EditText phoneTv;
	private EditText emailTv;
	private TextView growUpTv;
	private LinearLayout reportBtn;
	private EditText departmentTv;
	private TextView leaderNameTv;
	private ImageButton editBtn;

	private ScrollView container;
	private ProgressBar default_load_view;

	/** 统计视图 */
	private LinearLayout medalBtn;
	private TextView medalAmountTv;
	private LinearLayout leaderBtn;
	private TextView leaderAmountTv;
	private LinearLayout clientBtn;
	private TextView clientAmountTv;

	private AmountMemberBean amountMemberBean;

	private String leaderId;
	private String leaderName;

	private Dialog dialog;
	private AsyncHttpClient asyncHttpClient;
	private MessageReceiver mMessageReceiver;
	private HashMap<String, LeaderBean> isLeaderHash;// 汇报人数据

	// 头部View
	private ImageButton backBtn;
	private DisplayImageOptions options;
	public static KzxAccountInfoFragment newInstance() {
		KzxAccountInfoFragment fragment = new KzxAccountInfoFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		View contentView = inflater.inflate(R.layout.kzx_account_info_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		container = (ScrollView) view.findViewById(R.id.container);
		default_load_view = (ProgressBar) view.findViewById(R.id.default_load_view);
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			public void onRefresh() {
				postLoad();
			}
		});
		accountIv = (ImageView) view.findViewById(R.id.accountIv);
		accountTv = (TextView) view.findViewById(R.id.accountTv);
		accountBtn = (LinearLayout) view.findViewById(R.id.accountBtn);
		bottomDivider = (ImageView) view.findViewById(R.id.bottomDivider);
		medalBtn = (LinearLayout) view.findViewById(R.id.medalBtn);
		medalAmountTv = (TextView) view.findViewById(R.id.medalAmountTv);
		leaderBtn = (LinearLayout) view.findViewById(R.id.leaderBtn);
		leaderAmountTv = (TextView) view.findViewById(R.id.leaderAmountTv);
		clientBtn = (LinearLayout) view.findViewById(R.id.clientBtn);
		clientAmountTv = (TextView) view.findViewById(R.id.clientAmountTv);
		editBtn = (ImageButton) view.findViewById(R.id.editBtn);
		user_bg = (CircleImageView) view.findViewById(R.id.user_bg);
		growUpTv = (TextView) view.findViewById(R.id.growUpTv);
		nameTv = (EditText) view.findViewById(R.id.nameTv);
		phoneTv = (EditText) view.findViewById(R.id.phoneTv);
		emailTv = (EditText) view.findViewById(R.id.emailTv);
		reportBtn = (LinearLayout) view.findViewById(R.id.reportBtn);
		departmentTv = (EditText) view.findViewById(R.id.departmentTv);
		leaderNameTv = (TextView) view.findViewById(R.id.leaderNameTv);
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KeyBoardUtils.closeKeybord(nameTv, getActivity());
				getActivity().finish();
			}
		});
		// 编辑部门
		departmentTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					final String contentStr = v.getText().toString();
					if (!StringUtils.isEmpty(contentStr)) {
						RequestParams rp = new RequestParams();
						rp.put("department", contentStr);
						postUploadAccount(rp, Constants.uploadMemberAPI);
						departmentTv.setEnabled(false);
					}
				}
				return false;
			}
		});
		// 编辑名字
		nameTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					final String contentStr = v.getText().toString();
					if (!StringUtils.isEmpty(contentStr)) {
						RequestParams rp = new RequestParams();
						rp.put("name", contentStr);
						postUploadAccount(rp, Constants.uploadAccountAPI);
						nameTv.setEnabled(false);
					}
				}
				return false;
			}
		});
		
		// 编辑手机
		phoneTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					final String contentStr = v.getText().toString();
					if (!StringUtils.isEmpty(contentStr) && RegexUtils.checkMobile(contentStr)) {
						if(!amountMemberBean.getPhone().equals(contentStr)){
							postCheckPhone(contentStr);
						}else{
							phoneTv.setEnabled(false);
						}
					}
				}
				return false;
			}
		});
		
		// 编辑邮箱
		emailTv.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
					final String contentStr = v.getText().toString();
					if (!StringUtils.isEmpty(contentStr) && RegexUtils.checkEmail(contentStr)) {
						RequestParams rp = new RequestParams();
						rp.put("email", contentStr);
						postUploadAccount(rp, Constants.uploadAccountAPI);
						emailTv.setEnabled(false);
					}
				}
				return false;
			}
		});

		// 编辑汇报人
		reportBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), ContentFragmentActivity.class);
				mIntent.putExtra("data", isLeaderHash);
				mIntent.putExtra("action", "report_selected");
				startActivity(mIntent);
			}
		});

		// 编辑资料
		editBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!departmentTv.isEnabled()) {
					nameTv.setEnabled(true);
					emailTv.setEnabled(true);
					phoneTv.setEnabled(true);
					departmentTv.setEnabled(true);
					departmentTv.setHint(getResources().getString(ResourceMap.getString_department_hint()));
					nameTv.setHint(getResources().getString(ResourceMap.getString_name_hint()));
					emailTv.setHint(getResources().getString(ResourceMap.getString_email_hint()));
					phoneTv.setHint(getString(R.string.input_phone_hint));
					nameTv.requestFocus(); // EditText获得焦点
					KeyBoardUtils.openKeybord(nameTv, getActivity());
				} else {
					departmentTv.setHint("");
					nameTv.setHint("");
					emailTv.setHint("");
					phoneTv.setHint("");
					phoneTv.setEnabled(false);
					departmentTv.setEnabled(false);
					nameTv.setEnabled(false);
					emailTv.setEnabled(false);
				}
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

		// 编辑头像
		user_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelectedPicMode(v);
			}
		});
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 注册用于更换汇报人广播
		registerMessageReceiver();
	}

	/**选择上传头像模式*/ 
	private void showSelectedPicMode(View v) {
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setItems(getResources().getStringArray(R.array.avatar), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					gallery(user_bg);
					break;
				case 1:
					camera(user_bg);
					break;
				default:
					break;
				}
				dialog.dismiss();
			}
		}).create().show();
	}

	/**加载数据*/ 
	private void postLoad() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		asyncHttpClient.post(getActivity(), Constants.memberDetailAPI, rp, responseHandler);
	}

	/**加载数据回调*/ 
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			super.onStart();
			if (!StringUtils.isEmpty(nameTv.getText().toString())) {
				swipeLayout.setRefreshing(true);
			} else {
				default_load_view.setVisibility(View.VISIBLE);
				accountBtn.setVisibility(View.GONE);
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
			accountBtn.setVisibility(View.VISIBLE);
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
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
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
	
	/**验证手机号*/ 
	private void postCheckPhone(String phone) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("phone", phone);
		asyncHttpClient.post(getActivity(), Constants.queryByPhoneAPI, rp, checkResponseHandler);
	}

	/**验证手机号回调*/ 
	AsyncHttpResponseHandler checkResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success");
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (success) {
					String contentStr=phoneTv.getText().toString();
					DialogFragment myFragment = KzxChangePhoneDialogFragment.newInstance(contentStr);
					myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
					myFragment.show(getFragmentManager(), TAG);
				} else {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
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

	/**离开团队或者解散*/ 
	private void postLeave(String path) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		asyncHttpClient.post(getActivity(), path, rp, dismissResponseHandler);
	}

	/**离开团队或者解散回调*/ 
	AsyncHttpResponseHandler dismissResponseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
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
		};

		public void onFinish() {
			dialog.dismiss();
		};

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success");
				String message = new JSONObject(content).optString("message");
				int data = new JSONObject(content).optInt("data");
				if (success) {
					initDataForDrawer(message);
				} else {
					// 解散团队
					if (data == 1) {
						new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(message).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								postLeave(Constants.dismissCompanyAPI);
								dialog.dismiss();
							}
						}).create().show();
					}
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
	
	/**重新刷新个人中心信息和侧边栏信息*/ 
	private void initDataForDrawer(String message) {
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(message).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				postLoad();
				Intent mIntent = new Intent(getActivity().getPackageName() + ".DRAWER_RECEIVED_ACTION");
				mIntent.putExtra("action", "reload");
				getActivity().sendBroadcast(mIntent);
				dialog.dismiss();
			}
		}).create().show();
	}

	/**初始化更新个人信息*/ 
	private void initData(AmountMemberBean amountMemberBean) {
		String leaderName = StringUtils.isEmpty(amountMemberBean.getLeaderName()) ? "" : amountMemberBean.getLeaderName();
		nameTv.setText(amountMemberBean.getName());
		phoneTv.setText(amountMemberBean.getPhone());
		emailTv.setText(amountMemberBean.getEmail());
		departmentTv.setText(amountMemberBean.getDepartment());
		leaderNameTv.setText(getResources().getString(ResourceMap.getString_huijibaoren_hint()) + leaderName);
		growUpTv.setText("LV " + amountMemberBean.getGrowUp());
		medalAmountTv.setText(String.valueOf(amountMemberBean.getMedalCount()));
		leaderAmountTv.setText(String.valueOf(amountMemberBean.getTaskLeaderGoodCount()));
		clientAmountTv.setText(String.valueOf(amountMemberBean.getTaskClientGoodCount()));
//		Picasso.with(getActivity()).load(amountMemberBean.getIcon()) //
//				.placeholder(R.drawable.default_bg) //
//				.error(R.drawable.default_bg) //
//				.into(uiTarget);
		ImageLoader.getInstance().displayImage(amountMemberBean.getIcon(), user_bg, options);
		// 处理底部菜单
		if (!StringUtils.isEmpty(amountMemberBean.getMemberId())) {
			accountIv.getDrawable().setLevel(0);
			accountTv.setText(getResources().getString(ResourceMap.getString_leaver_team()));
			accountBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(R.string.leave_team_title).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							postLeave(Constants.leaveCompanyAPI);
							dialog.dismiss();
						}
					}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
						}
					}).create().show();
				}
			});
		} else {
			accountIv.getDrawable().setLevel(1);
			accountTv.setText(getResources().getString(ResourceMap.getString_create_team()));
			accountBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), ContentFragmentActivity.class);
					i.putExtra("action", "create_team");
					getActivity().startActivity(i);
				}
			});
		}
	}

	/**头像和头像背景回调*/ 
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

	/**跳转到登录界面*/ 
	private void gotoLogin() {
		SharedPreferences sp = getActivity().getSharedPreferences("token_info", 0);
		boolean isSuccess = sp.edit().clear().commit();
		if (isSuccess) {
			JPushInterface.clearAllNotifications(getActivity());
			JPushInterface.stopPush(getActivity());
			Intent i = new Intent(getActivity(), LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			getActivity().startActivity(i);
		}
	}

	/**上传资料*/ 
	private void postUploadAccount(RequestParams rp, String path) {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), path, rp, accountResponseHandler);
	}

	/**上传头像*/ 
	private void postUpload(File... files) throws FileNotFoundException {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		for (File file : files) {
			rp.put("Filedata", file);
		}
		asyncHttpClient.post(getActivity(), Constants.uploadAvaterAPI, rp, uploadResponseHandler);
	}

	/**上传资料回调*/ 
	AsyncHttpResponseHandler accountResponseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");// 1:不用,2:重新登录
				if (success) {
					SharedPreferences sp = getActivity().getSharedPreferences("kzx_token_info", 0);
					sp.edit().putString("department", departmentTv.getText().toString()).commit();
					sp.edit().putString("accountName", nameTv.getText().toString()).commit();
					sp.edit().putString("phone", phoneTv.getText().toString()).commit();
					sp.edit().putString("email", emailTv.getText().toString()).commit();
					if (!StringUtils.isEmpty(leaderId) && !StringUtils.isEmpty(leaderName)) {
						sp.edit().putString("leader", leaderId).commit();
						sp.edit().putString("leaderName", leaderName).commit();
					}
					if (TAG_POSITION_1.equals(data)) {
						Intent mIntent = new Intent(getActivity().getPackageName() + ".DRAWER_RECEIVED_ACTION");
						mIntent.putExtra("action", "name");
						mIntent.putExtra("name", nameTv.getText().toString());
						getActivity().sendBroadcast(mIntent);
					} else if (TAG_POSITION_2.equals(data)) {
						gotoLogin();
					}
				}
				MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
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

	/**上传图片回调*/ 
	AsyncHttpResponseHandler uploadResponseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
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
		};

		public void onFinish() {
			dialog.dismiss();
		};

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (!success) {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				} else {
					MsgTools.toast(getActivity(), getResources().getString(ResourceMap.getString_upload_success()), 3000);
					// 更新首页侧滑信息
					Intent mIntent = new Intent(getActivity().getPackageName() + ".DRAWER_RECEIVED_ACTION");
					mIntent.putExtra("action", "avater");
					mIntent.putExtra("avater", data);
					getActivity().sendBroadcast(mIntent);
					// 上传成功后删除多余的图片
					if (tempFile != null && tempFile.exists()) {
						tempFile.delete();
					}
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

	/**注册广播*/ 
	public void registerMessageReceiver() {
		postLoad();
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getActivity().getPackageName() + ".INVITE_RECEIVED_ACTION");
		getActivity().registerReceiver(mMessageReceiver, filter);
	}

	/**回调广播*/ 
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getActivity().getPackageName() + ".INVITE_RECEIVED_ACTION").equals(intent.getAction())) {
				isLeaderHash = (HashMap<String, LeaderBean>) intent.getSerializableExtra("data");
				for (Map.Entry<String, LeaderBean> entry : isLeaderHash.entrySet()) {
					LeaderBean leaderBean = entry.getValue();
					leaderName = leaderBean.getName();
					leaderId = leaderBean.getId();
				}
				// 刷新页面
				leaderNameTv.setText(getResources().getString(ResourceMap.getString_huijibaoren_hint()) + leaderName);
				// 提交数据
				RequestParams rp = new RequestParams();
				rp.put("leader", leaderId);
				postUploadAccount(rp, Constants.uploadMemberAPI);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				crop(uri);
			}
		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (!FileUtil.hasSdcard()) {
				MsgTools.toast(getActivity(), getResources().getString(ResourceMap.getString_upload_error()), Toast.LENGTH_SHORT);
				return;
			}
			tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
			crop(Uri.fromFile(tempFile));
		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				mBitmap = data.getParcelableExtra("data");
				refreshAvater(mBitmap);
				if (tempFile != null && tempFile.exists()) {
					tempFile.delete();
				}
				tempFile = FileUtil.getFileFromBytes(mBitmap);
				postUpload(tempFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 刷新头像和背景图 */
	public void refreshAvater(Bitmap mBitmap) {
		user_bg.setImageBitmap(mBitmap);
	}

	/** 从相册获取 */
	public void gallery(View view) {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/** 从相机获取 */
	public void camera(View view) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (FileUtil.hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	/** 剪切图片 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
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
