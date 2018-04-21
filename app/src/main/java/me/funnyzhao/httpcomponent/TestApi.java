package me.funnyzhao.httpcomponent;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by fz on 2018/4/21 18:14.
 * 描述:
 */
public interface TestApi {
    @GET("v2/book/1220562")
    Observable<BaseResopnse<String>> getBook();
}
