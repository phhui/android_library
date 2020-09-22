package com.pq.pqtools;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class DownUtil {
    private static DownNotifier dn;
    public static void downloadFile(final String url, final String savePath, DownNotifier _dn){
        dn=_dn;
        final long startTime = System.currentTimeMillis();
        Log.i("log>>>","startTime="+startTime);
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();// 下载失败
                dn.downFiled(e.getMessage());
                Log.i("log>>>","download failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
//                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, url.substring(url.lastIndexOf("/") + 1));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        dn.downing(progress);
                    }
                    fos.flush();
                    dn.downFinish();
                    Log.i("log>>>","download success");
                    Log.i("log>>>","totalTime="+ (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    dn.downFiled(e.getMessage());
                    Log.i("log>>>","download failed");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
}
