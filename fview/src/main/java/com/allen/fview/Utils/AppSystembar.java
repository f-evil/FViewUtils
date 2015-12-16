package com.allen.fview.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.allen.fview.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Method;

/**
 * 改变状态栏颜色,API19及以上可用
 *
 * Created by fyj on 2015/9/16.
 */
public class AppSystembar {

	private static View mChildOfContent;

	/**
	 * NoActionBar
	 * @param on
	 * @param activity
	 * @param window
	 * @return
	 */
	@TargetApi(19)
	public static SystemBarTintManager.SystemBarConfig setSystemBar(boolean on,Activity activity,Window window){

		WindowManager.LayoutParams winParams = window.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		window.setAttributes(winParams);

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.title_red);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

		FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);

		mChildOfContent.setPadding(0, PublicUtils.getStatusBarHeight(activity), 0, 0);



//		XLog.e("tintManager.isNavBarTintEnabled()", checkDeviceHasNavigationBar(activity) + ":"+getNavigationBarHeight(activity));


		return config;

	}

	/**
	 * NoActionBar
	 * @param on
	 * @param activity
	 * @param window
	 * @param colorResource
	 * @return
	 */
	@TargetApi(19)
	public static SystemBarTintManager.SystemBarConfig setSystemBar(boolean on,Activity activity,Window window,int colorResource){

		WindowManager.LayoutParams winParams = window.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		window.setAttributes(winParams);

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(colorResource);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

		FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);

		mChildOfContent.setPadding(0, PublicUtils.getStatusBarHeight(activity), 0, 0);



//		XLog.e("tintManager.isNavBarTintEnabled()", checkDeviceHasNavigationBar(activity) + ":"+getNavigationBarHeight(activity));


		return config;

	}



	public static boolean checkDeviceHasNavigationBar(Context context) {
		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasNavigationBar = rs.getBoolean(id);
		}
		try {
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (Exception e) {

		}

		return hasNavigationBar;

	}
	//获取NavigationBar的高度：
	public static int getNavigationBarHeight(Context context) {
		int navigationBarHeight = 0;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if (id > 0 && checkDeviceHasNavigationBar(context)) {
			navigationBarHeight = rs.getDimensionPixelSize(id);
		}
		return navigationBarHeight;
	}


}
