package com.pq.pqtools;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
public class ApkUtil{
    public static void install(Context context,String apkPath,String packName) {
        File apk = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, packName+".fileprovider", apk);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(apk),"application/vnd.android.package-archive");
        }
        try {
            context.startActivity(intent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
