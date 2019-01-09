package com.example.demo.service;

import com.example.demo.bean.EfenceInfoBean;
import com.example.demo.bean.WarningInfoBean;
import com.example.demo.utils.db.DataRow;
import com.example.demo.utils.db.SqlHelper;

import java.util.ArrayList;
import java.util.List;

public class Efence {

    public static String sql_insert_info = "insert into fy_efence values(?,?,?,?,?,?,?,?)";
    public static String sql_update_info = "update fy_efence set name=?,address=?,radius=?,status=?,latitude=?,longitude=? where id = ?";
    public static String sql_select_info = "select * from fy_efence where id = ?";
    public static String sql_select_infos = "select * from fy_efence where childid = ? order by status desc";
    public static String sql_delete_info = "delete from fy_efence where id = ?";

    public static void insert(Object[] pams){
        SqlHelper.executeUpdate(sql_insert_info, pams);
    }

    public static void update(Object[] pams){
        SqlHelper.executeUpdate(sql_update_info, pams);
    }

    public static void delete(int id){
        Object[] pams = {
                id,
        };
        SqlHelper.executeUpdate(sql_update_info, pams);
    }

    public static EfenceInfoBean getItem(int id){
        Object[] pams = {
                id,
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_info, pams);
        EfenceInfoBean info = null;
        if(dr != null) {
            info = new EfenceInfoBean();
            info.id = dr.getInt(0);
            info.childid = dr.getInt(1);
            info.name = dr.getString(2);
            info.address = dr.getString(3);
            info.radius = dr.getInt(4);
            info.status = dr.getBoolean(5);
            info.latitude = dr.getDouble(6);
            info.longitude = dr.getDouble(7);
        }
        return info;
    }

    public static List<EfenceInfoBean> getItems(int childid, int pageIndex){
        Object[] pams = {
                childid,
        };
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_infos, pams);
        List<EfenceInfoBean> infos = null;
        if(list != null) {
            infos = new ArrayList<>();
            for(int i=0; i<list.size(); i++) {
                EfenceInfoBean info = new EfenceInfoBean();
                DataRow dr = list.get(i);
                info.id = dr.getInt(0);
                //info.childid = dr.getInt(1);
                info.name = dr.getString(2);
                info.address = dr.getString(3);
                info.radius = dr.getInt(4);
                info.status = dr.getBoolean(5);
                info.latitude = dr.getDouble(6);
                info.longitude = dr.getDouble(7);
                infos.add(info);
            }
        }
        return infos;
    }
}
