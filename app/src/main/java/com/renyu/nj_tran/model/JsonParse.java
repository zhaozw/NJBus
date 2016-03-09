package com.renyu.nj_tran.model;

import com.renyu.nj_tran.common.GsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by renyu on 16/3/9.
 */
public class JsonParse {
    public static int getResult(String string) {
        try {
            JSONObject object=new JSONObject(string);
            return object.getInt("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static <T>T getModelValue(String string, Class<T> class_) {
        Response response;
        Type objectTypes= GsonUtils.type(Response.class, class_);
        response=GsonUtils.getGson().fromJson(string, objectTypes);
        return (T) response.getData();
    }

    public static <T>List<T> getModelListValue(String string, Class<T> class_) {
        ResponseList response;
        Type objectTypes= GsonUtils.type(ResponseList.class, class_);
        response= GsonUtils.getGson().fromJson(string, objectTypes);
        return response.getData();
    }
}
