package me.funnyzhao.httpcomponent.http;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.funnyzhao.httpcomponent.intercepter.CacheInterceptor;
import me.funnyzhao.httpcomponent.intercepter.TransformInterceptor;
import me.funnyzhao.httpcomponent.intercepter.InitInterceptor;
import me.funnyzhao.httpcomponent.util.JsonLogUtil;
import me.funnyzhao.httpcomponent.util.LogUtil;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/*
 
 * -----------------------------------------------------------------
 
 * Copyright (C) by funnyzhao, All rights reserved.
 
 * -----------------------------------------------------------------
 
 * Author: funnyzhao
 
 * Create: 2018/4/23 11:41
 
 * Changes (from 2018/4/23)
 
 * -----------------------------------------------------------------
 * 描述:Global Http Allocator
 * 全局网络配置器,提供对外的自由配置
 * 请求头、baseurl、log、拦截器等
 * Builder构建
 */
public class GHttpAllocator {
    private static GHttpAllocator mInstance;
    
    public static GHttpAllocator getInstance() {
        if (mInstance == null) {
            synchronized (GHttpAllocator.class) {
                if (mInstance == null) {
                    mInstance = new GHttpAllocator();
                }
            }
        }
        return mInstance;
    }
    
    /**
     * 移除请求头
     * @return
     */
    public  GHttpAllocator removeHeaders(List<String> headerNames) {
        getGlobalOkHttpBuilder().addInterceptor(new InitInterceptor(headerNames,true));
        return this;
    }
    
    /**
     * 设置base_url
     * @param baseUrl
     * @return
     */
    public GHttpAllocator setBaseUrl(String baseUrl) {
        getGlobalRetrofitBuilder().baseUrl(baseUrl);
        return this;
    }
    
    /**
     * 添加请求头
     * @param headers
     * @return
     */
    public GHttpAllocator addHeaderInit(Map<String,Object> headers){
        getGlobalOkHttpBuilder().addInterceptor(new InitInterceptor(headers));
        return this;
    }
    
    
    /**
     * 改造请求体的拦截器
     * 包含请求头 请求体等
     * @return
     */
    public GHttpAllocator addTransformIntercept(){
        getGlobalOkHttpBuilder().addInterceptor(new TransformInterceptor());
        return this;
    }
    
    /**
     * 设置是否打印log,可在正式版时关闭
     * @param isShowLog true false
     * @return
     */
    public GHttpAllocator setLog(boolean isShowLog){
        if (isShowLog){
            HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                private StringBuilder mMessage = new StringBuilder();
                @Override
                public void log(String message) {
                    // 请求或者响应开始
                    if (message.startsWith("--> POST")) {
                        mMessage.setLength(0);
                    }
                    // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                    if ((message.startsWith("{") && message.endsWith("}"))
                            || (message.startsWith("[") && message.endsWith("]"))) {
                        message = JsonLogUtil.formatJson(JsonLogUtil.decodeUnicode(message));
                    }
                    mMessage.append(message.concat("\n"));
                    // 响应结束，打印整条日志
                    if (message.startsWith("<-- END HTTP")) {
                        LogUtil.netInfo(mMessage.toString());
                    }
                }
                
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            getGlobalOkHttpBuilder().addNetworkInterceptor(loggingInterceptor);
        }
        return this;
    }
    
    /**
     * 开启缓存，缓存到默认路径
     * @return
     */
    public GHttpAllocator setCache() {
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        Cache cache = new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/HttpCacheData")
                , 1024 * 1024 * 100);
        getGlobalOkHttpBuilder().addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .cache(cache);
        return this;
    }
    
    /**
     * 设置缓存路径及缓存文件大小
     * @param cachePath
     * @param maxSize
     * @return
     */
    public GHttpAllocator setCache(String cachePath, long maxSize) {
        if (!TextUtils.isEmpty(cachePath) && maxSize > 0) {
            CacheInterceptor cacheInterceptor = new CacheInterceptor();
            Cache cache = new Cache(new File(cachePath), maxSize);
            getGlobalOkHttpBuilder()
                    .addInterceptor(cacheInterceptor)
                    .addNetworkInterceptor(cacheInterceptor)
                    .cache(cache);
        }
        return this;
    }
    
    /**
     * 设置读取超时时间
     *
     * @param second
     * @return
     */
    public GHttpAllocator setReadTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }
    
    /**
     * 设置写入超时时间
     *
     * @param second
     * @return
     */
    public GHttpAllocator setWriteTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }
    
    /**
     * 设置连接超时时间
     *
     * @param second
     * @return
     */
    public GHttpAllocator setConnectTimeout(long second) {
        getGlobalOkHttpBuilder().readTimeout(second, TimeUnit.SECONDS);
        return this;
    }
    
    private Retrofit.Builder getGlobalRetrofitBuilder() {
        return RetrofitClient.getInstance().getRetrofitBuilder();
    }
    
    private OkHttpClient.Builder getGlobalOkHttpBuilder() {
        return HttpClient.getInstance().getBuilder();
    }
    
    /**
     * 全局的 retrofit
     * @return
     */
    public static Retrofit getGlobalRetrofit() {
        return RetrofitClient.getInstance().getRetrofit();
    }
    
    
    public static <K> K createApi(final Class<K> kClass){
        return getGlobalRetrofit().create(kClass);
    }
}
