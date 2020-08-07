package com.pq.pqwebview;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class PqWebview extends WebView {
    private IPqWebviewListener listener=null;
    private FrameLayout view;
    private Activity ct;
    public PqWebview(Activity context) {
        super(context);
        ct=context;
        init();
    }
    public PqWebview(Activity context,IPqWebviewListener ipwl) {
        super(context);
        ct=context;
        listener=ipwl;
        init();
    }
    protected void init(){
        this.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                if(listener!=null)listener.onPageFinished(url);//这里执行完毕才能调用webview中的JS
            }
            /**
             doUpdateVisitedHistory(WebView view, String url, boolean isReload)  //(更新历史记录)
             onFormResubmission(WebView view, Message dontResend, Message resend) //(应用程序重新请求网页数据)
             onLoadResource(WebView view, String url) // 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
             onPageStarted(WebView view, String url, Bitmap favicon) //这个事件就是开始载入页面调用的，通常我们可以在这设定一个loading的页面，告诉用户程序在等待网络响应。
             onPageFinished(WebView view, String url) //在页面加载结束时调用。同样道理，我们知道一个页面载入完成，于是我们可以关闭loading 条，切换程序动作。
             onReceivedError(WebView view, int errorCode, String description, String failingUrl)// (报告错误信息)
             onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host,String realm)//（获取返回信息授权请求）
             onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) //重写此方法可以让webview处理https请求。
             onScaleChanged(WebView view, float oldScale, float newScale) // (WebView发生改变时调用)
             onUnhandledKeyEvent(WebView view, KeyEvent event) //（Key事件未被加载时调用）
             shouldOverrideKeyEvent(WebView view, KeyEvent event)//重写此方法才能够处理在浏览器中的按键事件。
             shouldOverrideUrlLoading(WebView view, String url)
             //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
             */
        });
        this.setWebChromeClient(new WebChromeClient(){
            //设置响应js 的Alert()函数
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(ct);
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
        WebSettingUtil.setEgret(this.getSettings());
        this.requestFocusFromTouch();
        view=(FrameLayout)ct.getWindow().getDecorView();
        view.addView(PqWebview.this);
//        this.addJavascriptInterface(new Object(){
//            @JavascriptInterface
//            public void signIn(String msg) {
//                /**js通过myapp.signIn("aa");可调用此方法*/
//                Log.d("log>>>",msg);
//            }
//        }, "myapp");
    }
    /**注册的方法必需加上@JavascriptInterface才可以被JS调用到*/
    @SuppressLint("JavascriptInterface")
    public void regFunc(Object obj, String name){
        this.addJavascriptInterface(obj,name);
    }
    public void callJs(String js){
        PqWebview.this.evaluateJavascript("javascript:"+js, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if(listener!=null)listener.onReceiveJsValue(value);//此处为 js 返回的结果
            }
        });
    }
}
