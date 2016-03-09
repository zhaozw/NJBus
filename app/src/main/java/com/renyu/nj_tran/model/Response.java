package com.renyu.nj_tran.model;

/**
 * Created by renyu on 15/12/16.
 */
public class Response<T> {

    T data;
    int result;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
