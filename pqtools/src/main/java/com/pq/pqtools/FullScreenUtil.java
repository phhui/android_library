package com.pq.pqtools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.Timer;
import java.util.TimerTask;

public class FullScreenUtil {
    private static Handler handler;
    @SuppressLint("HandlerLeak")
    public static void autoFullScreen(final Activity act){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                setFullscreen(act);
            }
        };
        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTimerTask, 0,2000);
    }
    @SuppressLint("HandlerLeak")
    public static void autoHideNavigation(final Activity act){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                hideNavigation(act);
            }
        };
        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        mTimer.schedule(mTimerTask, 0,2000);
    }
    public static void setFullscreen(Activity act){
        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View v=act.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            v.setSystemUiVisibility(uiOptions);
        }
    }
    public static void hideTitle(Activity act){
        act.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    public static void hideStatusBar(Activity act){
        View v=act.getWindow().getDecorView();
        act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    public static void hideNavigation(Activity act){
        View v=act.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            v.setSystemUiVisibility(uiOptions);
        }
    }
}
