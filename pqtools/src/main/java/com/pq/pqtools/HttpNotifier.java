package com.pq.pqtools;

public interface HttpNotifier {
    void onSuccess(String res);
    void onFailed(String res);
}
