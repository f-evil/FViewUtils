package com.allen.fview.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicUtils {

	public static float density;    // 屏幕密度（0.75 / 1.0 / 1.5）
	public static int width;      // 屏幕宽度（像素）
	public static int height;            // 屏幕高度（像素）
	public static int densityDpi;       // 屏幕密度DPI（120 / 160 / 240）

	public static int getWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public static int getHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	public static int dp2px(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2dp(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue
	 * @param pxValue （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param spValue （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	// 图片转Base64

	public static String Bitmap2StrByBase64(Bitmap bit) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bit.compress(CompressFormat.JPEG, 100, bos);// 参数100表示不压缩
		byte[] bytes = bos.toByteArray();
		double i = bytes.length / 1024.0;
		System.out.println(i / 1024.0 + "M");
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	// 语音文件
	@SuppressWarnings("resource")
	public static String SoundResStrBase64(File file) {
		FileInputStream in;
		String data64 = "";
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length() + 100];
			int length = in.read(buffer);
			data64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data64;

	}

	public static String refreshUpdatedAtValue(String time) {
		/**
		 * 一分钟的毫秒值，用于判断上次的更新时间
		 */
		final long ONE_MINUTE = 60 * 1000;

		/**
		 * 一小时的毫秒值，用于判断上次的更新时间
		 */
		final long ONE_HOUR = 60 * ONE_MINUTE;

		/**
		 * 一天的毫秒值，用于判断上次的更新时间
		 */
		final long ONE_DAY = 24 * ONE_HOUR;

		/**
		 * 一月的毫秒值，用于判断上次的更新时间
		 */
		final long ONE_MONTH = 30 * ONE_DAY;

		/**
		 * 一年的毫秒值，用于判断上次的更新时间
		 */
		final long ONE_YEAR = 12 * ONE_MONTH;

//		long lastUpdateTime;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date dt = null;//转换成功的Date对象
//		try {
//			dt = sdf.parse(time);
//			lastUpdateTime = dt.getTime();
//		} catch (ParseException e) {
//			e.printStackTrace();
//			lastUpdateTime=System.currentTimeMillis()-3828000;
//		}
//		long currentTime = System.currentTimeMillis();
//		long timePassed = currentTime - lastUpdateTime;
//		long timeIntoFormat;
//		String updateAtValue;

		long lastUpdateTime = Long.valueOf(time);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;

		if (timePassed < 0) {
			updateAtValue = "时间有问题";
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = "1小时内";
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "分钟";
			updateAtValue = value;
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "小时内";
			updateAtValue = value;
		}
//		else if (timePassed < ONE_MONTH) {
//			timeIntoFormat = timePassed / ONE_DAY;
//			String value = timeIntoFormat + "天";
//			updateAtValue = "1天前";
//		} else if (timePassed < ONE_YEAR) {
//			timeIntoFormat = timePassed / ONE_MONTH;
//			String value = timeIntoFormat + "个月";
//			updateAtValue = "1天前";
//		}
		else {
			try {

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(lastUpdateTime);
				Formatter ft = new Formatter(Locale.CHINA);
				updateAtValue = ft.format("%1$tY年%1$tm月%1$td日", cal).toString();
			} catch (Exception e) {
				updateAtValue = "时间解析错误";
			}

		}
		return updateAtValue;
	}

	public static String longToFormatTime(String time){
		if (time.length()<10){
			time=time+"000";
		}
		long lastUpdateTime = Long.valueOf(time);
		String updateAtValue;
//		try {
//
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(lastUpdateTime);
//			Formatter ft = new Formatter(Locale.CHINA);
//			updateAtValue = ft.format("%1$tY年%1$tm月%1$td日 %1$tH:%1$tm", cal).toString();
//		} catch (Exception e) {
//			updateAtValue = "时间解析错误";
//		}

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(lastUpdateTime);
		updateAtValue=sdf.format(date);
		return updateAtValue;
	}

	// 图片压缩
	public static String picCompress(byte[] pic) {
		double i = pic.length / 1024.0;
		i = i / 1024.0;
		if (i < 0.5) {
			return Base64.encodeToString(pic, Base64.DEFAULT);
		} else if (i < 1) {
			return getPicFromBytes(pic, 2);
		} else if (i < 2) {
			return getPicFromBytes(pic, 4);
		} else if (i < 4) {
			return getPicFromBytes(pic, 8);
		} else {
			return getPicFromBytes(pic, 16);
		}

	}

	public static String getPicFromBytes(byte[] bytes, int sampleSize) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opts.inSampleSize = sampleSize;
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
				opts);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);// 参数100表示不压缩
		byte[] bytes1 = bos.toByteArray();
		return Base64.encodeToString(bytes1, Base64.DEFAULT);

		// return Bitmap2StrByBase64(bitmap);
	}

	/**
	 * 验证是否是手机号码
	 *
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("[1][358]\\d{9}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * MD5加密
	 *
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toUpperCase();
	}

	/**
	 * 获取状态栏高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}
