package com.example.demo.service;

import com.example.demo.bean.UserInfoBean;
import com.example.demo.bean.UserInfoExBean;
import com.example.demo.utils.db.SqlHelper;
import com.example.demo.utils.db.DataRow;

import java.util.Date;

public class User {
    private static String sql_select_info = "select * from fy_users where uid = ?";
    private static String sql_insert_info = "insert into fy_users values(?,?,?,?,?,?,?)";
    private static String sql_insert_ex_info = "insert into fy_userex(uid) values(?)";
    private static String sql_select_ex_info = "select * from fy_userex where uid = ?";
    private static String sql_select_info_by_name = "select * from fy_users where username = ?";
    private static String sql_select_count = "select count(uid) from fy_users where username = ?";
    private static String sql_update_login_time = "update set lastLoginTime = ? from fy_userex where uid = ?";
    private static String sql_update_password = "update fy_users set password = ? where username = ?";
    private static String sql_update_password_by_uid = "update fy_users set password = ? where uid = ?";
    //昵称
    private static String sql_select_name_info = "select nickname from fy_userex where uid = ?";
    //登录统计
    private static String sql_insert_login_tj = "insert into fy_login_tj(uid) values(?)";

    public UserInfoBean getUserInfo(int uid){
        UserInfoBean info = null;
        Object[] pams = {
                uid
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_info, pams);
        if(dr != null) {
            info = new UserInfoBean();
            for (int i = 0; i < dr.size(); i++) {
                info.uid = dr.getInt(0);
            }
        }
        return info;
    }

    public static String getName(int uid){
        String name = "";
        Object[] pams = {
                uid
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_name_info, pams);
        if(dr != null) {
            name = dr.getString(0);
        }
        return name;
    }

    public UserInfoBean getUserInfoByName(String username){
        UserInfoBean info = null;
        Object[] pams = {
                username
        };
        System.out.println("getUserInfoByName 1");
        DataRow dr = SqlHelper.executeQuery(sql_select_info_by_name, pams);
        System.out.println("getUserInfoByName 2");
        if(dr != null) {
            info = new UserInfoBean();
            for (int i = 0; i < dr.size(); i++) {
                info.uid = dr.getInt(0);
                info.username = dr.getString(1);
                info.password = dr.getString(2);
            }
        }
        System.out.println("getUserInfoByName 3");
        return info;
    }

    public int isUserExist(String username){
        Object[] pams = {
                username
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_count, pams);
        int count = -1;
        if(dr != null) {
            count = dr.getInt(0);
        }
        return count;
    }

    public int insert(String username, String password){
        Object[] pams = {
                0,
                username,
                password,
                0,
                0,
                new Date(),
                new Date(),
        };
        return SqlHelper.executeUpdate(sql_insert_info, pams);
    }
    public int insert(int uid){
        Object[] pams = {
                uid
        };
        return SqlHelper.executeUpdate(sql_insert_ex_info, pams);
    }

    public UserInfoExBean getUserInfoEx(int uid){
        UserInfoExBean info = null;
        Object[] pams = {
                uid
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_ex_info, pams);
        if(dr != null) {
            info = new UserInfoExBean();
            for (int i = 0; i < dr.size(); i++) {
                info.uid = dr.getInt(0);
                info.nickname = dr.getString(1);
                info.headimage = dr.getString(2);
                info.birthday = dr.getString(3);
                info.sex = dr.getInt(4);
                info.phone = dr.getString(5);
                info.email = dr.getString(6);
            }
        }
        return info;
    }

    public void login_tj(int uid){
        Object[] pams = {
                uid
        };
        SqlHelper.executeUpdate(sql_insert_login_tj, pams);
    }

    public void updateLoginTime(){
        Object[] pams = {
                new Date(),
        };
    }

    public void resetPassword(String username, String password){
        Object[] pams = {
                password,
                username
        };
        SqlHelper.executeUpdate(sql_update_password, pams);
    }

    public void resetPassword(int uid, String password){
        Object[] pams = {
                password,
                uid
        };
        SqlHelper.executeUpdate(sql_update_password, pams);
    }

    public void changePassword(int uid, String password){
        Object[] pams = {
                password,
                uid
        };
        SqlHelper.executeUpdate(sql_update_password_by_uid, pams);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    private static String sql_update_nickname = "update fy_userex set nickname = ? where uid = ?";
    public void updateNickname(int uid, String nickname){
        Object[] pams = {
                nickname,
                uid
        };
        SqlHelper.executeUpdate(sql_update_nickname, pams);
    }

    private static String sql_update_sex = "update fy_userex set nickname = ? where uid = ?";
    public void updateSex(int uid, int sex){
        Object[] pams = {
                sex,
                uid
        };
        SqlHelper.executeUpdate(sql_update_sex, pams);
    }

    private static String sql_update_headimage = "update fy_userex set headimage = ? where uid = ?";
    public void updateHeadImage(int uid, String headimage){
        Object[] pams = {
                headimage,
                uid
        };
        SqlHelper.executeUpdate(sql_update_headimage, pams);
    }

    private static String sql_update_sign = "update fy_userex set sign = ? where uid = ?";
    public void updateSign(int uid, String sign){
        Object[] pams = {
                sign,
                uid
        };
        SqlHelper.executeUpdate(sql_update_sign, pams);
    }

    private static String sql_update_birthday = "update fy_userex set birthday = ? where uid = ?";
    public void updateBirthday(int uid, String birthday){
        Object[] pams = {
                birthday,
                uid
        };
        SqlHelper.executeUpdate(sql_update_birthday, pams);
    }

    private static String sql_update_user_info = "update fy_userex set nickname = ?, headimage = ?, birthday = ?, sex=?, phone=?, email=? where uid = ?";
    public void updateUserInfo(int uid, String nickname, String headimage, String birthday, int sex, String phone, String email){
        Object[] pams = {
                nickname,
                headimage,
                birthday,
                sex,
                phone,
                email,
                uid
        };
        SqlHelper.executeUpdate(sql_update_user_info, pams);
    }
}
