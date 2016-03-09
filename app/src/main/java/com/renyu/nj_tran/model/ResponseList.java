package com.renyu.nj_tran.model;

import java.util.List;

/**
 * Created by renyu on 15/12/25.
 */
public class ResponseList<T> {
    List<T> data;
    int result;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
