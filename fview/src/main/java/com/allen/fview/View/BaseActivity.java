package com.allen.fview.View;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.allen.fview.Utils.AppSystembar;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * BaseActivity
 * Î´¶¨¸å
 * fyj
 */
public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		getDate();
		initBroadCast();
		bindEvent();
		changeSystembar();
	}

	protected abstract void initView();

	protected abstract void getDate();

	protected abstract void initBroadCast();

	protected abstract void bindEvent();

	private void changeSystembar(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			titleColor();
		}
	}

	protected void titleColor(){
		SystemBarTintManager.SystemBarConfig config = AppSystembar.setSystemBar(true,
				this, getWindow());
	}




}
