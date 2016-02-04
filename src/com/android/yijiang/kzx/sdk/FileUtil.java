package com.android.yijiang.kzx.sdk;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

public class FileUtil {

	public static String renameFile() {
		StringBuffer newFileName = new StringBuffer();
		newFileName.append(DateUtil.format(new Date(), "yyyyMMddHHmmssms")).append(".").append("jpg");
		return newFileName.toString();
	}
	
	private byte[] InputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}
	
	public static String getFileByUri(final Context context, final Uri uri ){
		if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}

	public static File getFileFromBytes(Bitmap bm) {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return null;
		}
		File SDCardRoot = Environment.getExternalStorageDirectory();
		if (SDCardRoot.getFreeSpace() < 10000) {
			Log.e("Utils", "存储空间不够");
			return null;
		}
		byte[] b = Bitmap2Bytes(bm);
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(Environment.getExternalStorageDirectory(),renameFile());
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}
	
	public static File getFileFromUrl() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return null;
		}
		File SDCardRoot = Environment.getExternalStorageDirectory();
		if (SDCardRoot.getFreeSpace() < 10000) {
			Log.e("Utils", "存储空间不够");
			return null;
		}
		File file = new File(Environment.getExternalStorageDirectory(),renameFile());
		return file;
	}

	private static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
	/** SD卡获取 */
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
