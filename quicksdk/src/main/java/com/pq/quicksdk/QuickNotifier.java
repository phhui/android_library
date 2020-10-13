package com.pq.quicksdk;
import com.quicksdk.entity.UserInfo;
public interface QuickNotifier {
    void inited();
    void loginSuccess(UserInfo info);
    void loginFailed(String message, String trace);
    void signOut();
    void exit();
    void changeAccount(UserInfo info);
    void paySuccess(String sdkOrderID, String cpOrderID, String extrasParams);
    void payFailed(String cpOrderID, String message, String trace);
    void reportFailed(String msg);
}
