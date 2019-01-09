package com.example.demo.controller;

import com.example.demo.bean.UserInfoBean;
import com.example.demo.service.User;
import com.example.demo.utils.http.HttpResult;
import com.example.demo.utils.json.JObject;
import com.example.demo.utils.json.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("api/test")
public class TestController {
    @ResponseBody
    @RequestMapping(value = "api/test/getJson", method = RequestMethod.POST)
    public String getJson(@RequestBody String json){
        JObject jo = new JObject(json);
        int id = jo.getInt("id");
        User user = new User();
        UserInfoBean bean = user.getUserInfo(id);
        HttpResult ret = new HttpResult();
        if(bean == null){
            ret.code = 1000;
            ret.message = "没有此用户!";
        }else {
            ret.data = JsonUtils.toString(bean);
        }
        return ret.toString();
    }
}
