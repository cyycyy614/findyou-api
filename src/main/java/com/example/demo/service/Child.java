package com.example.demo.service;

import com.example.demo.bean.ChildInfoBean;
import com.example.demo.bean.PropertyInfoBean;
import com.example.demo.utils.db.DataRow;
import com.example.demo.utils.db.SqlHelper;

import java.util.ArrayList;
import java.util.List;

public class Child {
    private static String sql_insert_guardian_info = "insert into fy_child values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static String sql_select_info = "select * from fy_child where id = ?";
    private static String sql_select_infos = "select * from fy_child where uid = ?";
    private static String sql_delete_info = "delete from fy_child where id = ?";
    private static String sql_update_info = "update fy_child set uid=?,imei=?,headimage=?,name=?,category=?,sex=?,birthday=?,phone=?,address=?,health=?,looks=?,contact=?,contact_phone=?,create_time=? where id = ?";
    private static String sql_update_info_change = "update fy_child set uid=?,imei=?,headimage=?,name=?,category=?,sex=?,birthday=?,phone=?,address=?,health=?,looks=?,contact=?,contact_phone=?,create_time=?,deviceId=?,psk=? where id = ?";

    private static String sql_update_imei = "update fy_child set imei=?, deviceId=?, psk=?, address=? where id=?"; //更换终端
    private static String sql_select_count_not_imei = "select count(id) from fy_child where uid = ? and imei <> ''";
    private static String sql_select_count = "select count(id) from fy_child where uid = ?";
    private static String sql_get_main_guardian = "select name from fy_guardian where childid = ? and ismain = true";
    //得到属性
    private static String sql_get_property = "select type,num,name from fy_child_constant order by type, num";

    public static List<PropertyInfoBean> getProperty(){
        List<PropertyInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_get_property, null);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                DataRow dr = list.get(i);
                PropertyInfoBean info = new PropertyInfoBean();
                info.type = dr.getInt(0);
                info.num = dr.getInt(1);
                info.name = dr.getString(2);
                infos.add(info);
            }
        }
        return infos;
    }

    public static int insert(Object[] pams){
        return SqlHelper.executeUpdate(sql_insert_guardian_info, pams);
    }

    public static void update(Object[] pams, boolean isChangeImei){
        if(isChangeImei){
            SqlHelper.executeUpdate(sql_update_info_change, pams);
        }else {
            SqlHelper.executeUpdate(sql_update_info, pams);
        }
    }

    public static void delete(int id){
        Object[] pams = {
                id
        };
        SqlHelper.executeUpdate(sql_delete_info, pams);
    }

    public static ChildInfoBean getItem(int id){
        ChildInfoBean info = null;
        Object[] pams = {
                id
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_info, pams);
        if(dr != null) {
            info = new ChildInfoBean();
            for (int i = 0; i < dr.size(); i++) {
                info.id = dr.getInt(0);
                //info.uid = dr.getInt(1);
                info.imei = dr.getString(2);
                info.headimage = dr.getString(3);
                info.name = dr.getString(4);
                info.category = dr.getInt(5);
                info.sex = dr.getInt(6);
                info.birthday = dr.getString(7);
                info.phone = dr.getString(8);
                info.address = dr.getString(9);
                info.health = dr.getInt(10);
                info.looks = dr.getInt(11);
                info.contact = dr.getString(12);
                info.contact_phone = dr.getString(13);
                info.create_time = dr.getString(14);
                info.deviceId = dr.getString(15);
                info.psk = dr.getString(16);
                info.device_address = dr.getString(17);
            }
        }
        return info;
    }

    public static List<ChildInfoBean> getItems(int uid, int pageIndex){
        Object[] pams = {
                uid
        };
        List<ChildInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_infos, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                DataRow dr = list.get(i);
                ChildInfoBean info = new ChildInfoBean();
                info.id = dr.getInt(0);
                //info.uid = dr.getInt(1);
                info.imei = dr.getString(2);
                info.headimage = dr.getString(3);
                info.name = dr.getString(4);
                info.category = dr.getInt(5);
                info.sex = dr.getInt(6);
                info.birthday = dr.getString(7);
                info.phone = dr.getString(8);
                info.address = dr.getString(9);
                info.health = dr.getInt(10);
                info.looks = dr.getInt(11);
                info.contact = dr.getString(12);
                info.contact_phone = dr.getString(13);
                info.create_time = dr.getString(14);
                info.deviceId = dr.getString(15);
                info.psk = dr.getString(16);
                info.device_address = dr.getString(17);
                info.guardian = getMainGuardian(info.id);
                infos.add(info);
            }
        }
        return infos;
    }

    public static String getMainGuardian(int childid){
        ChildInfoBean info = null;
        Object[] pams = {
                childid
        };
        DataRow dr = SqlHelper.executeQuery(sql_get_main_guardian, pams);
        String name = "";
        if(dr != null) {
            for (int i = 0; i < dr.size(); i++) {
                name = dr.getString(0);
            }
        }
        return name;
    }

    public static void changeImei(int id, String imei, String deviceId, String psk, String address){
        Object[] pams = {
                imei,
                deviceId,
                psk,
                address,
                id
        };
        SqlHelper.executeUpdate(sql_update_imei, pams);
    }

    public static int isExistBindDevice(int uid){
        Object[] pams = {
                uid
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_count_not_imei, pams);
        int count = 0;
        if(dr != null) {
            count = dr.getInt(0);
        }
        return count;
    }

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


}
