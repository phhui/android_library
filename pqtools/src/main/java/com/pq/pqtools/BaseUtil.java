package com.pq.pqtools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class BaseUtil {
    protected static Context context;
    protected static void init(Context ct){
        context=ct;
    }
    protected static void log(String msg){
        Log.d("log>>> ",msg);
    }
    protected static void tip(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        log(msg);
    }
}
