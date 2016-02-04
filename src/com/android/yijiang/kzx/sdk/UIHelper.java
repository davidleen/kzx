package com.android.yijiang.kzx.sdk;

import java.net.URLEncoder;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class UIHelper {

	/** 全局web样式 */
	public final static String WEB_STYLE = "<style>* {font-size:22px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;text-align:center;} "
			+ "img.alignleft {float:left;text-align:center;max-width:120px;margin:0 10px 0px 10px;border:1px solid #ccc;background:#fff;padding:2px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} "
			+ "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
	private static String urls;

	/**
	 * 添加网页的点击图片展示支持
	 */
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public static void addWebImageShow(final Context cxt, WebView wv) {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.addJavascriptInterface(new OnWebViewImageListener() {

			@Override
			public void onImageClick(String bigImageUrl) {
				if (bigImageUrl != null)
					UIHelper.showImageZoomDialog(cxt, bigImageUrl);
			}
		}, "mWebViewImageListener");
	}

	/**
	 * 显示图片对话框
	 * 
	 * @param context
	 * @param imgUrl
	 */

	public static void showImageZoomDialog(Context context, String imgUrl) {
//		Intent intent = new Intent(context, PhotoViewActivity.class);
//		intent.putExtra("path", imgUrl);
//		context.startActivity(intent);
	}

	/**
	 * 获取webviewClient对象
	 * 
	 * @return
	 */
	public static WebViewClient getWebViewClient() {
		return new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}

	/**
	 * url跳转
	 * 
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url) {
		if (url.startsWith("http://") || url.startsWith("https://"))
			urls = "http://" + URLEncoder.encode(url);
		openBrowser(context, urls);
	}

	/**
	 * 打开浏览器
	 * 
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url) {
		try {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			ToastMessage(context, "无法浏览此网页", 500);
		}
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}
}
