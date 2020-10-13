package com.pq.pqtools;
import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class H5Util extends BaseUtil{
    private static String verUrl="http://120.132.17.138/apk/test.json";
    private static String localPath;
    private static H5Notifier hn;
    public static String version;
    private static Timer timer = new Timer();
    public static void init(Context ct, H5Notifier _hn){
        init(ct);
        hn=_hn;
        DownloadUtil.init("http://120.132.17.138/");
        localPath=context.getFilesDir().getAbsolutePath();
        if(!FileUtil.checkDir(localPath+"/www")){
            hn.notifier(0,"");
            copyRes();
        }else {
            checkUpdate();
        }
    }
    public static void copyRes() {
        new Thread() {
            @Override
            public void run() {
                FileUtil.copy(context, "www.zip", localPath, "www.zip", new FileNotifier() {
                    @Override
                    public void copySuccess() {
                        Log.d("log>>>","解压文件："+localPath + "/www.zip");
                        unzipRes();
                    }
                    @Override
                    public void copyFailed() {
                        Log.d("log>>>","文件复制失败！");
                    }
                });
            }
        }.start();
    }
    public static void unzipRes(){
        if(FileUtil.checkDir(localPath + "/www.zip")) {
            ZipUtil.unZip(localPath + "/www.zip", "qdsj", localPath);
            FileUtil.del(localPath + "/www.zip");
            checkUpdate();
        }else{
            Log.d("log>>>",localPath + "/www.zip 不存在");
        }
    }
    public static String readFileFromAssets(String fileName){
        if (null == context || null == fileName) return null;
        try {
            InputStream input = context.getResources().getAssets().open(fileName);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.close();
            input.close();
            return output.toString();
        }catch(Exception err){
            return null;
        }
    }
    public static void checkUpdate(){
        getNewVer();
    }
    private static void getNewVer(){
        HttpUtil.get(verUrl, new HttpNotifier() {
            @Override
            public void onSuccess(String res) {
                if(res.contains("404 Not Found")) Log.d("log>>>","file no found  404");
                else{
                    Log.d("log>>>","http get success >> "+res);
                    checkVer(res);
                }
            }
            @Override
            public void onFailed(String res) {
                Log.d("log>>>","http get failed >> "+res);
            }
        });
    }
    private static void checkVer(String verStr){
        try {
            String localVer=readFileFromAssets("ver.json");//act.getResources().getAssets().open("ver.json").;//FileUtil.read("file:///android_asset/ver.json");
            Log.d("log>>>",verStr+"------"+localVer);
            JSONObject oldVer = new JSONObject(localVer);
            JSONObject newVer=new JSONObject(verStr);
            updateVersionFile(verStr);
            version=newVer.getString("version")+"."+newVer.getString("resVer");
            if(oldVer.getString("version").equals(newVer.getString("version"))){
                if(oldVer.getString("resVer").equals(newVer.getString("resVer"))){
                    Log.d("log>>>","版本一致，无需更新");
                    hn.notifier(1,"");
//                    JSONArray oldList = new JSONArray(oldVer.getJSONArray("list"));
//                    JSONArray newList = new JSONArray(newVer.getJSONArray("list"));
                }else {
                    Log.d("log>>>","需要更新资源包");
                    hn.notifier(3,newVer.getString("res"));
                    downFile(newVer.getString("res"));
                }
            }else{
                Log.d("log>>>","需要更新APK");
                hn.notifier(3,newVer.getString("res"));
                downFile(newVer.getString("apk"));
            }
        }catch (Exception err){
            hn.notifier(4,"检测版本异常："+err.getMessage()+"--"+err.toString());
        }
    }
    public static void downFile(final String url){
        Log.d("log>>>", "下载文件："+url);
        Log.d("log>>>","保存路径："+localPath);
//        DownUtil.downloadFile(url, localPath, new DownNotifier() {
        DownloadUtil.download(url,localPath,new DownNotifier() {
            @Override
            public void downFiled(String s) {
                Log.d("log>>>", "下载失败！"+s);
                reDownload(url);
//                hn.notifier(4,s);
            }
            @Override
            public void downFinish() {
                String type = url.substring(url.lastIndexOf(".") + 1);
                String fileName=url.substring(url.lastIndexOf("/"));
                if(FileUtil.checkDir(localPath  + fileName)) {
                    Log.d("log>>>", url + "下载成功！");
                    Log.d("log>>>", fileName);
                    Log.d("log>>>", "检测文件：" + localPath + fileName + "是否存在：" + FileUtil.checkDir(localPath + fileName));
                    if (type.equals("apk"))
                        ApkUtil.install(context, localPath + fileName, "com.cldwd.yx.qd");
                    else {
                        boolean isUnzip = ZipUtil.unZip(localPath + fileName, "qdsj", localPath);
                        FileUtil.del(localPath + fileName);
                        if(!isUnzip)reDownload(url);
                        else hn.notifier(1, "更新完成！");
                    }
                }else reDownload(url);
            }
            @Override
            public void downing(int i) {
                hn.notifier(5,String.valueOf(i));
            }
        });
    }
    private static void reDownload(final String url){
        Log.d("log>>>","下载失败，重新下载");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                downFile(url);
            }
        }, 800);
    }
    private static void updateVersionFile(String content){
        String verPath=localPath+"/www/ver.json";
        FileUtil.del(verPath);
        DownloadUtil.download(verUrl, localPath+"/www/", new DownNotifier() {
            @Override
            public void downFiled(String s) {
                Log.d("log>>>","版本文件更新失败");
            }
            @Override
            public void downing(int i) {

            }
            @Override
            public void downFinish() {
                Log.d("log>>>","版本文件更新成功！");
            }
        });
    }
}
