package com.example.demo.service;

import com.example.demo.bean.UserInfoBean;
import com.example.demo.bean.WarningInfoBean;
import com.example.demo.utils.db.DataRow;
import com.example.demo.utils.db.SqlHelper;

public class Warning {

    public static String sql_insert_info = "insert into fy_warning_setting values(?,?,?,?,?,?,?,?,?)";
    public static String sql_update_info = "update fy_warning_setting set isrange=?,isdistance=?,distance=?,istime=?,time=?,ispower=?,power=? where childid = ?";
    public static String sql_select_info = "select * from fy_warning_setting where childid = ?";

    public static void insert(Object[] pams){
        SqlHelper.executeUpdate(sql_insert_info, pams);
    }

    public static void update(Object[] pams){
        SqlHelper.executeUpdate(sql_update_info, pams);
    }

    public static WarningInfoBean getItem(int childid){
        Object[] pams = {
                childid,
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_info, pams);
        WarningInfoBean info = null;
        if(dr != null) {
            info = new WarningInfoBean();
            info.childid = dr.getInt(0);
            info.isrange = dr.getBoolean(1);
            info.range = dr.getInt(2);
            info.isdistance = dr.getBoolean(3);
            info.distance = dr.getInt(4);
            info.istime = dr.getBoolean(5);
            info.time = dr.getInt(6);
            info.ispower = dr.getBoolean(7);
            info.power = dr.getInt(8);
        }
        return info;
    }

}
