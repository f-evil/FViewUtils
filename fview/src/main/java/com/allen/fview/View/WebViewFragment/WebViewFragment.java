package com.allen.fview.View.WebViewFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.allen.fview.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 网页展示组件
 * 加载错误出现自定义404界面
 * 解决有些视频无法加载的问题
 * 解决关闭WebView声音继续播放的问题
 *
 * @author fyj
 */
public class WebViewFragment extends Fragment {

    private static final String TYPE = "type";

    private static String errorPage = "file:///android_asset/404.html";
    private String homePage;

    private WebView webView;

    private ActionCallBack callBack;

    private Map<String, Object> jsinterfaces = new HashMap<String, Object>();

    private boolean type=false;


    public WebViewFragment() {
    }

    public static WebViewFragment newInstance(boolean type) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putBoolean(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getBoolean(TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        webView = (WebView) view.findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.clearFormData();
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadURL(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl(errorPage + "?url=" + failingUrl);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (callBack != null) {
                    callBack.onReceivedTitle(title);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //    return super.onJsAlert(view, url, message, result);
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return true;
            }
        });
        addInterface(webView);
        loadURL(homePage);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (type) {
            webView.stopLoading();
            webView.loadData("", "text/html", "utf-8");
        }

        CookieSyncManager.createInstance(getActivity());
        CookieManager.getInstance().removeAllCookie();

        super.onDestroy();
    }

    /**
     * 添加js方法
     *
     * @param webView 网页视图
     */
    private void addInterface(WebView webView) {
        if (!jsinterfaces.isEmpty()) {
            for (String name : jsinterfaces.keySet()) {
                webView.addJavascriptInterface(jsinterfaces.get(name), name);
            }
        }
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public void setActionCallBack(ActionCallBack callBack) {
        this.callBack = callBack;
    }

    public void addJavascriptInterface(Object object, String name) {
        jsinterfaces.put(name, object);
    }

    public void loadURL(final String url) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });
    }

    public void goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else if (callBack != null) {
            callBack.onClose();
        }
    }

    public interface ActionCallBack {
        void onReceivedTitle(String title);

        void onClose();
    }
}
