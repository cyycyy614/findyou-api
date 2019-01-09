package com.example.demo.controller.iot;

import com.example.demo.bean.DeviceResult;
import com.example.demo.service.iot.Auth;
import com.example.demo.service.iot.DeviceManager;
import com.example.demo.tools.app.AppTask;
import com.example.demo.utils.http.HttpResult;
import com.example.demo.utils.json.JObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller("api/iot/device")
public class DeviceController {
    @ResponseBody
    @RequestMapping(value = "api/iot/device/checkDeviceStatus", method = RequestMethod.POST)
    public String checkDeviceStatus(@RequestBody String json){
        String deviceId = "131927f3-36ed-494e-a4f1-71b53777dae2";
        Map<String,Object> ret = DeviceManager.checkDeviceStatus(deviceId);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/iot/device/register", method = RequestMethod.POST)
    public String registerDevice(@RequestBody String json){
        JObject jo = new JObject(json);
        String imei = jo.getString("imei");
        Auth.login();
        DeviceResult result = DeviceManager.registerDevice(imei);
        HttpResult ret = new HttpResult(result);
        return ret.toString();
    }
}
