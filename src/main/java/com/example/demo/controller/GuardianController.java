package com.example.demo.controller;

import com.example.demo.bean.*;
import com.example.demo.service.*;
import com.example.demo.service.iot.Auth;
import com.example.demo.service.iot.DeviceManager;
import com.example.demo.tools.app.AppTask;
import com.example.demo.utils.SMSUtils;
import com.example.demo.utils.TextUtils;
import com.example.demo.utils.http.HttpResult;
import com.example.demo.utils.json.JObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.util.calendar.Gregorian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller("api/guardian")
public class GuardianController {
    @ResponseBody
    @RequestMapping(value = "api/guardian/child/isExistBindDevice", method = RequestMethod.POST)
    public static String isBindDevice(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int count = Child.isExistBindDevice(uid);
        HttpResult ret = new HttpResult();
        if(count == 0){
            ret.code = 1000;
            ret.message = "你还没有已绑定设备的被监护人!";
        }
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/child/constant", method = RequestMethod.POST)
    public String getProperty(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        List<PropertyInfoBean> infos = Child.getProperty();
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/child/add", method = RequestMethod.POST)
    public String addChildItem(@RequestBody String json){
        JObject jo = new JObject(json);

        int uid = jo.getInt("uid");
        String imei = jo.getString("imei");
        String name = jo.getString("name");
        int category = jo.getInt("category");
        int sex = jo.getInt("sex");
        String birthday = jo.getString("birthday");
        //判断必选项
        //选填
        String headimage = jo.getString("headimage");
        String phone = jo.getString("phone");
        String address = jo.getString("address");
        int health = jo.getInt("health");
        int looks = jo.getInt("looks");
        String contact = jo.getString("contact");
        String contact_phone = jo.getString("contact_phone");
        String device_address = jo.getString("device_address");

        HttpResult ret = new HttpResult();
        if(TextUtils.isEmpty(imei)){
            ret.code = 1000;
            ret.message = "设备码为空,请扫描设备二维码绑定设备!";
            return ret.toString();
        }

        Auth.login();
        DeviceResult result = DeviceManager.registerDevice(imei);
        if(result == null){
            ret.code = 1001;
            ret.message = "绑定设备失败!";
            return ret.toString();
        }
        if(TextUtils.isEmpty(result.deviceId)){
            ret.code = 1002;
            if(result.code.equals("100416")){
                ret.message = "设备已经被绑定!";
            }else {
                ret.message = "绑定设备失败!";
            }
            return ret.toString();
        }

        Object[] pams = {
                0,
                uid,
                imei,
                headimage,
                name,
                category,
                sex,
                birthday,
                phone,
                address,
                health,
                looks,
                contact,
                contact_phone,
                new Date(),
                result.deviceId,
                result.psk,
                device_address //device_register_address
        };
        int ret1 = Child.insert(pams);
        if(ret1 == -1){
            //删除设备
            DeviceManager.removeDevice(result.deviceId);
            ret.code = 1003;
            ret.message = "";
            return ret.toString();
        }
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/child/list", method = RequestMethod.POST)
    public static String getChildItems(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int pageIndex = jo.getInt("pageIndex");
        List<ChildInfoBean> infos = Child.getItems(uid, pageIndex);
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/child/update", method = RequestMethod.POST)
    public String updateChildItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        String imei = jo.getString("imei");
        String name = jo.getString("name");
        int category = jo.getInt("category");
        int sex = jo.getInt("sex");
        String birthday = jo.getString("birthday");
        //判断必选项
        //选填
        String headimage = jo.getString("headimage");
        String phone = jo.getString("phone");
        String address = jo.getString("address");
        String health = jo.getString("health");
        String looks = jo.getString("looks");
        String contact = jo.getString("contact");
        String contact_phone = jo.getString("contact_phone");
        boolean isChangeImei = jo.getBoolean("isChangeImei");
        String deviceId = jo.getString("deviceId");

        HttpResult ret = new HttpResult();
        //处理设备相关
        if(TextUtils.isEmpty(imei)){
            ret.code = 1000;
            ret.message = "设备码为空,请扫描设备二维码绑定设备!";
            return ret.toString();
        }

        //如果更换设备
        DeviceResult result = null;
        if(isChangeImei) {
            if(TextUtils.isEmpty(deviceId)){
                ret.code = 1001;
                ret.message = "旧设备ID不能为空!";
                return ret.toString();
            }
            Auth.login();
            //删除旧设备
            boolean isRemove = DeviceManager.removeDevice(deviceId);
            if (!isRemove) {
                ret.code = 1002;
                ret.message = "删除旧设备失败!";
                return ret.toString();
            }
            //注册新设备
            result = DeviceManager.registerDevice(imei);
            if (result == null) {
                ret.code = 1003;
                ret.message = "绑定新设备失败!";
                return ret.toString();
            }
            if (TextUtils.isEmpty(result.deviceId)) {
                ret.code = 1004;
                if (result.code.equals("100416")) {
                    ret.message = "此设备已经被绑定!";
                } else {
                    ret.message = "绑定新设备失败!";
                }
                return ret.toString();
            }
        }
        if(result == null){
            result = new DeviceResult();
            result.deviceId = "";
            result.psk = "";
        }

        if(isChangeImei) {
            Object[] pams = {
                    uid,
                    imei,
                    headimage,
                    name,
                    category,
                    sex,
                    birthday,
                    phone,
                    address,
                    health,
                    looks,
                    contact,
                    contact_phone,
                    new Date(),
                    result.deviceId,
                    result.psk,
                    childid,
            };
            Child.update(pams, isChangeImei);
        }else{
            Object[] pams = {
                    uid,
                    imei,
                    headimage,
                    name,
                    category,
                    sex,
                    birthday,
                    phone,
                    address,
                    health,
                    looks,
                    contact,
                    contact_phone,
                    new Date(),
                    childid,
            };
            Child.update(pams, isChangeImei);
        }

        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/child/getItem", method = RequestMethod.POST)
    public String getItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        ChildInfoBean info = Child.getItem(childid);
        HttpResult ret = new HttpResult(info);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/child/changeImei", method = RequestMethod.POST)
    public String changeImei(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        String name = jo.getString("name");
        String imei = jo.getString("imei");
        String deviceId = jo.getString("deviceId");
        String address = jo.getString("device_address");

        HttpResult ret = new HttpResult();
        if(TextUtils.isEmpty(imei)){
            ret.code = 1000;
            ret.message = "设备码为空,请扫描设备二维码绑定设备!";
            return ret.toString();
        }
        if(TextUtils.isEmpty(deviceId)){
            ret.code = 1001;
            ret.message = "旧设备ID不能为空!";
            return ret.toString();
        }

        Auth.login();
        //删除旧设备
        boolean isRemove = DeviceManager.removeDevice(deviceId);
        if(!isRemove){
            ret.code = 1002;
            ret.message = "删除旧设备失败!";
            return ret.toString();
        }
        //注册新设备
        DeviceResult result = DeviceManager.registerDevice(imei);
        if(result == null){
            ret.code = 1003;
            ret.message = "绑定新设备失败!";
            return ret.toString();
        }
        if(TextUtils.isEmpty(result.deviceId)){
            ret.code = 1004;
            if(result.code.equals("100416")){
                ret.message = "此设备已经被绑定!";
            }else {
                ret.message = "绑定新设备失败!";
            }
            return ret.toString();
        }

        Child.changeImei(childid, imei, result.deviceId, result.psk, address);
        //发送成功短信
        List<GuardianInfoBean> infos = Guardian.getItems(childid, 0);
        for(int i=0; i<infos.size(); i++){
            GuardianInfoBean info = infos.get(i);
            if(info.status == 1) {
                String username = info.username;
                SMSUtils.sendChangeImei(username, name, imei);
            }
        }

        return ret.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //监护人增，删除，查，改
    @ResponseBody
    @RequestMapping(value = "api/guardian/add", method = RequestMethod.POST)
    public String addItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        String name = jo.getString("name");
        String phone = jo.getString("phone");
        int status = jo.getInt("status");

        Object[] pams = {
                0,
                uid,
                childid,
                name,
                phone,
                status,
                new Date(),
                0,
        };
        Guardian.insert(pams);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/list", method = RequestMethod.POST)
    public String getItems(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        int pageIndex = jo.getInt("pageIndex");
        List<GuardianInfoBean> infos = Guardian.getItems(childid, pageIndex);
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/update", method = RequestMethod.POST)
    public String updateItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int id = jo.getInt("id");
        int uid = jo.getInt("uid");
        String name = jo.getString("name");
        String phone = jo.getString("phone");
        int status = jo.getInt("status");

        Object[] pams = {
                name,
                phone,
                new Date(),
                status,
                id,
        };
        Guardian.update(pams);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/setMain", method = RequestMethod.POST)
    public String setMain(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        int childid = jo.getInt("childid");
        Guardian.setMain(childid, id);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/unbind", method = RequestMethod.POST)
    public String unbind(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        String childName = jo.getString("name");
        String deviceId = jo.getString("deviceId");

        HttpResult ret = new HttpResult();
        if(TextUtils.isEmpty(deviceId)){
            ret.code = 1001;
            ret.message = "解绑的设备ID不能为空!";
            return ret.toString();
        }

        Auth.login();
        //删除旧设备
        Map<String, Object> deviceStatus = DeviceManager.checkDeviceStatus(deviceId);
        String error = deviceStatus.get("error_code").toString();
        int error_code = Integer.parseInt(error);
        if(error_code != 100403){ //设备不存在电信后台
            boolean isRemove = DeviceManager.removeDevice(deviceId);
            if(!isRemove){
                ret.code = 1002;
                ret.message = "解绑终端失败!";
                return ret.toString();
            }
        }

        //删除记录
        Child.delete(childid);

        //解除绑定成功,发送短信
        List<GuardianInfoBean> infos = Guardian.getItems(childid, 0);
        for(int i=0; i<infos.size(); i++){
            GuardianInfoBean info = infos.get(i);
            if(info.status == 1) {
                String username = info.username;
                SMSUtils.sendUnbind(username, childName);
            }
        }
        return ret.toString();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //报警设置，增删查改
    @ResponseBody
    @RequestMapping(value = "api/guardian/warn/setting", method = RequestMethod.POST)
    public String setWarnItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        boolean isrange = jo.getBoolean("isrange");
        int range = jo.getInt("range");
        boolean isdistance = jo.getBoolean("isdistance");
        int distance = jo.getInt("distance");
        boolean istime = jo.getBoolean("istime");
        int time = jo.getInt("time");
        boolean ispower = jo.getBoolean("ispower");
        int power = jo.getInt("power");

        WarningInfoBean info = Warning.getItem(childid);
        if(info == null){
            //insert
            Object[] pams = new Object[]{
                childid,
                    isrange,
                    range,
                    isdistance,
                    distance,
                    istime,
                    time,
                    ispower,
                    power
            };
            Warning.insert(pams);
        }else {
            //update
            Object[] pams = new Object[]{
                isrange,
                    isdistance,
                    distance,
                    istime,
                    time,
                    ispower,
                    power,
                    childid
            };
            Warning.update(pams);
        }
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/warn/getItem", method = RequestMethod.POST)
    public String getWarnItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        WarningInfoBean info = Warning.getItem(childid);
        if(info == null){
            info = new WarningInfoBean(); //默认值
        }
        HttpResult ret = new HttpResult(info);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/warn/history", method = RequestMethod.POST)
    public String history(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        List<WarHistoryInfoBean> infos = new ArrayList<>();
        for(int i=0; i<20; i++){
            WarHistoryInfoBean item = new WarHistoryInfoBean();
            item.msg = "11-11 09:22:00 上海市闵行区都市中路";
            item.speed = "0.2米/秒";
            infos.add(item);
        }
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //电子围栏
    @ResponseBody
    @RequestMapping(value = "api/guardian/efence/add", method = RequestMethod.POST)
    public String addEfenceItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        String name = jo.getString("name");
        String address = jo.getString("address");
        int radius = jo.getInt("radius");
        double latitude = jo.getDouble("latitude");
        double longitude = jo.getDouble("longitude");
        boolean status = jo.getBoolean("status");
        Object[] pams = new Object[]{
                0,
                childid,
                name,
                address,
                radius,
                status,
                latitude,
                longitude
        };
        Efence.insert(pams);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/efence/update", method = RequestMethod.POST)
    public String updateEfenceItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        String name = jo.getString("name");
        String address = jo.getString("address");
        int radius = jo.getInt("radius");
        double latitude = jo.getDouble("latitude");
        double longitude = jo.getDouble("longitude");
        boolean status = jo.getBoolean("status");
        Object[] pams = new Object[]{
                name,
                address,
                radius,
                status,
                latitude,
                longitude,
                id
        };
        Efence.update(pams);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/efence/list", method = RequestMethod.POST)
    public String getEfenceItems(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        int pageIndex = jo.getInt("pageIndex");
        List<EfenceInfoBean> infos = Efence.getItems(childid, pageIndex);
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/efence/getItem", method = RequestMethod.POST)
    public String getEfenceItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        EfenceInfoBean info = Efence.getItem(id);
        if(info == null){
            info = new EfenceInfoBean(); //默认值
        }
        HttpResult ret = new HttpResult(info);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/efence/remove", method = RequestMethod.POST)
    public String removeEfenceItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        Efence.delete(id);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //终端的设置
    @ResponseBody
    @RequestMapping(value = "api/guardian/device/setting", method = RequestMethod.POST)
    public String deviceSetting(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        int track = jo.getInt("track");
        int daily = jo.getInt("daily");
        int timer = jo.getInt("timer");
        int mode = jo.getInt("mode");

        DeviceSettingInfoBean info = Device.getItem(childid);
        if(info == null){
            Object[] pams = new Object[]{
                    childid,
                    track,
                    daily,
                    timer,
                    mode
            };
            Device.insert(pams);
        }else {
            Object[] pams = new Object[]{
                    track,
                    daily,
                    timer,
                    mode,
                    childid
            };
            Device.update(pams);
        }
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/device/getItem", method = RequestMethod.POST)
    public String getDeviceItem(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");
        DeviceSettingInfoBean info = Device.getItem(childid);
        HttpResult ret = new HttpResult(info);
        return ret.toString();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //地图所需要的信息
    @ResponseBody
    @RequestMapping(value = "api/guardian/map/getTrackLoc", method = RequestMethod.POST)
    public String getTrackLoc(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");

        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/map/getSmartLoc", method = RequestMethod.POST)
    public String getSmartLoc(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");

        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/map/getStartStopLoc", method = RequestMethod.POST)
    public String getStartStopLoc(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");

        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/guardian/map/getTimerLoc", method = RequestMethod.POST)
    public String getTimerLoc(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int childid = jo.getInt("childid");

        HttpResult ret = new HttpResult();
        return ret.toString();
    }

}
