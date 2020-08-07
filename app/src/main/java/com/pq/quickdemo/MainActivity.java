package com.pq.quickdemo;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.pq.pqwebview.PqWebview;
import com.pq.quicksdk.QuickActivity;
import com.pq.quicksdk.QuickNotifier;
import com.pq.quicksdk.QuickUtil;
import com.quicksdk.entity.UserInfo;

public class MainActivity extends QuickActivity {
    private boolean isInit=false;
    private PqWebview wv=null;
    private boolean isLogin=false;
    private boolean openLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createQuick(MainActivity.this,iquickListener);
    }
    private void init(){
        createWebview();
//        FullScreenUtil.autoHideNavigation(MainActivity.this);
    }
    public void changeStatus(int statu){
        wv.callJs("javascript:appPlugin.pause('"+statu+"')");
    }
    @JavascriptInterface
    public void signIn() {
        if(!isInit)return;
        openLogin=true;
        QuickUtil.signIn();
    }
    @JavascriptInterface
    public void signOut(){
        QuickUtil.signOut();
    }
    @JavascriptInterface
    public void report(String jsonStr,boolean isCreate){
        showLog("上报："+jsonStr);
        QuickUtil.reportUserinfo(jsonStr,isCreate);
    }
    @JavascriptInterface
    public void toPay(String jStr){
        showLog("支付：" + jStr);
        QuickUtil.toPay(jStr);
    }
    @JavascriptInterface
    public void showLog(String msg){
        Log.d("log>>> ",msg);
    }
    @JavascriptInterface
    public void showTip(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        showLog(msg);
    }
    @Override
    public void onResume() {
        if(getRequestedOrientation()== ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        super.onResume();
    }
    private QuickNotifier iquickListener=new QuickNotifier() {
        @Override
        public void inited() {
            isInit=true;
            init();
        }

        @Override
        public void loginFailed(String message, String trace) {
            showTip("登录失败("+trace+")");
        }

        @Override
        public void paySuccess(String sdkOrderID, String cpOrderID, String extrasParams) {
            showTip("支付成功:"+sdkOrderID+"--"+cpOrderID+"--"+extrasParams);
            wv.callJs("pay_ok(\""+cpOrderID+"\",\""+sdkOrderID+"\")");
        }

        @Override
        public void payFailed(String cpOrderID, String message, String trace) {
            showTip("支付失败："+message+"--"+trace);
        }

        @Override
        public void reportFailed(String msg) {
            showLog("上报结果："+msg);
        }

        @Override
        public void loginSuccess(UserInfo info) {
            isLogin=true;
            showTip("登录成功");
            wv.callJs("login_game(\""+info.getUID()+"\",\""+info.getToken()+"\")");
        }
    };
    private void createWebview() {
        wv = new PqWebview(MainActivity.this);
        wv.regFunc(MainActivity.this,"pq");
        wv.loadUrl("http://baidu.com");
    }
}
