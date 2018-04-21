package me.funnyzhao.httpcomponent;

/**
 * Created by fz on 2018/4/21 18:34.
 * 描述:
 */
public class BaseResopnse<T> {
    private String code;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
