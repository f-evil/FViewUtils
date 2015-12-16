package com.allen.fview.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.allen.fview.R;
import com.allen.fview.Utils.PublicUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;


/**
 * 富文本TextView
 * 增加图片点击接口与超链接点击接口
 */
public class RichText extends TextView {

    private ImageLoader mLoader;
    private DisplayImageOptions options;

    private int screenWidth;
    private int viewWidth;

    private int widthExp;

    private Drawable placeHolder, errorImage;//占位图、出错图
    private OnImageClickListener onImageClickListener;//图片点击回调
    private OnUrlClickListener onUrlClickListener;//URL点击回调

    public RichText(Context context) {
        this(context, null);
    }

    public RichText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (placeHolder == null) {
            placeHolder = new ColorDrawable(Color.GRAY);
        }
        if (errorImage == null) {
            errorImage = new ColorDrawable(Color.GRAY);
        }

        mLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.icon_pic_rec)
                .showImageOnFail(R.drawable.icon_pic_rec)
                .resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        screenWidth = PublicUtils.getWidth(context);
        viewWidth = screenWidth - PublicUtils.dp2px(context, 15);

        screenWidth = screenWidth - PublicUtils.dp2px(context, 25);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY) {
            widthExp = widthSize;
        } else {
            widthExp = widthSize;
        }
    }

    /**
     * 设置要显示的富文本内容
     *
     * @param text 内容
     */
    public void setRichText(String text) {
        Spanned spanned = Html.fromHtml(text, asyncImageGetter, null);
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }

        URLSpan[] urls = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(spannableStringBuilder, span);
        }

        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final List<String> imageUrls = new ArrayList<>();

        URLSpan[] url_html = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpan.class);
        for (final URLSpan span : url_html) {
            setLinkClickable(spannableStringBuilder, span);
        }

        for (int i = 0, size = imageSpans.length; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);

            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onImageClickListener != null) {
                        onImageClickListener.imageClicked(imageUrls, finalI);
                    }
                }
            };
            ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(start, end, ClickableSpan.class);
            if (clickableSpans != null && clickableSpans.length != 0) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        super.setText(spanned);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
                                  final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {
                onUrlClickListener.urlClicked(urlSpan.getURL());
            }
        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    private Html.ImageGetter asyncImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable(getContext());

            if (source.contains("storage/emulated") || source.toLowerCase().contains("/sdcard/")) {
                source = "file://" + source;
            } else if (source.startsWith("http://")) {
                source = source;
            } else {
                source = "自定义http" + source;
            }

            mLoader.loadImage(source, options, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    super.onLoadingStarted(imageUri, view);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);

                    int width = 100;
                    int height = 100;

                    Drawable drawable = new BitmapDrawable(getContext().getResources(), loadedImage);

                    float imgH = (float) loadedImage.getHeight();
                    float imgW = (float) loadedImage.getWidth();
                    if (imgW > screenWidth) {
                        width = screenWidth;
                        height = (int) (width * (imgH / imgW));
                    } else {
                        width = (int) imgW;
                        height = (int) imgH;
                    }

                    drawable.setBounds(0, 0, width, height);
                    urlDrawable.setBounds(0, 0, screenWidth, height + 5);
                    urlDrawable.setDrawable(drawable);
                    RichText.this.setText(getText());
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    super.onLoadingCancelled(imageUri, view);
                }
            });
//            }


            return urlDrawable;
        }
    };

    private static Bitmap small(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public static class URLDrawable extends BitmapDrawable {
        private Drawable drawable;

        public URLDrawable(Context context) {
//			drawable = context.getResources().getDrawable(R.drawable.icon_pic_rec);
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null)
                drawable.draw(canvas);
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    public void setPlaceHolder(Drawable placeHolder) {
        this.placeHolder = placeHolder;
    }

    public void setErrorImage(Drawable errorImage) {
        this.errorImage = errorImage;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setOnUrlClickListenerListener(OnUrlClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
    }

    public interface OnImageClickListener {
        void imageClicked(List<String> imageUrls, int position);
    }

    public interface OnUrlClickListener {
        void urlClicked(String url);
    }

}
