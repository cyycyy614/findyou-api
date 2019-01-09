package com.example.demo.controller;

import com.example.demo.service.iot.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("api/iot")
public class IoTController {
    @ResponseBody
    @RequestMapping(value = "api/iot/login", method = RequestMethod.POST)
    public String login(@RequestBody String json){
        Auth auth = new Auth();
        auth.login();
        return "ok";
    }
}
