package com.pq.pqtools;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtil {
    private static FileNotifier fn;
    /**
     * @param myContext
     * @param ASSETS_NAME 要复制的文件名
     * @param savePath    要保存的路径
     * @param saveName    复制后的文件名
     * testCopy(Context context)是一个测试例子。
     */
    public static void copy(Context myContext, String ASSETS_NAME,String savePath, String saveName) {
        String filename = savePath + "/" + saveName;
        File dir = new File(savePath);
        // 如果目录不中存在，创建这个目录
        if (!dir.exists())
            dir.mkdir();
        try {
            if (!(new File(filename)).exists()) {
                InputStream is = myContext.getResources().getAssets().open(ASSETS_NAME);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            Log.d("log>>>","copy failed>"+e.getMessage());
            e.printStackTrace();
        }
    }
    public static boolean checkDir(String dirPath){
        File f=new File(dirPath);
        return f.exists();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean unzip(String dir, String filePath,FileNotifier _fn){
        fn=_fn;
        try {
            BufferedInputStream bi;
            ZipFile zf = new ZipFile(filePath, Charset.forName("GBK"));
            Enumeration e = zf.entries();
            File fi;
            FileOutputStream fos;
            while (e.hasMoreElements())
            {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
                String path = dir + "/" + entryName;
                if (ze2.isDirectory())
                {
                    System.out.println("正在创建解压目录 - " + path);
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()){
                        decompressDirFile.mkdirs();
                    }
                } else {
//                    System.out.println("正在创建解压文件 - " + entryName);
                    fi = new File(path);
                    System.out.println("正在创建解压文件 - " + path);
                    File fileParent = fi.getParentFile();//返回的是File类型,可以调用exsit()等方法
                    String fileParentPath = fi.getParent();//返回的是String类型
//                    System.out.println("fileParent:" + fileParent);
//                    System.out.println("fileParentPath:" + fileParentPath);
                    if (!fileParent.exists()) {
                        fileParent.mkdirs();// 能创建多级目录
                    }
                    if (!fi.exists())
                        fi.createNewFile();//有路径才能创建文件

//                    if (!fi.exists()){
//                        System.out.println("文件不存在");
//                    }else System.out.println("文件已存在");
//                    Log.d("log>>>","a");
                    fos=new FileOutputStream(fi);
//                    Log.d("log>>>","b");
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
//                    Log.d("log>>>","c");
                    bi = new BufferedInputStream(zf.getInputStream(ze2));
                    byte[] readContent = new byte[1024];
                    int readCount = bi.read(readContent);
//                    Log.d("log>>>","d");
                    while (readCount != -1)
                    {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.close();
                }
            }
            zf.close();
            fn.unzipFinish();
        }catch(Exception err){
            Log.d("log>>>","unzip failed:"+err.getMessage());
            fn.unzipFailed();
            return false;
        }
        return true;
    }
    public static boolean del(String filePath){
        File file=new File(filePath);
        if(file.exists()&&file.isFile()){
            if(file.delete()){
                Log.d("log>>>","删除文件:"+filePath);
                return true;
            }else{
                Log.d("log>>>","文件:"+filePath+"删除失败！");
            }
        }else{
            Log.d("log>>>","文件"+filePath+"不存在或不是文件");
        }
        return false;
    }
    public static boolean unpackZip(String path, String zipname){
        InputStream is;
        ZipInputStream zis;
        try{
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;
            while ((ze = zis.getNextEntry()) != null){
                filename = ze.getName();
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(path + filename);
                while ((count = zis.read(buffer)) != -1){
                    fout.write(buffer, 0, count);
                }
                fout.close();
                zis.closeEntry();
            }
            zis.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void testCopy(Context context) {
        String path = context.getFilesDir().getAbsolutePath();
        String name = "test.txt";
        FileUtil.copy(context, name, path, name);
    }
}
