package me.funnyzhao.httpcomponent.observer;

import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.funnyzhao.httpcomponent.HttpUtil;
import me.funnyzhao.httpcomponent.exception.ApiException;
import me.funnyzhao.httpcomponent.util.NetUtil;
/*
 
 * -----------------------------------------------------------------
 
 * Copyright (C) by funnyzhao, All rights reserved.
 
 * -----------------------------------------------------------------
 
 * Author: funnyzhao
 
 * Create: 2018/4/23 11:48
 
 * Changes (from 2018/4/23)
 
 * -----------------------------------------------------------------
 
 */

public abstract class BaseObserver<T> implements Observer<T> {
    
    public BaseObserver() {
    }
    
    @Override
    public void onError(Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        hideDialog();
        _onError(error);
    }
    
    @Override
    public void onSubscribe(Disposable d) {
        if (!NetUtil.isNetworkAvailable(HttpUtil.getContext())) {
            Toast.makeText(HttpUtil.getContext(), "当前网络不可用，请检查网络情况", Toast.LENGTH_SHORT).show();
            onComplete();
            return;
        }
        _onSub(d);
        //建立连接
        showDialog();
    }
    
    protected abstract void _onSub(Disposable d);
    
    @Override
    public void onComplete() {
        //请求完毕
        hideDialog();
    }
    
    @Override
    public void onNext(T t) {
        _onNext(t);
    }
    
    /**
     *
     *响应
     * @param t
     */
    protected abstract void _onNext(T t);
    
    /**
     * 错误
     * @param errorMsg
     */
    
    protected abstract void _onError(String errorMsg);
    
    /**
     * 进度条或对话框
     */
    protected abstract void showDialog();
    
    protected abstract void hideDialog();
    
    
}