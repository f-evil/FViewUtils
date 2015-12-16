package com.allen.fview.Utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Toast显示工具类
 * 防止Toast多次显示的问题
 *
 * Created by fyj on 2015/9/19.
 */
public class ToastUtil {

	private static Toast mToast=null;


	public static void makeText(Context context,CharSequence msg){

		if (mToast ==null){
			mToast= Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}

		mToast.show();
	}

	public static void makeText(final Activity activity, final String message) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mToast ==null){
					mToast= Toast.makeText(activity, message, Toast.LENGTH_SHORT);
				}else {
					mToast.setText(message);
					if(message.length() < 20)
						mToast.setDuration(Toast.LENGTH_SHORT);
					else
						mToast.setDuration(Toast.LENGTH_LONG);
				}
				mToast.show();
			}
		});
	}

}
