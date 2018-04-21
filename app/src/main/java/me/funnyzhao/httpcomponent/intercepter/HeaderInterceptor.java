package me.funnyzhao.httpcomponent.intercepter;


import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fz on 2018/4/21 17:36.
 * 描述:请求拦截器
 */
public class HeaderInterceptor implements Interceptor{
    private Map<String,Object> mHeadersMap=new TreeMap<>();
    public HeaderInterceptor(Map<String,Object> headers){
        this.mHeadersMap=headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        if (mHeadersMap != null && mHeadersMap.size() > 0) {
            for (Map.Entry<String, Object> entry : mHeadersMap.entrySet()) {
                request.addHeader(entry.getKey(), (String) entry.getValue());
            }
        }
        return chain.proceed(request.build());
    }
}
