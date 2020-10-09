package com.pq.pqtools;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static void get(final String url, final HttpNotifier hn){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    hn.onSuccess(responseData);
                }catch (Exception e){
                    hn.onFailed(e.getMessage());
//                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void post(final String url, final RequestBody param, final HttpNotifier hn){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("username","20100539")
//                            .add("password","123456")
//                            .build();
                    Request request = new Request.Builder().url(url).post(param).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    hn.onSuccess(responseData);
                }catch (Exception e){
                    hn.onFailed(e.getMessage());
//                    e.printStackTrace();
                }
            }
        }).start();
    }
}
