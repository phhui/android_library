package com.pq.pqwebview;

public interface IPqWebviewListener {
    void onPageFinished(String url);
    void onReceiveJsValue(String value);
}
