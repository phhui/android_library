package com.pq.pqtools;

import android.text.TextUtils;
import android.util.Log;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.util.ArrayList;

public class ZipUtil {
    /**
     * 对文件列表压缩加密
     * @param srcfile
     * @param destZipFile
     * @param password
     * @return
     */
    public static File doZipFileList(File[] srcfile, String destZipFile, String password) {
        if (srcfile == null || srcfile.length == 0) {
            return null;
        }
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
        if (!TextUtils.isEmpty(password)) {
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        }
        ArrayList<File> existFileList = new ArrayList<File>();
        for (int i = 0; i < srcfile.length; i++) {
            if (srcfile[i] != null) {
                existFileList.add(srcfile[i]);
            }
        }
        try {
            ZipFile zipFile = new ZipFile(destZipFile,password.toCharArray());
            zipFile.addFiles(existFileList, zipParameters);
            return zipFile.getFile();
        } catch (ZipException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 对文件夹加密
     * @param folder
     * @param destZipFile
     * @param password
     * @return
     */
    public static File doZipFilesDir(File folder, String destZipFile, String password) {
        if (!folder.exists()) {
            return null;
        }
        ZipParameters parameters = new ZipParameters();
        // 加密方式
        if (!TextUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        }
        try {
            ZipFile zipFile = new ZipFile(destZipFile,password.toCharArray());
            zipFile.addFolder(folder, parameters);
            return zipFile.getFile();
        } catch (ZipException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 单文件压缩并加密
     * @param file 要压缩的zip文件
     * @param destZipFile zip保存路径
     * @param password 密码   可以为null
     * @return
     */
    public static File doZipFile(File file, String destZipFile, String password) {
        if (!file.exists()) {
            return null;
        }
        ZipParameters parameters = new ZipParameters();
        // 加密方式
        if (!TextUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        }
        try {
            ZipFile zipFile = new ZipFile(destZipFile,password.toCharArray());
            zipFile.addFile(file,parameters);
            return zipFile.getFile();
        } catch (ZipException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *   解压文件
     File：目标zip文件
     password：密码，如果没有可以传null
     path：解压到的目录路径
     */
    public static boolean unZip(String file,String password,String path) {
        boolean res = false;
        try {
            ZipFile zipFile = new ZipFile(file);
            if (zipFile.isEncrypted()) {
                if(password != null && !password.isEmpty()) {
                    zipFile.setPassword(password.toCharArray());
                }
            }
            zipFile.extractAll(path);
            res = true;
        } catch (ZipException e) {
            Log.d("log>>>","解压失败："+e.getMessage());
            e.printStackTrace();
        }
        return res;
    }
    /**
     *   解压文件
     File：目标zip文件
     password：密码，如果没有可以传null
     path：解压到的目录路径
     */
    public static boolean unZip(File file,String password,String path) {
        boolean res = false;
        try {
            ZipFile zipFile = new ZipFile(file);
            if (zipFile.isEncrypted()) {
                if(password != null && !password.isEmpty()) {
                    zipFile.setPassword(password.toCharArray());
                }
            }
            zipFile.extractAll(path);
            res = true;
        } catch (ZipException e) {
            Log.d("log>>>","解压失败："+e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

}