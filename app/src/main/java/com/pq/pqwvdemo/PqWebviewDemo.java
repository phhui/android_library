package com.pq.pqwvdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pq.pqwebview.PqWebview;
import com.pq.quickdemo.MainActivity;

public class PqWebviewDemo extends AppCompatActivity {
    private PqWebview wv=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wv = new PqWebview(PqWebviewDemo.this);
        wv.regFunc(PqWebviewDemo.this,"pq");
        wv.loadUrl("http://baidu.com");
    }
}
