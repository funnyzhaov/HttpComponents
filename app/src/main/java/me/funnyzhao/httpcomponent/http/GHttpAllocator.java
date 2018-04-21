package me.funnyzhao.httpcomponent.http;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.funnyzhao.httpcomponent.intercepter.CacheInterceptor;
import me.funnyzhao.httpcomponent.intercepter.HeaderInterceptor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by fz on 2018/4/21 17:04.
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
     * 设置base_url
     * @param baseUrl
     * @return
     */
    public GHttpAllocator setBaseUrl(String baseUrl) {
        getGlobalRetrofitBuilder().baseUrl(baseUrl);
        return this;
    }

    /**
     * 设置请求头
     * @param headers
     * @return
     */
    public GHttpAllocator setHeaders(Map<String,Object> headers){
        getGlobalOkHttpBuilder().addInterceptor(new HeaderInterceptor(headers));
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
                @Override
                public void log(String message) {
                    Log.e("HttpUtil", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            getGlobalOkHttpBuilder().addInterceptor(loggingInterceptor);
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
