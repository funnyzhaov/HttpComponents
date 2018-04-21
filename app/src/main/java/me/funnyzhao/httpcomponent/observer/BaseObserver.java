package me.funnyzhao.httpcomponent.observer;

import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.funnyzhao.httpcomponent.HttpUtil;
import me.funnyzhao.httpcomponent.exception.ApiException;
import me.funnyzhao.httpcomponent.util.NetUtil;

/**
 * Created by fz on 2018/4/21 18:24.
 * 描述:
 */
public abstract class BaseObserver<T> implements Observer<T> {

    public BaseObserver() {}

    @Override
    public void onError(Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        _onError(error);
    }

    @Override
    public void onSubscribe(Disposable d) {
        if(!NetUtil.isNetworkAvailable(HttpUtil.getContext())) {
            Toast.makeText(HttpUtil.getContext(), "当前网络不可用，请检查网络情况", Toast.LENGTH_SHORT).show();
            onComplete();
            return;
        }
        //建立连接
        showDialog();
    }

    @Override
    public void onComplete() {
        //请求完毕
        hideDialog();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    public abstract void _onNext(T t);

    public abstract void _onError(String errorMsg);

    /**
     * 进度条或对话框
     */
    protected abstract void showDialog();

    protected abstract void hideDialog();
}
