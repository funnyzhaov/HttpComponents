package me.funnyzhao.httpcomponent.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fz on 2018/4/21 17:06.
 * 描述: retrofit 配置
 */
public class RetrofitClient {

    private static RetrofitClient mInstance;
    private Retrofit.Builder mRetrofitBuilder;
    private OkHttpClient.Builder mOkHttpBuilder;

    public RetrofitClient(){

        mOkHttpBuilder=HttpClient.getInstance().getBuilder();

        mRetrofitBuilder=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    public static RetrofitClient getInstance(){
        if (mInstance==null){
            synchronized (RetrofitClient.class){
                if (mInstance==null){
                    mInstance=new RetrofitClient();
                }
            }
        }
        return mInstance;
    }

    public Retrofit.Builder getRetrofitBuilder(){
        return mRetrofitBuilder;
    }

    /**
     * 构建Retrofit
     * @return
     */
    public Retrofit getRetrofit(){
        return mRetrofitBuilder.client(mOkHttpBuilder.build()).build();
    }



}
