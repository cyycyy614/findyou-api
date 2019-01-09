package com.example.demo.service;

import com.example.demo.utils.db.SqlHelper;

public class Log {
    public static String sql_insert_info = "insert into fy_log values(?,?)";

    public static void insert(String msg){
        Object[] pams = {
                0,
                msg,
        };
        SqlHelper.executeUpdate(sql_insert_info, pams);
    }
}
