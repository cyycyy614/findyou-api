package com.example.demo.controller.iot;

import com.example.demo.service.iot.DeviceManager;
import com.example.demo.tools.app.AppTask;
import com.example.demo.utils.http.HttpResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("api/iot/data")
public class DataController {
    @ResponseBody
    @RequestMapping(value = "api/iot/data/getCurrentData", method = RequestMethod.POST)
    public String getCurrentData(@RequestBody String json){
        String deviceId = "131927f3-36ed-494e-a4f1-71b53777dae2";
        AppTask.CurrentData data = DeviceManager.getCurrentData(deviceId);
        HttpResult ret = new HttpResult(data);
        return ret.toString();
    }
}
