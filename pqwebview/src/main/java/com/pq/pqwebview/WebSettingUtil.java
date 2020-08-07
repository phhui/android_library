package com.pq.pqwebview;
import android.webkit.WebSettings;
public class WebSettingUtil {
    public static void setEgret(WebSettings ws){
        ws.setJavaScriptEnabled(true);  //支持js
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);// 设置允许JS弹窗
        ws.setUseWideViewPort(false);  //将图片调整到适合webview的大小
//        ws.setSupportZoom(true);  //支持缩放
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//        ws.supportMultipleWindows();  //多窗口
        ws.setDomStorageEnabled(true);//开启本地缓存功能
//        ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        ws.setAllowFileAccess(true);  //设置可以访问文件
        ws.setAllowContentAccess(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
//        ws.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//        ws.setBuiltInZoomControls(true); //设置支持缩放
        ws.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
//        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);//设置提高渲染的优先级
        ws.setMediaPlaybackRequiresUserGesture(false); //（2020/05/07-23:30分新增）设置WebView是否通过手势触发播放媒体，默认是true，需要手势触发。
//        //##特别说明：如果是跳转百度网页，因baidu.com对这个属性进行了拦截，这里在有的情况，不能设置为true，使用默认就好
//        ws.setSupportMultipleWindows(true);//（2020/05/07-23:30分新增)设置WebView是否支持多屏窗口，参考WebChromeClient#onCreateWindow，默认false，不支持。
//        //下面再新增进行编号处理：
//        ws.setStandardFontFamily("");
//        //设置WebView标准字体库字体，默认字体“sans-serif”。
//        ws.setStandardFontFamily("sans-serif");
//        ws.setFixedFontFamily("");
//        //设置WebView固定的字体库字体，默认“monospace”。
//        ws.setFixedFontFamily("monospace");
//        ws.setSansSerifFontFamily("");
//        //设置WebView Sans SeriFontFamily字体库字体，默认“sans-serif”。
//        ws.setSansSerifFontFamily("sans-serif");
//        ws.setSerifFontFamily("");
//        //设置WebView seri FontFamily字体库字体，默认“sans-serif”。
//        ws.setSansSerifFontFamily("sans-serif");
//        ws.setCursiveFontFamily("");
//        //设置WebView字体库字体，默认“cursive”
//        ws.setCursiveFontFamily("cursive");
//        ws.setFantasyFontFamily("");
//        //设置WebView字体库字体，默认“fantasy”。
//        ws.setFantasyFontFamily("fantasy");
//        ws.setMinimumFontSize(12);
//        //设置WebView字体最小值，默认值8，取值1到72
//        ws.setMinimumFontSize(8);
//        ws.setMinimumLogicalFontSize(12);
//        //设置WebView逻辑上最小字体值，默认值8，取值1到72
//        ws.setMinimumLogicalFontSize(8);
//        ws.setDefaultFontSize(12);
//        //设置WebView默认值字体值，默认值16，取值1到72
//        ws.setDefaultFontSize(16);
//        ws.setDefaultFixedFontSize(12);
//        //设置WebView默认固定的字体值，默认值16，取值1到72
//        ws.setDefaultFixedFontSize(16);
//        ws.setGeolocationEnabled(true);//设置是否开启定位功能，默认true，开启定位
//        ws.setGeolocationEnabled(false);
//        ws.setJavaScriptCanOpenWindowsAutomatically(false);//设置脚本是否允许自动打开弹窗，默认false，不允许
        ws.setAllowContentAccess (true);//是否允许在WebView中访问内容URL（Content Url），默认允许。内容Url访问允许WebView从安装在系统中的内容提供者载入内容。
    }
}
