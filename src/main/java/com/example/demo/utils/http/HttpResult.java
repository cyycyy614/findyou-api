package com.example.demo.utils.http;

import com.example.demo.utils.json.JObject;
import com.example.demo.utils.json.JsonUtils;

public class HttpResult {
    public int code = 200;
    public String message = "操作成功!";
    public String data = "";
    public HttpResult(){
    }

    public HttpResult(Object data)
    {
        if(data == null){
            this.data = "";
        }else {
            this.data = JsonUtils.toString(data);
        }
    }

    @Override
    public String toString(){
        return JsonUtils.toString(this);
    }
}
