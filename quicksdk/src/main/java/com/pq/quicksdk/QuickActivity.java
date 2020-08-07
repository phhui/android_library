package com.pq.quicksdk;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
public class QuickActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**此方法必需在onCreate中调用*/
    protected void createQuick(Activity act, QuickNotifier iql){
        QuickUtil.create(act,iql);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            QuickUtil.init("授权成功，调用初始化！");
        }else QuickUtil.requestPermissions();
    }
    @Override
    protected void onStart() {
        super.onStart();
        com.quicksdk.Sdk.getInstance().onStart(QuickActivity.this);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        com.quicksdk.Sdk.getInstance().onRestart(QuickActivity.this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        com.quicksdk.Sdk.getInstance().onPause(QuickActivity.this);
    }
    @Override
    public void onResume() {
        super.onResume();
        com.quicksdk.Sdk.getInstance().onResume(QuickActivity.this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        com.quicksdk.Sdk.getInstance().onStop(QuickActivity.this);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        com.quicksdk.Sdk.getInstance().onDestroy(QuickActivity.this);
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        com.quicksdk.Sdk.getInstance().onNewIntent(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        com.quicksdk.Sdk.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }
}
