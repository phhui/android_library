package com.pq.pqtools;

public interface DownNotifier {
    void downFinish();
    void downFiled(String msg);
    void downing(int progress);
}
