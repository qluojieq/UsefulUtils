/**
 * Copyright (C) 2015 Zhensheng Yongyida Robot Co.,Ltd. All rights reserved.
 * 
 * @author: hujianfeng@yongyida.com
 * @version 0.1
 * @date 2015-09-01
 * 
 */
package robot.yongyida.com.omrondemo.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.YuvImage;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class Utils {
	private static final String TAG = "Utils";

	public static String getAppName(Context ctx, int pid) {
		String processName = null;
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
		Iterator<RunningAppProcessInfo> i = l.iterator();
		while (i.hasNext()) {
			RunningAppProcessInfo info = (RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pid) {
					return info.processName;
				}
			}
			catch (Exception e) {
			}
		}
		return processName;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * 
	 */
	public static String getVersionInfo(Context ctx) {
		try {
			PackageManager manager = ctx.getPackageManager();
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			return "VersionName: " + info.versionName + ", VersionCode: " + info.versionCode;
		}
		catch (Exception e) {
			return "";
		}
	}

	public static int getLayoutId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "layout", paramContext.getPackageName());
	}

	public static int getStringId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string", paramContext.getPackageName());
	}

	public static int getDrawableId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "drawable", paramContext.getPackageName());
	}

	public static int getStyleId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style", paramContext.getPackageName());
	}

	public static int getId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id", paramContext.getPackageName());
	}

	public static int getColorId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color", paramContext.getPackageName());
	}

	public static String getMetaData(Context context, String name, String def) {
		String value = getMetaData(context, name);
		return (value == null) ? def : value;
	}

	public static String getMetaData(Context context, String name) {
		PackageManager packageManager = context.getPackageManager();

		try {
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);

			if (applicationInfo != null && applicationInfo.metaData != null) {
				return applicationInfo.metaData.getString(name);
			}
		}
		catch (Exception e) {
			log.e(TAG, "Get metadata exception: " + e);
		}

		return null;
	}

	/**
	 * 文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean fileExists(String path) {
		return new File(path).exists();
	}

	public static boolean createDir(String path) {
		return new File(path).mkdirs();
	}

	public static String getFileName(String path) {
		return new File(path).getName();
	}

	public static long getFileSize(String path) {
		return new File(path).length();
	}

	/**
	 * 检测网络是否可用
	 * 
	 */
	public static boolean isNetWorkConnected(Context ctx) {
		if (ctx != null) {
			ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测SDCARD是否存在
	 * 
	 * @param
	 * @return boolean
	 *
	 */
	public static boolean isExitsSdcard() {
		return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
	}

	/**
	 * 返回外部存储路径
	 * 
	 * @param
	 * @return String
	 *
	 */
	public static String getExternalStorageDirectory() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * 返回时间的字符串格式
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String getTimeString(String pattern, Date date) {
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
	}

	/**
	 * 返回时间毫秒的字符串格式
	 * 
	 * @param pattern
	 * @param dateTime
	 * @return
	 */
	public static String getLongTimeString(String pattern, long dateTime) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		return sDateFormat.format(new Date(dateTime));
	}

	/**
	 * 根据字符串返回日期
	 * 
	 * @param pattern
	 *            日期格式
	 * @param stringDate
	 *            字符串日期
	 * @return
	 */
	public static Date stringToDate(String pattern, String stringDate) {
		DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
		Date date = null;
		try {
			date = format.parse(stringDate);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static Bitmap getBitmap(byte[] yuvNV21, int width, int height) {
		YuvImage yuvImage = new YuvImage(yuvNV21, ImageFormat.NV21, width, height, null);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		yuvImage.compressToJpeg(new android.graphics.Rect(0, 0, width, height), 100, outStream);
		byte[] bytes = outStream.toByteArray();
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	/**
	 * 保存Bitmap图像到文件
	 * 
	 * @param filename
	 * @param bitmap
	 * @return boolean
	 *
	 */
	public static boolean saveBitmap(String filename, Bitmap bitmap, Bitmap.CompressFormat format) {
		log.d(TAG, "save bitmap to:" + filename);
		try {
			FileOutputStream fout = new FileOutputStream(filename);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			bitmap.compress(format, 100, bos);
			bos.flush();
			bos.close();
			return true;
		}
		catch (IOException e) {
			log.e(TAG, "save bitmap error:" + e);
			return false;
		}
	}

	public static byte[] bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(format, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 保存字节数组到文件
	 * 
	 * @param filename
	 * @param bytes
	 * @return boolean
	 *
	 */
	public static boolean saveFile(String filename, byte[] bytes) {
		log.d(TAG, "saveFile: " + filename);

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(filename);
			fOut.write(bytes);
			fOut.close();
			return true;
		}
		catch (FileNotFoundException e) {
			log.e(TAG, "File not found: " + e);
		}
		catch (IOException e) {
			log.e(TAG, "Save file error: " + e);
		}
		return false;
	}
	
	/**
	 * 读取文件到缓冲区
	 * 
	 * @param filename
	 * @return byte[]
	 *
	 */
	public static byte[] loadFile(String filename) {
		log.d(TAG, "loadFile: " + filename);
		
		File file = new File(filename);
		if (!file.exists()) {
			log.e(TAG, "File not exists");
			return null;
		}
		if (file.length() <= 0) {
			log.e(TAG, "File size:%d", file.length());
			return null;
		}
		
		byte[] buffer = new byte[(int)file.length()];
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			fin.read(buffer);
		}
		catch (FileNotFoundException e) {
			log.e(TAG, "File not found: " + e);
			buffer = null;
		}
		catch (IOException e) {
			log.e(TAG, "Save file error: " + e);
			buffer = null;
		}
		finally {
			try {
				fin.close();
			}
			catch (IOException e) {
			}
		}
		
		return buffer;
	}
	
	/**
	 * 返回屏幕横竖屏信息
	 * 
	 * @param ctx
	 * @return int
	 * 
	 */
	public static int getScreenConfigurationOrientatioin(Context ctx) {
		return ctx.getResources().getConfiguration().orientation;
	}

	/**
	 * 设置屏幕方向，设置为横屏或竖屏后，屏幕就不会自动旋转。
	 * setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ==
	 * android:screenOrientation="landscape"
	 * 
	 * @param ctx
	 * @return int
	 * 
	 */
	public static void setScreenOrientatioin(Activity activity, int screenOrientatioin) {
		activity.setRequestedOrientation(screenOrientatioin);
	}

	/**
	 * 返回屏幕横竖屏的设置值
	 * 
	 * @param ctx
	 * @return int
	 * 
	 */
	public static int getScreenOrientatioin(Activity activity) {
		int orientation = activity.getRequestedOrientation();

		log.d(TAG, "Requested.Orientation = " + orientation);
		if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			log.d(TAG, "ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, orientation=" + orientation);
		else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
			log.d(TAG, "ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, orientation=" + orientation);
		else if (orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
			log.d(TAG, "ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, orientation=" + orientation);
		else
			log.d(TAG, "ActivityInfo, orientation=" + orientation);

		return orientation;
	}

	/**
	 * 返回屏幕宽
	 * 
	 * @param ctx
	 * @return int
	 *
	 */
	public static int getScreenWidth(Context ctx) {
		return ctx.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 返回屏幕高
	 * 
	 * @param ctx
	 * @return int
	 * 
	 */
	public static int getScreenHeight(Context ctx) {
		return ctx.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 返回屏幕大小
	 * 
	 * @param ctx
	 * @return
	 * 
	 */
	public static Point getDisplayMetrics(Context ctx) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		return new Point(dm.widthPixels, dm.heightPixels);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
	 */
	public static int sp2px(Context context, float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (sp * scale);
	}

	/**
	 * 返回屏幕比例
	 * 
	 * @param ctx
	 * @return
	 * 
	 */
	public static float getScreenRate(Context ctx) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		return (dm.widthPixels / dm.widthPixels);
	}

	/**
	 * 返回字符串资源
	 * 
	 */
	static String getStringResource(Context ctx, int resId) {
		return ctx.getResources().getString(resId);
	}

	/**
	 * 返回顶层Activity
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static String getTopActivity(Context ctx) {
		ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * 返回DeviceId
	 * 
	 */
	public static String getDeviceId(Context ctx) {
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * 返回SubscriberId
	 * 
	 */
	public static String getSubscriberId(Context ctx) {
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSubscriberId();
	}

	/**
	 * 返回SimSerialNumber
	 * 
	 */
	public static String getSimSerialNumber(Context ctx) {
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSimSerialNumber();
	}

	/**
	 * 返回UID
	 * 
	 */
	public static String getUID(Context ctx) {
		return getSimSerialNumber(ctx);
	}

	/**
	 * 返回PhoneNumber
	 * 
	 */
	public static String getPhoneNumber(Context ctx) {
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}

	/**
	 * 返回GUID
	 * 
	 */
	public static String getGUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "").toUpperCase(Locale.getDefault());
	}

	/**
	 * 返回短整型的十六进制字符串
	 * 
	 */
	public static String getHexString(short value) {
		return String.format("0x%04X", value);
	}

	/**
	 * 返回整型的十六进制字符串
	 * 
	 */
	public static String getHexString(int value) {
		return String.format("0x%08X", value);
	}

	/**
	 * 返回长整型的十六进制字符串
	 * 
	 */
	public static String getHexString(long value) {
		return String.format("0x%16X", value);
	}

	/**
	 * 返回字节数组的十六进制字符串
	 * 
	 */
	public static String getHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase(Locale.getDefault()));
		}
		return sb.toString();
	}

	/**
	 * 返回字节数组的十六进制字符串
	 * 
	 */
	public static String getHexString(byte[] bytes, int offset, int length) {
		StringBuffer sb = new StringBuffer();
		int len = offset + length;
		for (int i = offset; i < len; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase(Locale.getDefault()));
		}
		return sb.toString();
	}

	/**
	 * 图像旋转
	 * 
	 * @param bitmap
	 * @param degrees
	 * @return
	 * 
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
		Matrix matrix = new Matrix();
		matrix.postRotate((float) degrees);
		Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
		return rotaBitmap;
	}

	/**
	 * 返回本机IP
	 * 
	 * @param ctx
	 * @return String
	 *
	 */
	public static String getIp(Context ctx) {
		WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return getIpString(ipAddress);
	}

	/**
	 * 整型IP转换成字符串型IP
	 * 
	 * @param i
	 * @return
	 *
	 */
	public static String getIpString(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	/**
	 * 判断IP地址是否合法
	 * 
	 * @param ip
	 *            IP地址
	 * @return IP合法返回true，不合法返回false
	 */
	public static boolean isCorrectIp(String ip) {
		Pattern ipPattern = Pattern
				.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
		Matcher ipMatcher = ipPattern.matcher(ip);
		return ipMatcher.find();
	}

	/**
	 * 在Activity的非UI线程中显示提示
	 * 
	 * @param activity
	 * @param msg
	 * @return
	 */
	public static void toast(final Activity activity, final String msg) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 在非UI线程中显示提示
	 * 
	 * @param ctx
	 * @param msg
	 * @return
	 */
	public static void toast(final Context ctx, final String msg) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 在非UI线程中显示提示
	 * 
	 * @param ctx
	 * @param msg
	 * @return
	 */
	public static void showCustomToast(Activity ctx, String msg) {
		int layoutId = getLayoutId(ctx, "custom_toast");
		View layout = LayoutInflater.from(ctx).inflate(layoutId,
				(ViewGroup) ctx.findViewById(getId(ctx, "layout_toast")));
		TextView tv = (TextView) layout.findViewById(getId(ctx, "tv_message"));
		tv.setText(msg);
		Toast toast = new Toast(ctx);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

	/**
	 * 在非UI线程中显示提示
	 * 
	 * @param ctx
	 * @param msg
	 * @return
	 */
	public static void showCustomToast(Activity ctx, String msg, int textColor, int textSize, int x, int y) {
		View layout = LayoutInflater.from(ctx).inflate(getLayoutId(ctx, "custom_toast"),
				(ViewGroup) ctx.findViewById(getId(ctx, "layout_toast")));
		TextView tv = (TextView) layout.findViewById(getId(ctx, "tv_message"));
		tv.setText(msg);
		tv.setTextColor(textColor);
		tv.setTextSize(textSize);
		Toast toast = new Toast(ctx);
		toast.setGravity(Gravity.START | Gravity.TOP, x, y);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	/**
	 * 屏幕唤醒
	 * 
	 * @param ctx
	 * @return
	 *
	 */
	@SuppressWarnings("deprecation")
	public static void ScreenOff(Context ctx) {
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		if (pm.isScreenOn()) {
			PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
					"LOCK_TAG");
			wl.release();
		}
	}

	/**
	 * 屏幕唤醒
	 * 
	 * @param ctx
	 * @return
	 *
	 */
	@SuppressWarnings("deprecation")
	public static void wakeUp(Context ctx) {
		PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		if (!pm.isScreenOn()) {
			PowerManager.WakeLock wl = pm
					.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
			wl.acquire();
			wl.release();
		}
	}

	/**
	 * 解锁
	 * 
	 * @param ctx
	 * @return
	 *
	 */
	@SuppressWarnings("deprecation")
	public static void wakeUnlock(Context ctx) {
		KeyguardManager km = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
		kl.disableKeyguard();
	}

	public boolean isSupportMediaCodec() {
		boolean isHardcode = false;

		// 读取系统配置文件/system/etc/media_codecc.xml
		File file = new File("/system/etc/media_codecs.xml");
		InputStream inFile = null;
		try {
			inFile = new FileInputStream(file);
		}
		catch (Exception e) {
		}

		if (inFile != null) {
			XmlPullParserFactory pullFactory;
			try {
				pullFactory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = pullFactory.newPullParser();
				xmlPullParser.setInput(inFile, "UTF-8");

				int eventType = xmlPullParser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String tagName = xmlPullParser.getName();

					switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("MediaCodec".equals(tagName)) {
							String componentName = xmlPullParser.getAttributeValue(0);

							if (componentName.startsWith("OMX.")) {
								if (!componentName.startsWith("OMX.google.")) {
									isHardcode = true;
								}
							}
						}
					}
					eventType = xmlPullParser.next();
				}
			}
			catch (Exception e) {
			}
		}
		return isHardcode;
	}

	public static String getString(String[] strs) {
		StringBuilder temp = new StringBuilder();
		for (String str : strs) {
			temp.append(str).append(";");
		}
		return temp.toString();
	}

	public static String getPropertyValue(String str) {
		if ((str != null) && (str.length() > 0)) {
			int equ = str.lastIndexOf('=');
			if ((equ > -1) && (equ < (str.length() - 1))) {
				return str.substring(equ + 1);
			}
		}
		return str;
	}

	public static boolean StringsContain(String[] strs, String value) {
		if (strs == null || strs.length == 0) {
			return false;
		}
		for (String str : strs) {
			if (str.equals(value))
				return true;
		}
		return false;
	}

	/**
	 * 获取当前可用内存大小
	 * 
	 * @return
	 */
	public static String getFreeMemory(Context ctx) {
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);

		return Formatter.formatFileSize(ctx, mi.availMem);
	}

	public static Path getPath(float[] points) {
		Path path = new Path();
		int count = points.length / 2;
		path.moveTo(points[0], points[1]);

		for (int i = 0; i < count - 1; ++i) {
			path.lineTo(points[2 + i * 2], points[2 + i * 2 + 1]);
		}
		path.close();
		return path;
	}
}
