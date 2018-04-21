package me.funnyzhao.httpcomponent;

import android.app.Application;
import android.content.Context;

import me.funnyzhao.httpcomponent.http.GHttpAllocator;

/**
 * Created by fz on 2018/4/21 17:02.
 * 描述:网络请求入口
 */
public class HttpUtil {
    private static Application mContext;
    private static HttpUtil mInstance;

    public static HttpUtil getInstance(){
        if (mContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 HttpUtil.init() 初始化！");
        }
        if (mInstance==null){
            synchronized (HttpUtil.class){
                if (mInstance==null){
                    mInstance=new HttpUtil();
                }
            }
        }
        return mInstance;
    }
    public static void init(Application app){
        mContext=app;
    }

    public GHttpAllocator startConfig(){
        return GHttpAllocator.getInstance();
    }

    /**
     * 创建业务Api
     * @param cls
     * @param <K>
     * @return
     */
    public static <K> K createApi(Class<K> cls){
        return GHttpAllocator.createApi(cls);
    }
    public static Context getContext(){
        if (mContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 HttpUtil.init() 初始化！");
        }
        return mContext;
    }

}
