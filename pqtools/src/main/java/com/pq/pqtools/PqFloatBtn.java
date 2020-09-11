package com.pq.pqtools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class PqFloatBtn {
    private static Activity ct;
    private static FrameLayout view;
    private static ImageView backBtn;
    private static FrameLayout.LayoutParams backBtnParam;
    private static Timer timer = new Timer();
    private static PqFloatBtnNotifier notifier;
    @SuppressLint("ClickableViewAccessibility")
    public static ImageView create(final Activity _ct, int icon, final PqFloatBtnNotifier _notifier){
        ct=_ct;
        notifier=_notifier;
        view=(FrameLayout)ct.getWindow().getDecorView();
        backBtn = new ImageView(ct);//new 一个ImageView
        backBtn.setImageDrawable(ct.getResources().getDrawable(icon));//设置资源
        backBtnParam=new FrameLayout.LayoutParams(150,150);//两个400分别为添加图片的大小
        backBtnParam.leftMargin=50;
        backBtnParam.topMargin=150;
        backBtn.setLayoutParams(backBtnParam);
        view.addView(backBtn);
        backBtn.setVisibility(View.INVISIBLE);
        backBtn.setAlpha((float) 0.5);
        backBtn.setOnTouchListener(onTouch);
        return backBtn;
    }
    private static View.OnTouchListener onTouch=new View.OnTouchListener(){
        private int firstX,firstY;
        private int lastX;
        private int lastY;
        private int maxRight=0;
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(maxRight==0) {//保证只赋一次值
                        Point s = new Point();
                        ct.getWindowManager().getDefaultDisplay().getRealSize(s);
                        maxRight = s.x;
                    }
                    lastX =eventX;
                    lastY = eventY;
                    firstX =eventX;
                    firstY = eventY;
//                    notifier.onDown(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    backBtnParam.leftMargin=backBtn.getLeft()+eventX-lastX;
                    backBtnParam.topMargin=backBtn.getTop()+eventY-lastY;
                    backBtn.setLayoutParams(backBtnParam);
                    lastX = eventX;
                    lastY = eventY;
//                    notifier.onMove(event);
                    break;
                case MotionEvent.ACTION_UP:
                    if(eventX-firstX<30&&eventY-firstY<30){
                        if(backBtnParam.width!=200){
                            resetBackBtn(false,false);
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    resetBackBtn(true,false);
                                }
                            }, 2000);
                        }else{
                            resetBackBtn(true,true);
                            notifier.onClick(event);
                        }
                    }else{
                        resetBackBtn(true,false);
                    }
                    break;
                default:
                    break;
            }
            return true;//所有的motionEvent都交给imageView处理
        }
    };
    private static void resetBackBtn(final boolean def, final boolean reset) {
        ct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backBtnParam.width = (def ? 150 : 200);
                backBtnParam.height = (def ? 150 : 200);
                backBtn.setAlpha((float) (def ? 0.5 : 1));
                if (reset) {
                    backBtnParam.leftMargin = 50;
                    backBtnParam.topMargin = 150;
                }
                backBtn.setLayoutParams(backBtnParam);
            }
        });
    }
}
interface PqFloatBtnNotifier{
    void onClick(MotionEvent e);
//    void onDown(MotionEvent e);
//    void onMove(MotionEvent e);
}