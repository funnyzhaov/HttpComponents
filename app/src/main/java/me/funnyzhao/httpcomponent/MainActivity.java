package me.funnyzhao.httpcomponent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import me.funnyzhao.httpcomponent.observer.BaseObserver;
import me.funnyzhao.httpcomponent.observer.Transformer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                .setHeaders(new HashMap<String, Object>());
        //业务层调用
        HttpUtil.createApi(TestApi.class)
                .getBook()
                .compose(Transformer.<BaseResopnse<String>>switchSchedulers())
                .subscribe(new BaseObserver<BaseResopnse<String>>() {
                    @Override
                    public void _onNext(BaseResopnse<String> stringBaseResopnse) {

                    }
                    @Override
                    public void _onError(String errorMsg) {

                    }
                    @Override
                    protected void showDialog() {

                    }
                    @Override
                    protected void hideDialog() {

                    }
                });
    }
}
