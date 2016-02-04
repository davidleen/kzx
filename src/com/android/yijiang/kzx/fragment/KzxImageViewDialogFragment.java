package com.android.yijiang.kzx.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONObject;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskProcessBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ArithUtils;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.FileSize;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 快执行选择图片弹出框
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxImageViewDialogFragment extends DialogFragment {

	private String filePath;

	private TextView titleTv;
	private TextView sizeTv;
	private ImageButton closeBtn;
	private ImageView imageViewBg;
	private Button sendBtn;

	private String broadcast;
	private boolean isOld;

	public static KzxImageViewDialogFragment newInstance(boolean isOld,String filePath,String broadcast) {
		KzxImageViewDialogFragment fragment = new KzxImageViewDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("broadcast", broadcast);
		bundle.putBoolean("isOld", isOld);
		bundle.putString("filePath", filePath);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		closeBtn = (ImageButton) view.findViewById(R.id.closeBtn);
		titleTv=(TextView)view.findViewById(R.id.titleTv);
		sizeTv=(TextView)view.findViewById(R.id.sizeTv);
		imageViewBg = (ImageView) view.findViewById(R.id.imageViewBg);
		sendBtn = (Button) view.findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(getActivity().getPackageName()+"."+broadcast);
				mIntent.putExtra("action", "add_taskprocess");
				mIntent.putExtra("filePath", filePath);
				mIntent.putExtra("isOld", isOld);
				getActivity().sendBroadcast(mIntent);
				dismiss();
			}
		});
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		titleTv.setText(getString(R.string.file_name)+new File(filePath).getName());
		sizeTv.setText(getString(R.string.file_size)+new FileSize(new File(filePath)).toString());
		Picasso.with(getActivity()).load(new File(filePath))
		.placeholder(R.drawable.default_bg)
		.error(R.drawable.default_bg)
		.into(showTarget);
	}

	Target showTarget=new Target() {
		@Override
		public void onPrepareLoad(Drawable arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onBitmapLoaded(Bitmap mBitmap, LoadedFrom arg1) {
			imageViewBg.setImageBitmap(mBitmap);
		}
		@Override
		public void onBitmapFailed(Drawable arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		filePath = getArguments().getString("filePath");
		broadcast=getArguments().getString("broadcast");
		isOld=getArguments().getBoolean("isOld");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.kzx_imageview_dialog_fragment, container, false);
		return view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}
}
