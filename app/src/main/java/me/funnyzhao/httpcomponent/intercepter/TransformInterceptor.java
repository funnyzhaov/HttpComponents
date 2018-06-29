package me.funnyzhao.httpcomponent.intercepter;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.funnyzhao.httpcomponent.util.LogUtil;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/*

 * -----------------------------------------------------------------

 * Copyright (C) by funnyzhao, All rights reserved.

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2018/4/23 11:45

 * Changes (from 2018/4/23)

 * -----------------------------------------------------------------
   请求体处理，加密等需求
   这个转换器可以根据项目实际需求来创建，设置内部的处理方式
   拦截器
 */
public class TransformInterceptor implements Interceptor {
    private String paramName;//单参数
    
    public TransformInterceptor() {
    
    }
    public TransformInterceptor(String addParamName) {
       this.paramName=addParamName;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        LogUtil.info("TransformInterceptor","------>进入拦截");
        
        Request.Builder requestBuild=chain.request().newBuilder();
        
        Request request = chain.request();
        try {
            requestBuild = transformBody(request,requestBuild);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(requestBuild.build());
    }
    
    //改造请求体，比如添加一个sign参数
    private Request.Builder transformBody(Request request,Request.Builder builder) throws Exception {
        RequestBody requestBody = request.body();
        if (requestBody != null && paramName!=null) {
            MediaType contentType = requestBody.contentType();
            //添加参数返回json
            String endEnctyStr = putCommonParams(request);
            LogUtil.info("TransformInterceptor","改造后： "+endEnctyStr);
            //转换为json，再次构建请求
            builder = request.newBuilder().post(RequestBody.create(contentType, endEnctyStr));
        }
        return builder;
    }
    
    /**
     * 添加请求体参数
     * @param request
     * @return
     */
    private String putCommonParams(Request request) {
        RequestBody requestBody = request.body();
        HashMap<String, Object> rootMap = new HashMap<>();
        
        //buffer流
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //获取之前的请求参数json
        String oldParamsJson = buffer.readUtf8();
        if (oldParamsJson==null){
            return null;
        }
        
        //转换为map形式
        rootMap= JSON.parseObject(oldParamsJson,HashMap.class);
        if (rootMap==null){
            rootMap=new HashMap<>();
        }
        
        //在新的sortMap中添加旧的参数
        TreeMap sortMap = new TreeMap();
        for(String key:rootMap.keySet()){
            if(!paramName.equals(key)){
                sortMap.put(key,rootMap.get(key));
            }
        }
        //将旧参数转json，做MD5加密
        String sortMapJson = new Gson().toJson(sortMap);
        LogUtil.info("TransformInterceptor","------> 加MD5之前 : "+sortMapJson);
        
        //然后添加sign参数
        sortMap.put(paramName, encryptMd5(sortMapJson));
        
        //最终转成json字符串
        String endJsonParams = new Gson().toJson(sortMap);
        LogUtil.info("TransformInterceptor","------> 加了MD5后 "+endJsonParams);
        
        return endJsonParams;
    }
    
    /**
     * MD5加密
     * @param string
     * @return
     */
    private String encryptMd5(String string) {
        return string;
    }
    
    
}
