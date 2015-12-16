package com.allen.fview.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allen.fview.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 九宫格视图,测试版
 * fyj
 */
public class NinePhotoView extends LinearLayout {

	private Context context;

	private List<String> urls;
	private ImageLoader mLoader;
	private DisplayImageOptions options;

	private ImageView pic_0;
	private ImageView pic_1;
	private ImageView pic_2;
	private ImageView pic_3;
	private ImageView pic_4;
	private ImageView pic_5;
	private ImageView pic_6;
	private ImageView pic_7;
	private ImageView pic_8;
	private ImageView pic_9;

	private LinearLayout ll_pics_top;
	private LinearLayout ll_pics_middle;
	private LinearLayout ll_pics_buttom;

	public NinePhotoView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public NinePhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public NinePhotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

	}

	private void init() {
		View.inflate(context, R.layout.custom_ninephotoview, this);
		onCreate();
	}

	public void onCreate() {

		initView();

	}

	public void display() {
		int imageCount = urls.size();
		ImageView[] ivArray = new ImageView[]{pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7, pic_8,
				pic_9};
		LinearLayout[] ll_iv_pic = new LinearLayout[]{ll_pics_top, ll_pics_middle,
				ll_pics_buttom};

		visibleOrGoneLLPic(ll_iv_pic, false, false, false);
		goneAllImageView(ivArray);

		switch (imageCount) {
			case 9:
				ivArray = new ImageView[]{pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7, pic_8,
						pic_9};
				visibleOrGoneLLPic(ll_iv_pic, true, true, true);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 8:
				ivArray = new ImageView[]{pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7, pic_8};
				visibleOrGoneLLPic(ll_iv_pic, true, true, true);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 7:
				ivArray = new ImageView[]{pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7};
				visibleOrGoneLLPic(ll_iv_pic, true, true, true);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 6:
				ivArray = new ImageView[]{pic_1, pic_2, pic_3, pic_4, pic_5, pic_6};
				visibleOrGoneLLPic(ll_iv_pic, true, true, false);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 5:
				ivArray = new ImageView[]{pic_1, pic_2, pic_3, pic_4, pic_5};
				visibleOrGoneLLPic(ll_iv_pic, true, true, false);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 4:
				ivArray = new ImageView[]{pic_1, pic_2, pic_4, pic_5};
				visibleOrGoneLLPic(ll_iv_pic, true, true, false);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 3:
				ivArray = new ImageView[]{pic_1, pic_2, pic_3};
				visibleOrGoneLLPic(ll_iv_pic, true, false, false);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 2:
				ivArray = new ImageView[]{pic_1, pic_2};
				visibleOrGoneLLPic(ll_iv_pic, true, false, false);
				pic_0.setVisibility(View.GONE);
				visibleSelectImageView(ivArray);
				dataBindImageView(ivArray, urls);
				break;

			case 1:
				ivArray = new ImageView[]{pic_0};
				visibleOrGoneLLPic(ll_iv_pic, false, false, false);
				pic_0.setVisibility(View.VISIBLE);
				ll_pics_top.setVisibility(View.GONE);
				ll_pics_middle.setVisibility(View.GONE);
				ll_pics_buttom.setVisibility(View.GONE);
				dataBindImageView(ivArray, urls);
				break;

			case 0:
				ivArray = new ImageView[]{};
				visibleOrGoneLLPic(ll_iv_pic, false, false, false);
				ll_pics_top.setVisibility(View.GONE);
				ll_pics_middle.setVisibility(View.GONE);
				ll_pics_buttom.setVisibility(View.GONE);
				pic_0.setVisibility(View.GONE);
				break;

		}
	}

	private void dataBindImageView(ImageView[] iv_array, final List<String> urls) {

			for (int i = 0; i < iv_array.length; i++) {
				final int j = i;

				mLoader.displayImage("自定义http" + urls.get(i), iv_array[i],
						options);

			}

	}

	private void goneAllImageView(ImageView[] iv_array) {
		for (int i = 0; i < iv_array.length; i++) {
			iv_array[i].setVisibility(View.GONE);
		}
	}

	private void visibleSelectImageView(ImageView[] iv_array) {
		for (int i = 0; i < iv_array.length; i++) {
			iv_array[i].setVisibility(View.VISIBLE);
		}
	}

	private void visibleOrGoneLLPic(LinearLayout[] ll_array, boolean top, boolean middle, boolean buttom) {
		if (top) {
			ll_array[0].setVisibility(View.VISIBLE);
		} else {
			ll_array[0].setVisibility(View.GONE);
		}
		if (middle) {
			ll_array[1].setVisibility(View.VISIBLE);
		} else {
			ll_array[1].setVisibility(View.GONE);
		}
		if (buttom) {
			ll_array[2].setVisibility(View.VISIBLE);
		} else {
			ll_array[2].setVisibility(View.GONE);
		}
	}

	private void initView() {

		urls = new ArrayList<String>();

		pic_0 = (ImageView) findViewById(R.id.iv_pic00);
		pic_1 = (ImageView) findViewById(R.id.iv_pic11);
		pic_2 = (ImageView) findViewById(R.id.iv_pic22);
		pic_3 = (ImageView) findViewById(R.id.iv_pic33);
		pic_4 = (ImageView) findViewById(R.id.iv_pic44);
		pic_5 = (ImageView) findViewById(R.id.iv_pic55);
		pic_6 = (ImageView) findViewById(R.id.iv_pic66);
		pic_7 = (ImageView) findViewById(R.id.iv_pic77);
		pic_8 = (ImageView) findViewById(R.id.iv_pic88);
		pic_9 = (ImageView) findViewById(R.id.iv_pic99);

		ll_pics_top = (LinearLayout) findViewById(R.id.ll_pics_topp);
		ll_pics_middle = (LinearLayout) findViewById(R.id.ll_pics_middlee);
		ll_pics_buttom = (LinearLayout) findViewById(R.id.ll_pics_buttomm);
	}

	public void getUrl() {

	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public void setmLoader(ImageLoader mLoader, DisplayImageOptions options) {
		this.mLoader = mLoader;
		this.options = options;
	}


}
