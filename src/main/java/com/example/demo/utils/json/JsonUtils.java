package com.example.demo.utils.json;

import com.example.demo.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.internal.Primitives;
import org.springframework.boot.json.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class JsonUtils {
    private static Gson gson = new Gson();
    public static  <T> T convert(String jsonData, Class<T> clazz){
        Object bean = null;
        try {
            bean = gson.fromJson(jsonData, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Primitives.wrap(clazz).cast(bean);
    }

    public static String toString(Object obj){
        String str = gson.toJson(obj);
        return str;
    }

    public static boolean isJsonStr(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static <T> List<T> convert_array(String json, Class clazz){
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(json, type);
        return list;
    }
}
