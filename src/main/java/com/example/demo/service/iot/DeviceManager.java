package com.example.demo.service.iot;

import com.example.demo.bean.DeviceResult;
import com.example.demo.tools.app.AppTask;

import java.util.Map;

public class DeviceManager {
    public static DeviceResult registerDevice(String imei){
        AppTask myApp = Auth.getMyApp();
        if(myApp == null){
            return null;
        }
        String data = "";
        try{
            DeviceResult ret = myApp.registerDirectDevice(imei, imei, 0); //0表示永不过期
            return ret;
        }catch (Exception e){
        }
        return null;
    }

    public static boolean removeDevice(String deviceId){
        AppTask myApp = Auth.getMyApp();
        if(myApp == null){
            return false;
        }
        String data = "";
        try{
            boolean ret = myApp.deleteDevice(deviceId);
            return ret;
        }catch (Exception e){
        }
        return false;
    }

    public static Map<String, Object> checkDeviceStatus(String deviceId){
        AppTask myApp = Auth.getMyApp();
        if(myApp == null){
            return null;
        }
        Map<String, Object> ret = null;
        try{
            ret = myApp.checkDeviceStatus(deviceId);
        }catch (Exception e){
        }
        return ret;
    }

    public static AppTask.CurrentData getCurrentData(String deviceId){
        AppTask myApp = Auth.getMyApp();
        if(myApp == null){
            return null;
        }
        AppTask.CurrentData data = null;
        try{
            data = myApp.queryRawData(deviceId);
            return data;
        }catch (Exception e){
        }
        return null;
    }
}
