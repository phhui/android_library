package com.pq.pqtools;

public interface FileNotifier {
    void copySuccess();
    void copyInfo(String msg);
    void copyFailed();
}
