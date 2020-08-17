package com.pq.kxyx;

import com.kxyx.bean.PayStateBean;
import com.kxyx.bean.UserInfoBean;

public interface KxyxNotifier {
    void initSuccess();
    void loginSuccess(UserInfoBean info);
    void paySuccess(PayStateBean res);
    void signOut();
}
