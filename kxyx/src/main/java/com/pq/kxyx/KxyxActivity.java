package com.pq.kxyx;
import android.app.Activity;
import android.os.Bundle;

import com.kxyx.KxyxSDK;


public class KxyxActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        KxyxSDK.getInstance().onResume(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        KxyxSDK.getInstance().onPause(this);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        KxyxSDK.getInstance().exitSdk();
    }
}
