package com.pq.kxyx;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kxyx.KxyxSDK;
import com.kxyx.bean.PayStateBean;
import com.kxyx.bean.ReportBean;
import com.kxyx.bean.UserInfoBean;
import org.json.JSONObject;
public class KxyxUtil {
    private static KxyxSDK instance;
    private static Activity act;
    private static KxyxNotifier kn;
    private static String reportParam;
    private static boolean isReport=false;
    public static void init(Activity _act,KxyxNotifier _kn){
        act=_act;
        kn=_kn;
        instance = KxyxSDK.getInstance().init(act);
        instance.setOnInitListener(new KxyxSDK.onInitListener()
        {
            @Override
            public void onSuccess()
            {
                kn.initSuccess();
                log("初始化成功");
            }
            @Override
            public void onFail(String msg){
                log("初始化失败");
            }
        });
        instance.setOnLogoutListener(new KxyxSDK.onLogoutListener()
        {
            @Override
            public void onResponse()
            {
                kn.signOut();
            }
        });
    }
    public static void requestPermissions(){
        try {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }catch(Exception err){
            log("请求权限异常："+err.getMessage());
        }
    }
    public static boolean checkPermissions() {
        try {
            if ((ContextCompat.checkSelfPermission(act, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                return false;
            return true;
        } catch (Exception err) {
            log("检测权限异常：" + err.getMessage());
        }
        return false;
    }
    public static void signIn(){
        instance.login(act);
        instance.setOnLoginListener(new KxyxSDK.onLoginListener()
        {
            @Override
            public void onSuccess(UserInfoBean userInfoBean)
            {
                kn.loginSuccess(userInfoBean);
            }
            @Override
            public void onFail(String msg)
            {
                log("登录失败："+msg);
            }
        });
    }
    public static void pay(String jsonStr){
        if(!isReport){
            showDialog("✖✖✖✖✖✖注意，角色未上报！✖✖✖✖✖✖");
            tip("✖✖✖✖✖✖注意，角色未上报！✖✖✖✖✖✖");
        }
        try {
            JSONObject obj = new JSONObject(jsonStr);
            String money = obj.getString("money");
            String goods = obj.getString("goods");
            String goods_desc = obj.getString("goods_desc");
            String game_order_sn = obj.getString("game_order_sn");
            String game_api_url = obj.getString("game_api_url");
            String game_server_id = obj.getString("game_server_id");
            String game_zone = obj.getString("game_zone");
            String role_id = obj.getString("role_id");
            String role_name = obj.getString("role_name");
            String ext = obj.getString("ext");
            if (!TextUtils.isEmpty(goods) && !TextUtils.isEmpty(goods_desc)) {
                if (TextUtils.isEmpty(ext))
                    instance.pay(act, money, goods, goods_desc, game_order_sn, game_api_url, game_server_id, game_zone, role_id, role_name);
                else
                    instance.pay(act, money, goods, goods_desc, game_order_sn, game_api_url, game_server_id, game_zone, role_id, role_name, ext);
            } else {
                if (TextUtils.isEmpty(ext))
                    instance.pay(act, money, game_order_sn, game_api_url, game_server_id, game_zone, role_id, role_name);
                else
                    instance.pay(act, money, game_order_sn, game_api_url, game_server_id, game_zone, role_id, role_name, ext);
            }

            instance.setOnPayListener(new KxyxSDK.onPayListener() {
                @Override
                public void onResponse(PayStateBean payStateBean) {
                    switch (payStateBean.getState()) {
                        case "0":
                            log("待付款");
                            break;
                        case "1":
                            log("已付款");
                            break;
                        case "2":
                            log("未付款");
                            break;
                        case "3":
                            kn.paySuccess(payStateBean);
                            break;
                    }
                }
            });
        }catch(Exception err){
            log("支付参数错误，请检查！--"+err.getMessage());
        }
    }
    public static void report(String jsonStr){
        reportParam=jsonStr;
        try {
            JSONObject obj=new JSONObject(jsonStr);
            instance.reportUpgrade(obj.getString("server_name"),obj.getString("server_id"),obj.getString("role_name"),obj.getString("role_id"),obj.getString("level"),obj.getString("create_role_time"));
            instance.setOnUpgradeListener(new KxyxSDK.onUpgradeListener() {
                @Override
                public void onSuccess(ReportBean reportBean) {
                    isReport=true;
                    log("上报成功：" + reportBean.toString());
                }

                @Override
                public void onFail(String msg){
                    log("上报失败重新上报："+msg);
                    report(reportParam);
                }
            });
        }catch(Exception err){
            log("上报参数错误："+err.getMessage());
        }
    }
    public static void showDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        //    设置Title的图标
//        builder.setIcon(R.drawable.icon);
        //    设置Title的内容
        builder.setTitle("注意");
        //    设置Content来显示一个信息
        builder.setMessage(msg);
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(act, "positive: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //    设置一个NegativeButton
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                Toast.makeText(act, "negative: " + which, Toast.LENGTH_SHORT).show();
//            }
//        });
//        //    设置一个NeutralButton
//        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                Toast.makeText(act, "neutral: " + which, Toast.LENGTH_SHORT).show();
//            }
//        });
        //    显示出该对话框
        builder.show();
    }
    public static void tip(String msg){
        Toast.makeText(act, msg, Toast.LENGTH_SHORT).show();
        log(msg);
    }
    public static void log(String msg){
        Log.d("log>>>",msg);
    }
}
