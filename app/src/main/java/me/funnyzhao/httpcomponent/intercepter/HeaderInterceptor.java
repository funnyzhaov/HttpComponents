package me.funnyzhao.httpcomponent.intercepter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fz on 2018/4/21 17:36.
 * 描述:请求拦截器
 */
public class HeaderInterceptor implements Interceptor{
    private Map<String, Object> mHeadersMap = new HashMap<>();
    private boolean mIsRemove=false;
    private List<String> mHeaderNames=new ArrayList<>();

    public HeaderInterceptor(Map<String, Object> headers) {
        this.mHeadersMap = headers;
    }
    public HeaderInterceptor(List<String> headerNames, boolean isRemove){
        this.mHeaderNames = headerNames;
        mIsRemove=isRemove;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuild = chain.request().newBuilder();
        if (mIsRemove) {
            for (String s : mHeaderNames) {
                requestBuild.removeHeader(s).build();
            }
        } else {
            if (mHeadersMap != null && mHeadersMap.size() > 0) {
                for (Map.Entry<String, Object> entry : mHeadersMap.entrySet()) {
                    requestBuild.addHeader(entry.getKey(), (String) entry.getValue());
                }
            }
        }
        return chain.proceed(requestBuild.build());
    }
}
