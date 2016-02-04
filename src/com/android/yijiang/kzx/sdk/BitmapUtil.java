package com.android.yijiang.kzx.sdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Shader;
import android.os.Environment;

@SuppressLint("NewApi") public class BitmapUtil {

	// 压缩图片
	public static byte[] getBitMapByte(Bitmap photoBitMap) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			// 压缩图片
			photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photodata = baos.toByteArray();
			return photodata;
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		BitmapDrawable bd = (BitmapDrawable) drawable;

		Bitmap bitmap = bd.getBitmap();
		return bitmap;
	}

	/**
	 * Method used to circle a bitmap.
	 * 
	 * @param bitmap
	 *            The bitmap to circle
	 * @return The circled bitmap
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		int size = Math.min(bitmap.getWidth(), bitmap.getHeight());

		Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		BitmapShader shader;
		shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);

		RectF rect = new RectF(0, 0, size, size);
		int radius = size / 2;
		canvas.drawRoundRect(rect, radius, radius, paint);
		return output;
	}

	public static Bitmap resizeBitMapImage1(String filePath, int targetWidth, int targetHeight) {
		Bitmap bitMapImage = null;
		// First, get the dimensions of the image
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		double sampleSize = 0;
		// Only scale if we need to
		// (16384 buffer for img processing)
		Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth);
		if (options.outHeight * options.outWidth * 2 >= 1638) {
			// Load, scaling to smallest power of 2 that'll get it <= desired
			// dimensions
			sampleSize = scaleByHeight ? options.outHeight / targetHeight : options.outWidth / targetWidth;
			sampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
		}
		// Do the actual decoding
		options.inJustDecodeBounds = false;
		options.inTempStorage = new byte[128];
		while (true) {
			try {
				options.inSampleSize = (int) sampleSize;
				bitMapImage = BitmapFactory.decodeFile(filePath, options);
				break;
			} catch (Exception ex) {
				try {
					sampleSize = sampleSize * 2;
				} catch (Exception ex1) {
				}
			}
		}
		return bitMapImage;
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outBitmap);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPX = bitmap.getWidth() / 2 < bitmap.getHeight() / 2 ? bitmap.getWidth() : bitmap.getHeight();
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// paint.setARGB(255, 138, 43, 226);
		// paint.setStrokeWidth(2);
		// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getWidth() / 2,
		// dip2px(context, 83) + dip2px(context, 10), paint);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return outBitmap;
	}

	/**
	 * ��˹ģ��
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap convertToBlur(Bitmap bmp) {
		// ��˹����
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixColor = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int delta = 16; // ֵԽСͼƬ��Խ����Խ����Խ��
		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						newR = newR + pixR * gauss[idx];
						newG = newG + pixG * gauss[idx];
						newB = newB + pixB * gauss[idx];
						idx++;
					}
				}
				newR /= delta;
				newG /= delta;
				newB /= delta;
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		return newBmp;
	}

	/**
	 * ģ��Ч��
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImage(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int newColor = 0;

		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++) {
			for (int k = 1, len = height - 1; k < len; k++) {
				for (int m = 0; m < 9; m++) {
					int s = 0;
					int p = 0;
					switch (m) {
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					case 8:
						s = i;
						p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}

				for (int m = 0; m < 9; m++) {
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}

				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		return bitmap;
	}

	/**
	 * �ữЧ��(��˹ģ��)(�Ż�����������)
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImageAmeliorate(Bitmap bmp) {
		long start = System.currentTimeMillis();
		// ��˹����
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // ֵԽСͼƬ��Խ����Խ����Խ��

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		return bitmap;
	}

	public static File saveMyBitmap(Bitmap mBitmap, String path, String fileName) {
		File dirFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path + "/" + fileName);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.WEBP, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	// 质量压缩方法
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	// 图片按比例大小压缩方法（根据路径获取图片并压缩）
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	// 图片按比例大小压缩方法（根据Bitmap图片压缩）
	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.WEBP, 80, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.WEBP, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	//指定分辨率和清晰度的图片压缩方法
	public static File transImage(String fromFile, String toFile, int quality) {
		try {
//			Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
//			int bitmapWidth = bitmap.getWidth();
//			int bitmapHeight = bitmap.getHeight();
			// 缩放图片的尺寸
//			float scaleWidth = (float) width / bitmapWidth;
//			float scaleHeight = (float) height / bitmapHeight;
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
			// 产生缩放后的Bitmap对象
//			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight);
			Bitmap resizeBitmap =getimage(fromFile);
//			// save file
//			File myCaptureFile = new File(toFile);
//			FileOutputStream out = new FileOutputStream(myCaptureFile);
//			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
//				out.flush();
//				out.close();
//			}
			// save file
			File myCaptureFile = new File(toFile);
			FileOutputStream out = new FileOutputStream(myCaptureFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			int options = 100;
			while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				resizeBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 5;// 每次都减少5
			}
			baos.writeTo(out);
			out.flush();
			out.close();
//			if (!bitmap.isRecycled()) {
//				bitmap.recycle();// 记得释放资源，否则会内存溢出
//			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}
			return myCaptureFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	//指定分辨率和清晰度的图片压缩方法
	public static File transImageFromBitmap(Bitmap fromBitmp, String toFile,int width, int height,int quality) {
		try {
			int bitmapWidth = fromBitmp.getWidth();
			int bitmapHeight = fromBitmp.getHeight();
			// 缩放图片的尺寸
//			float scaleWidth = (float) width / bitmapWidth;
//			float scaleHeight = (float) height / bitmapHeight;
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
			// 产生缩放后的Bitmap对象
//			Bitmap resizeBitmap = Bitmap.createBitmap(fromBitmp, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
			Bitmap resizeBitmap=comp(fromBitmp);
			// save file
//			File myCaptureFile = new File(toFile);
//			FileOutputStream out = new FileOutputStream(myCaptureFile);
//			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
//				out.flush();
//				out.close();
//			}
			File myCaptureFile = new File(toFile);
			FileOutputStream out = new FileOutputStream(myCaptureFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			int options = 100;
			while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				resizeBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 5;// 每次都减少5
			}
			baos.writeTo(out);
			out.flush();
			out.close();
			if (!fromBitmp.isRecycled()) {
				fromBitmp.recycle();// 记得释放资源，否则会内存溢出
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}
			return myCaptureFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
