package com.pq.pqtools;

public interface PqWebviewNotifier {
    void onPageFinished(String url);
    void onReceiveJsValue(String value);
}
