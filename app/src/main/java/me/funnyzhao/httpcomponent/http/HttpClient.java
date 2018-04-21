package me.funnyzhao.httpcomponent.http;

import okhttp3.OkHttpClient;

/**
 * Created by fz on 2018/4/21 17:05.
 * 描述:OkHttp
 */
public class HttpClient {
    private static HttpClient mInstance;
    private OkHttpClient.Builder mBuilder;

    public HttpClient(){
        mBuilder=new OkHttpClient.Builder();
    }

    public static HttpClient getInstance(){
        if (mInstance==null){
            synchronized (HttpClient.class){
                if (mInstance==null){
                    mInstance=new HttpClient();
                }
            }
        }
        return mInstance;
    }

    /**
     * 在RetrofitClient中构建Retrofit
     * @return
     */
    public OkHttpClient.Builder getBuilder(){
        return mBuilder;
    }
}
