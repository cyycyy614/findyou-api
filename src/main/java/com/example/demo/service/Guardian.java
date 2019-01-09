package com.example.demo.service;

import com.example.demo.bean.ChildInfoBean;
import com.example.demo.bean.GuardianInfoBean;
import com.example.demo.utils.db.DataRow;
import com.example.demo.utils.db.SqlHelper;

import java.util.ArrayList;
import java.util.List;

public class Guardian {
    private static String sql_insert_guardian_info = "insert into fy_guardian values(?,?,?,?,?,?,?,?)";
    private static String sql_select_infos = "select g.*,u.username from fy_guardian g join fy_users u on g.uid = u.uid where g.childid = ? order by g.status desc";
    private static String sql_update_info = "update fy_guardian set name=?,phone=?,create_time=?,status=? where id=?";
    private static String sql_update_clear_main = "update fy_guardian set ismain=false where childid=?;";
    private static String sql_update_set_main = "update fy_guardian set ismain=true where id=?";
    //private static String sql_update_unbind = "update fy_guardian set status=0 where childid=?";
    //private static String sql_update_unbind = "delete from fy_child where childid=?"; //解绑

    private static String sql_select_count = "select count(*) from fy_guardian where uid = ?";
    private static String sql_select_count_main = "select count(*) from fy_guardian where uid = ? and ismain = true";
    private static String sql_select_count_by_childid = "select count(*) from fy_guardian where childid = ?";

    public static void insert(Object[] pams){
        SqlHelper.executeUpdate(sql_insert_guardian_info, pams);
    }

    public static List<GuardianInfoBean> getItems(int childid, int pageIndex){
        Object[] pams = {
                childid
        };
        List<GuardianInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_infos, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                DataRow dr = list.get(i);
                GuardianInfoBean info = new GuardianInfoBean();
                info.id = dr.getInt(0);
                info.uid = dr.getInt(1);
                info.childid = dr.getInt(2);
                info.name = dr.getString(3);
                info.phone = dr.getString(4);
                info.status = dr.getInt(5);
                info.auth_time = dr.getString(6);
                info.ismain = dr.getBoolean(7);
                info.username = dr.getString(8);
                infos.add(info);
            }
        }
        return infos;
    }

    public static void update(Object[] pams){
        SqlHelper.executeUpdate(sql_update_info, pams);
    }

    public static void clearMain(int childid){
        Object[] pams = {
                childid,
        };
        SqlHelper.executeUpdate(sql_update_clear_main, pams);
    }
    public static void setMain(int childid, int id){
        clearMain(childid);
        Object[] pams = {
                id,
        };
        SqlHelper.executeUpdate(sql_update_set_main, pams);
    }

//    public static void unbind(int childid){
//        //clearMain(childid);
//        Object[] pams = {
//                childid,
//        };
//        SqlHelper.executeUpdate(sql_update_unbind, pams);
//    }

    public static int getCount(int uid){
        Object[] pams = {
                uid
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_count, pams);
        int count = 0;
        if(dr != null) {
            count = dr.getInt(0);
        }
        return count;
    }

    public static int getGuardianCountByChildId(int childid){
        Object[] pams = {
                childid
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_count_by_childid, pams);
        int count = 0;
        if(dr != null) {
            count = dr.getInt(0);
        }
        return count;
    }

    public static int getCountMain(int uid){
        Object[] pams = {
                uid
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_count_main, pams);
        int count = 0;
        if(dr != null) {
            count = dr.getInt(0);
        }
        return count;
    }
}
