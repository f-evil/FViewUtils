package com.allen.fview.View.WebViewFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.fview.R;
import com.allen.fview.Utils.AppSystembar;
import com.allen.fview.Utils.ShrankViewUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MsgWebActivity extends FragmentActivity {

	private WebViewFragment webViewFragment;

	private ImageButton btnBack;
	private TextView txtChatName2;
	private LinearLayout webcontent;
	private LinearLayout ll_msgweb;

	private boolean viewShow = false;

	private boolean type;


	private static final int RADIO = 0;
	private static final int PHOTO = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_msg_web);

		InitComponent();
		getData();
		changeSystembar();
	}

	private void getData() {
		String url = getIntent().getStringExtra("Url").replace("？", "?");
		webViewFragment.setHomePage(url);
	}

	private void InitComponent() {

		btnBack = (ImageButton) findViewById(R.id.btnBack1);
		txtChatName2 = (TextView) findViewById(R.id.txtTitle1);
		txtChatName2.setText(getIntent().getStringExtra("Title"));
		ll_msgweb = (LinearLayout) findViewById(R.id.ll_msgweb);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		webViewFragment = WebViewFragment.newInstance(type);
		webViewFragment.addJavascriptInterface(new MsgWebActivity.JavaScriptInterface(), "同后台确定");
		transaction.replace(R.id.webview_msg, webViewFragment);
		transaction.commit();

		btnBack.setOnClickListener(new ClickEvent());

	}

	private void changeSystembar() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager.SystemBarConfig config = AppSystembar.setSystemBar(true, MsgWebActivity.this,
					getWindow());
			ShrankViewUtil.assistActivity(this);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public class JavaScriptInterface {
		@JavascriptInterface
		public void CloseWebView() {
			finish();
		}


	}

	private class ClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				case R.id.btnBack1:
					finish();
					break;
				default:
					break;
			}
		}
	}
}
