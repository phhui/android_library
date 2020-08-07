package com.pq.kxyx;

import com.kxyx.bean.PayStateBean;
import com.kxyx.bean.UserInfoBean;

interface KxyxNotifier {
    void initSuccess();
    void loginSuccess(UserInfoBean info);
    void paySuccess(PayStateBean res);
}
