package com.example.demo.service;

import com.example.demo.bean.DeviceSettingInfoBean;
import com.example.demo.bean.EfenceInfoBean;
import com.example.demo.utils.db.DataRow;
import com.example.demo.utils.db.SqlHelper;

//终端
public class Device {
    public static String sql_insert_info = "insert into fy_device_setting values(?,?,?,?,?)";
    public static String sql_update_info = "update fy_device_setting set track=?,daily=?,timer=?,mode=? where childid = ?";
    public static String sql_select_info = "select * from fy_device_setting where childid = ?";

    public static void insert(Object[] pams){
        SqlHelper.executeUpdate(sql_insert_info, pams);
    }

    public static void update(Object[] pams){
        SqlHelper.executeUpdate(sql_update_info, pams);
    }

    public static DeviceSettingInfoBean getItem(int childid){
        Object[] pams = {
                childid,
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_info, pams);
        DeviceSettingInfoBean info = null;
        if(dr != null) {
            info = new DeviceSettingInfoBean();
            info.childid = dr.getInt(0);
            info.track = dr.getInt(1);
            info.daily = dr.getInt(2);
            info.timer = dr.getInt(3);
            info.mode = dr.getInt(4);
        }
        return info;
    }
}
