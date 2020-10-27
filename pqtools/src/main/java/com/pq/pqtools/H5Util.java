package com.pq.pqtools;
import android.content.Context;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class H5Util extends BaseUtil {
    public static String verUrl="http://120.132.17.138/apk/ver.json";
    private static String localPath;
    private static H5Notifier hn;
    public static String version;
    private static String newVerStr;
    private static boolean isCopy=false;
    private static Timer timer = new Timer();
    public static void init(Context ct, H5Notifier _hn){
        init(ct);
        hn=_hn;
        DownloadUtil.init("http://120.132.17.138/");
        localPath=context.getFilesDir().getAbsolutePath();
        String iscp=FileUtil.read(localPath+"/iscp");
        log("启动检测:"+iscp);
        if(iscp.length()>0){
            log("suc");
            isCopy=true;
        }else {
            log("fail");
            hn.notifier(0,"");
        }
        checkUpdate();
    }
    public static void copyRes() {
        if(isCopy)return;
        new Thread() {
            @Override
            public void run() {
                copyAssetsDir2Phone(context, "www");
            }
        }.start();
    }
    public static void copyAssetsDir2Phone(Context ct,String path){
        copyAssetsDir2Phone(ct,path,0);
    }
    /**
     *  从assets目录中复制整个文件夹内容,考贝到 /data/data/包名/files/目录中
     *  @param  activity  activity 使用CopyFiles类的Activity
     *  @param  filePath  String  文件路径,如：/assets/aa
     */
    public static void copyAssetsDir2Phone(Context activity, String filePath,int fnum){
        try {
            String[] fileList = activity.getAssets().list(filePath);
            if(fileList.length>0) {//如果是目录
                File file=new File(activity.getFilesDir().getAbsolutePath()+ File.separator+filePath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName:fileList){
                    filePath=filePath+File.separator+fileName;
                    copyAssetsDir2Phone(activity,filePath,1);
                    filePath=filePath.substring(0,filePath.lastIndexOf(File.separator));
                }
                if(fnum==0){
                    log("资源拷贝完成");
                    writeFileData("iscp","iscp");
                    isCopy=true;
                    log("拷贝确认："+FileUtil.read(localPath+"/iscp"));
//                    hn.notifier(1,"success");
                }
            } else {//如果是文件
                InputStream inputStream=activity.getAssets().open(filePath);
                File file=new File(activity.getFilesDir().getAbsolutePath()+ File.separator+filePath);
//                Log.i("copyAssets2Phone","file:"+file);
                if(!file.exists() || file.length()==0) {
                    FileOutputStream fos=new FileOutputStream(file);
                    int len=-1;
                    byte[] buffer=new byte[1024];
                    while ((len=inputStream.read(buffer))!=-1){
                        fos.write(buffer,0,len);
                    }
                    fos.flush();
                    inputStream.close();
                    fos.close();
//                    log("文件拷贝成功："+filePath);
                } else {
//                    log("文件已存在");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("文件拷贝失败："+e.getMessage()+"--"+e.getStackTrace());
        }
    }
    public static void unzipRes(){
        if(FileUtil.checkDir(localPath + "/www.zip")) {
            ZipUtil.unZip(localPath + "/www.zip", "qdsj", localPath);
            FileUtil.del(localPath + "/www.zip");
            checkUpdate();
        }else{
            log(localPath + "/www.zip 不存在");
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
    private static void checkUpdate(){
        HttpUtil.get(verUrl, new HttpNotifier() {
            @Override
            public void onSuccess(String res) {
                if(res.contains("404 Not Found")) log("file no found  404");
                else{
                    log("http get success >> "+res);
                    checkVer(res);
                }
            }
            @Override
            public void onFailed(String res) {
                log("http get failed >> "+res);
            }
        });
    }
    private static void checkVer(String verStr){
        try {
            String localVer = "{}";
            JSONObject newVer=new JSONObject(verStr);
            newVerStr=verStr;
            if(FileUtil.checkDir(localPath+"/ver.json")) localVer =FileUtil.read(localPath+"/ver.json");//readFileFromAssets("ver.json");//act.getResources().getAssets().open("ver.json").;//FileUtil.read("file:///android_asset/ver.json");
            else localVer=readFileFromAssets("ver.json");
            log(verStr+"------"+ localVer);
            JSONObject oldVer = new JSONObject(localVer);
            version=newVer.getString("version")+"."+newVer.getString("resVer");
            if(Float.parseFloat(oldVer.getString("version"))>=Float.parseFloat(newVer.getString("version"))){
                copyRes();
                if(oldVer.getString("resVer").equals(newVer.getString("resVer"))){
                    log("版本一致，无需更新");
                    if(isCopy)hn.notifier(1,"");
                }else if(Float.parseFloat(oldVer.getString("resVer"))<Float.parseFloat(newVer.getString("resVer"))){
                    log("需要更新资源包");
                    hn.notifier(3,newVer.getString("res"));
                    downFile(newVer.getString("res"));
                    updateVersionFile(verStr);
                }else{
                    version=oldVer.getString("version")+"."+oldVer.getString("resVer");
                    log("版本为最新，无需更新");
                    if(isCopy)hn.notifier(1,"");
                }
            }else{
                log("需要更新APK");
                hn.notifier(3,newVer.getString("res"));
                downFile(newVer.getString("apk"));
            }
        }catch (Exception err){
            copyRes();
            log("版本检测异常："+err.getMessage()+"--"+err.toString());
            hn.notifier(1,"检测版本异常："+err.getMessage()+"--"+err.toString());
        }
    }
    public static void downFile(final String url){
        log( "下载文件："+url);
        log("保存路径："+localPath);
//        DownUtil.downloadFile(url, localPath, new DownNotifier() {
        DownloadUtil.download(url,localPath,new DownNotifier() {
            @Override
            public void downFiled(String s) {
                log( "下载失败！"+s);
                reDownload(url);
//                hn.notifier(4,s);
            }
            @Override
            public void downFinish() {
                String type = url.substring(url.lastIndexOf(".") + 1);
                String fileName=url.substring(url.lastIndexOf("/"));
                if(FileUtil.checkDir(localPath  + fileName)) {
                    log( url + "下载成功！");
                    log( fileName);
                    log( "检测文件：" + localPath + fileName + "是否存在：" + FileUtil.checkDir(localPath + fileName));
                    if (type.equals("apk")) {
                        updateVersionFile(newVerStr);
                        ApkUtil.install(context, localPath + fileName, "com.cldwd.yx.qd");
                    }else {
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
        log("下载失败，重新下载");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                downFile(url);
            }
        }, 800);
    }
    public static void writeFileData(String filename, String content){
        try {
            FileOutputStream fos = context.openFileOutput(filename, context.MODE_PRIVATE);//获得FileOutputStream
            //将要写入的字符串转换为byte数组
            byte[]  bytes = content.getBytes();
            fos.write(bytes);//将byte数组写入文件
            fos.close();//关闭文件输出流

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void updateVersionFile(String content){
        String verPath=localPath+"/ver.json";
        FileUtil.del(verPath);
        writeFileData("ver.json",content);
        log("版本文件更新成功！");
        log("读取："+FileUtil.read(localPath+"/ver.json"));
        log("检测："+FileUtil.checkDir(localPath+"/ver.json"));
    }
}
