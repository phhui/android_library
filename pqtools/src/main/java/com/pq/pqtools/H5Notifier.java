package com.pq.pqtools;

public interface H5Notifier {
    /**statu  0未初始化，需要解压  1已初始化已是最新可正常进入游戏   2已初始化，需要更新  3开始下载更新文件  4更新文件下载失败   5更新进度*/
    void notifier(int statu, String msg);
}
