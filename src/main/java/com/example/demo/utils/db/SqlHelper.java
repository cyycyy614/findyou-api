package com.example.demo.utils.db;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.example.demo.service.Log;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class SqlHelper {

    //private static Connection conn;
    //使用PreparedStatment可以防止sql注入
    //private static PreparedStatement ps;
    //private static ResultSet rs;
    //private static CallableStatement cs;

    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    //加载驱动，只需要执行一次
    static {
        try {
//            driver = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://sw.joyvc.com:3306/findyou";
//            user = "root";
//            password = "joysw377136";

//            driver = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://j3hrjo5c.hkzzcdb.dnstoo.com:5809/findyou?autoReconnect";
//            user = "findyou_f";
//            password = "a123456";

            driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://www.joysw.com:3306/findyou";
            user = "root";
            password = "joysw377136";
            // 1 加载驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private static PreparedStatement prepareStatement(Connection conn, String sql, Object[] params){
        if(sql!=null && !sql.equals("")){
            System.out.println(sql);
        }
//        if(conn == null){
//            getConnection();
//        }
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(sql);
            //Log.insert("prepareStatement:" + ps + "--sql:" + sql);
            if(params==null){
                params = new Object[0];
            }
            for(int i=0;i<params.length;i++){
                ps.setObject(i+1,params[i]);
            }
            //ps.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ps;
    }

    public static Connection getConnection() {
        Connection conn = null;
        System.out.println("getConnection 1");
        try {
            // 2 获取数据库连接
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("getConnection 2");
        } catch (SQLException e) {
            System.out.println("getConnection 3");
            e.printStackTrace();
        }
        System.out.println("getConnection 4");
        return conn;
    }

    private static void close(Connection conn, PreparedStatement ps){
        try {
            if(ps != null){
                ps.close();
                ps = null;
            }
            if(conn != null){
                conn.close();
                conn = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int executeUpdate(String sql, Object[] params){
        Connection conn = getConnection();
        PreparedStatement ps = prepareStatement(conn, sql, params);
        int ret = 0;
        try {
            ret = ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
            ret = -1;
        }finally {
            close(conn, ps);
        }
        return ret;
    }

    public static List<DataRow> executeQueryArray(String sql, Object[] params) {
        System.out.println("executeQueryArray 1");
        ArrayList<DataRow> a = null;
        try {
            Connection conn = conn = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = conn.prepareStatement(sql);
            if(params==null){
                params = new Object[0];
            }
            for(int i=0;i<params.length;i++){
                ps.setObject(i+1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            a = new ArrayList();
            ResultSetMetaData rsmd = rs.getMetaData();
            int column = rsmd.getColumnCount();
            while (rs.next()) {
                //对象数组，存储一行数据
                Object[] objs = new Object[column];
                for (int i = 0; i < objs.length; i++) {
                    objs[i] = rs.getObject(i + 1);
                }
                DataRow dr = new DataRow(objs);
                a.add(dr);
            }
            rs.close();
            ps.close();
            conn.close();
            rs = null;
            ps = null;
            conn = null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }

    public static DataRow executeQuery(String sql, Object[] params) {
        List<DataRow> a = executeQueryArray(sql, params);
        if(a == null){
            return null;
        }
        DataRow dr = null;
        if(a.size() != 0){
            dr = a.get(0);
        }
        return dr;
    }
}
