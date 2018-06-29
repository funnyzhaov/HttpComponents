package me.funnyzhao.httpcomponent;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by fz on 2018/4/21 18:14.
 * 描述:
 */
public interface TestApi {
    @POST("v2/book/1220562")
    Observable<BaseResopnse> getBook();
}
