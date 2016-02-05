package com.android.yijiang.kzx.fragment;

import java.io.ByteArrayOutputStream;
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
import java.util.Random;
import java.util.UUID;

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
import android.content.ComponentName;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
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
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 关于快执行
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxAboutFragment extends Fragment {

	private LinearLayout telBtn;
	private LinearLayout emailBtn;
	private LinearLayout websiteBtn;
	private LinearLayout wechatBtn;
	
	
	 IWXAPI weixiApi;
	 
	 public String[] messages;
	 public Random random;

	public static KzxAboutFragment newInstance() {
		KzxAboutFragment fragment = new KzxAboutFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		   String appId="wx7fd4fc5f42a07e7d";
	        weixiApi = WXAPIFactory.createWXAPI(getActivity(),appId,true);
	        weixiApi.registerApp(appId);
	        
	        messages=getResources().getStringArray(R.array.message_list);
	        random=new Random();
	}

	 @Override
	public void onDestroy() {

	        weixiApi.unregisterApp();
	        super.onDestroy();
	    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_about_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		telBtn = (LinearLayout) view.findViewById(R.id.telBtn);
		emailBtn = (LinearLayout) view.findViewById(R.id.emailBtn);
		websiteBtn = (LinearLayout) view.findViewById(R.id.websiteBtn);
		wechatBtn = (LinearLayout) view.findViewById(R.id.wechatBtn);
		telBtn.setOnClickListener(onClickListener);
		emailBtn.setOnClickListener(onClickListener);
		websiteBtn.setOnClickListener(onClickListener);
		wechatBtn.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.telBtn:
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "059163123239")));
				break;
			case R.id.emailBtn:
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL, new String[] { "service@larrymanage.com" });
				i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
				i.putExtra(Intent.EXTRA_TEXT, "body of email");
				startActivity(Intent.createChooser(i, "Send mail..."));
				break;
			case R.id.websiteBtn:
				Uri uri = Uri.parse("http://www.kuaizhixing.com");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				break;
			case R.id.wechatBtn:
				//shareToFriend(getString(R.string.about_wechat_website_hint));
				//shareToTimeLine();
				 
			//	sendMessageToWX( );
				sendMessageToWXTimeLine();
				break;
			default:
				break;
			}
		}
	};

	private void shareToFriend(String text) {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.SEND");
		intent.setType("text/*");
		// intent.setFlags(0x3000001);
		//intent.putExtra(Intent.EXTRA_TEXT, text);
		
		Uri uri = Uri.parse("android.resource://com.android.yijiang.kzx/drawable/app_icon_2");
		try {
			InputStream stream = getContext().getContentResolver().openInputStream(uri);
			stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		intent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(intent);
	}

	private void shareToTimeLine(File file) {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.SEND");
		intent.setType("image/*");
		// intent.setFlags(0x3000001);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		startActivity(intent);
	}
	
	private void shareToTimeLine( ) {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.SEND");
		intent.setType("image/*");
		// intent.setFlags(0x3000001);
		Uri uri = Uri.parse("android.resource://com.android.yijiang.kzx/drawable/app_icon_2");
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(intent);
	}

	
	/**
	 * 向微信发送网页分享给朋友
	 */
	  private void sendMessageToWX( )
	    {
	        WXWebpageObject webpage = new WXWebpageObject();
	        webpage.webpageUrl = getString(R.string.about_wechat_website_hint);
	        WXMediaMessage msg = new WXMediaMessage(webpage);
	        msg.title = "我们的团队正在使用\""+getResources().getString(R.string.app_name)+"\"";
	        
	        msg.description = messages[random.nextInt(messages.length)];
	        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_2);
	        msg.thumbData = bitmap2Byte(thumb);  // 设置缩略图
	        SendMessageToWX.Req req = new SendMessageToWX.Req();
	        req.transaction = buildTransaction("webpage");
	        req.message = msg;
	        
	      req.scene = SendMessageToWX.Req.WXSceneSession;
	        
	        weixiApi.sendReq(req);
	    }
	  /**
		 * 向微信发送网页分享朋友圈
		 */
		  private void sendMessageToWXTimeLine()
		    {
		        WXWebpageObject webpage = new WXWebpageObject();
		        webpage.webpageUrl = getString(R.string.about_wechat_website_hint);
		        WXMediaMessage msg = new WXMediaMessage(webpage);
		        msg.title = "我们的团队正在使用\""+getResources().getString(R.string.app_name)+"\"。\r\n  "+messages[random.nextInt(messages.length)];
		        
		        //msg.description = ;
		        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_2);
		        msg.thumbData = bitmap2Byte(thumb);  // 设置缩略图
		        SendMessageToWX.Req req = new SendMessageToWX.Req();
		        req.transaction = buildTransaction("webpage");
		        req.message = msg;
		        req.scene = SendMessageToWX.Req.WXSceneTimeline;
		       
		        weixiApi.sendReq(req);
		    }
	  
	  
	  private void setImageToWx()
	  {
		  
		  
		  Bitmap loadedImage=null;
		  WXImageObject imgObj = new WXImageObject(loadedImage);
		  //
		  //
		                  WXMediaMessage msg = new WXMediaMessage();
		                  msg.mediaObject = imgObj;
		                  int THUMB_SIZE = 150;
		                  Bitmap thumbBmp = Bitmap.createScaledBitmap(loadedImage, THUMB_SIZE, THUMB_SIZE, true);
		                  msg.thumbData = bitmap2Byte(thumbBmp);  // 设置缩略图
		                  thumbBmp.recycle();
		  
		  
		                  msg.title="title";
		                  msg.description="description";
		  
		                  SendMessageToWX.Req req = new SendMessageToWX.Req();
		                  req.transaction = buildTransaction("img");
		                  req.message = msg;
		                  req.scene = SendMessageToWX.Req.WXSceneTimeline;
		                  weixiApi.sendReq(req);
		  
	  }
	
	  private String buildTransaction(final String type) {
	        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	    }
	  public static byte[] bitmap2Byte(Bitmap bitmap)
	    {


	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
	       byte[] data = bos.toByteArray();
	        try {
	            bos.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return  data;
	    }
	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	 

}
