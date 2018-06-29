package me.funnyzhao.httpcomponent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import me.funnyzhao.httpcomponent.observer.BaseObserver;
import me.funnyzhao.httpcomponent.observer.Transformer;

public class MainActivity extends AppCompatActivity {
    TestApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //-----------------------------------初始化-----------------------------------------------
        //Application中初始化
        HttpUtil.init(getApplication());
        HttpUtil.getInstance()
                .startConfig()
                .setBaseUrl("xx")
                .setCache()
                .setConnectTimeout(10)
                .setReadTimeout(10)
                .setWriteTimeout(10)
                .setLog(true)
                .addTransformIntercept();//如果需要对请求体改造调用此方法，请在此只调用一遍，防止出现多次拦截的错误
    
    
    
        //-----------------------------------适应业务，改造请求体-----------------------------------------------
        
        //1.初始设置请求头信息，此方法可多次调用，调用后，在发起新的请求前，需要充值Api对象
        HttpUtil.getInstance().startConfig().addHeaderInit(new HashMap<String, Object>());
        mApi=HttpUtil.createApi(TestApi.class);
        
        //2.移除请求头，支持单项或多项,在List中加入移除的名称即可
        HttpUtil.getInstance().startConfig().removeHeaders(new ArrayList<String>());
    
    
    
    
    
        //-----------------------------------MVP模式下的使用-----------------------------------------------
        
        //Api创建
        mApi= HttpUtil.createApi(TestApi.class);
        //Presenter层
        BaseObserver<BaseResopnse> observer=new BaseObserver<BaseResopnse>() {
            @Override
            protected void _onSub(Disposable d) {
                //返回一个disposable 可用于注册，在页面销毁时取消请求
            }
    
            @Override
            protected void _onNext(BaseResopnse baseResopnse) {
                //处理结果
            }
    
            @Override
            protected void _onError(String errorMsg) {
                //处理错误
            }
    
            @Override
            protected void showDialog() {
                //显示自定义进度条
            }
    
            @Override
            protected void hideDialog() {
                //隐藏自定义进度条
            }
        };
        
        //Model层
        mApi.getBook().compose(Transformer.<BaseResopnse>switchSchedulers())
                .subscribe(observer);
        
        
    }
}
