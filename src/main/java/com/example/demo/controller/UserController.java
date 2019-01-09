package com.example.demo.controller;

import com.example.demo.bean.SmsCodeInfoBean;
import com.example.demo.bean.UserInfoBean;
import com.example.demo.bean.UserInfoExBean;
import com.example.demo.service.Child;
import com.example.demo.service.Guardian;
import com.example.demo.service.Log;
import com.example.demo.service.User;
import com.example.demo.tools.HttpUtil;
import com.example.demo.tools.StreamClosedHttpResponse;
import com.example.demo.utils.SMSUtils;
import com.example.demo.utils.TextUtils;
import com.example.demo.utils.db.SqlHelper;
import com.example.demo.utils.http.HttpResult;
import com.example.demo.utils.json.JObject;
import com.example.demo.utils.json.JsonUtils;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.util.calendar.Gregorian;

import java.net.URLEncoder;
import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller("api/user")
public class UserController {
    private static String sms_key = "5611035ae0a3fd2eecd9e0db06d8530a";
    private static String sms_code_url =  "http://v.juhe.cn/sms/send?mobile=%s&tpl_id=44482&tpl_value=%s&dtype=json&key=%s";

    //登录
    @ResponseBody
    @RequestMapping(value = "api/user/test", method = RequestMethod.POST)
    public String test(@RequestBody String json){
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    //登录
    @ResponseBody
    @RequestMapping(value = "api/user/login", method = RequestMethod.POST)
    public String login(@RequestBody String json){
        JObject jo = new JObject(json);
        String username = jo.getString("username");
        String password = jo.getString("password");
        String loginType = jo.getString("loginType");

        HttpResult ret = new HttpResult();
        System.out.println("login 0");
        if(TextUtils.isEmpty(username)){
            ret.code = 1000;
            ret.message = "用户名为空!";
            return ret.toString();
        }
        System.out.println("login 1");
        if(TextUtils.isEmpty(username)){
            ret.code = 1001;
            ret.message = "密码为空!";
            return ret.toString();
        }
        System.out.println("login 2");
        if(TextUtils.isEmpty(loginType)){
            ret.code = 1002;
            ret.message = "登录类型为空!";
            return ret.toString();
        }
        System.out.println("login 3");
        User item = new User();
        System.out.println("login 4");
        UserInfoBean info = item.getUserInfoByName(username);
        System.out.println("login 5");
        if(info == null){
            ret.code = 1003;
            ret.message = "没有此用户.";
            return ret.toString();
        }
        if(!info.password.equals(password)){
            ret.code = 1004;
            ret.message = "输入的密码有误!";
            return ret.toString();
        }
        UserInfoExBean infoex = item.getUserInfoEx(info.uid);
        if(infoex == null){
            item.insert(info.uid);
            infoex = new UserInfoExBean();
        }
        infoex.uid = info.uid;
        if(TextUtils.isEmpty(infoex.nickname)){
            infoex.nickname = info.username;
        }
        infoex.child = Child.getCount(info.uid);
        infoex.guardian = Guardian.getCount(info.uid);
        infoex.main_guar = Guardian.getCountMain(info.uid);

        //记录用户登录信息
        item.login_tj(info.uid);
        //infoex.uid = info.uid;
        ret.data = JsonUtils.toString(infoex);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/user/getUserInfo", method = RequestMethod.POST)
    public String getUserInfo(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        User item = new User();
        UserInfoExBean info = item.getUserInfoEx(uid);
        if(info != null) {
            info.child = Child.getCount(info.uid);
            info.guardian = Guardian.getCount(info.uid);
            info.main_guar = Guardian.getCountMain(info.uid);
        }
        HttpResult ret = new HttpResult(info);
        return ret.toString();
    }

    //注册
    @ResponseBody
    @RequestMapping(value = "api/user/register", method = RequestMethod.POST)
    public String register(@RequestBody String json){
        //Log.insert(json);
        JObject jo = new JObject(json);
        String username = jo.getString("username");
        String password = jo.getString("password");

        HttpResult ret = new HttpResult();
        if(TextUtils.isEmpty(username)){
            ret.code = 1000;
            ret.message = "用户名为空!";
            return ret.toString();
        }
        if(TextUtils.isEmpty(password)){
            ret.code = 1001;
            ret.message = "密码为空!";
            return ret.toString();
        }
        User item = new User();
        int count = item.isUserExist(username);
        if(count == 0) {
            int result = item.insert(username, password);
            if(result == -1){
                ret.code = 0;
                ret.message = "system exception:register->insert.";
                return ret.toString();
            }
            UserInfoBean info = item.getUserInfoByName(username);
            if(info != null){
                item.insert(info.uid);
            }
        }else if(count > 0){
            ret.code = 1002;
            ret.message = "用户名已存在!";
        }else {
            ret.code = 0;
            ret.message = "system exception:register->isUserExist.";
        }
        return ret.toString();
    }

    //发送验证码
    public static String createRandom(int num){
        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
        for (int i = 0; i < num; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            //MToast.showToast("手机号应为11位数");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            //LogUtil.e(isMatch);
            if (!isMatch) {
                //MToast.showToast("请填入正确的手机号");
            }
            return isMatch;
        }
    }

    @ResponseBody
    @RequestMapping(value = "api/user/sendAuthCode", method = RequestMethod.POST)
    public String sendAuthCode(@RequestBody String json){
        JObject jo = new JObject(json);
        String username = jo.getString("username");
        int type = jo.getInt("type");
        HttpResult ret = new HttpResult();
        if(!isPhone(username)){
            ret.code = 1003;
            ret.message = "无效的手机号!";
            return ret.toString();
        }
        UserInfoBean info = null;
        if(type == 1){
            //找回密码
            User item = new User();
            info = item.getUserInfoByName(username);
            if(info == null){
                ret.code = 1002;
                ret.message = "此用户不存在!";
                return ret.toString();
            }
        }
        String sms_code = createRandom(6);
        String code = "#code#=" + sms_code;
        code = URLEncoder.encode(code);
        String url = String.format(sms_code_url, username, code, sms_key);
        HttpResponse res = HttpUtil.doGet(url, null);
        String str = ((StreamClosedHttpResponse) res).getContent();
        SmsCodeInfoBean bean = JsonUtils.convert(str, SmsCodeInfoBean.class);
        if(bean == null){
            ret.code = 1000;
            ret.message = "获取验证码失败!";
            return ret.toString();
        }
        if(bean.error_code != 0){
            ret.code = 1001;
            ret.message = bean.reason;
            return ret.toString();
        }
        if(type == 0){
            //注册
            ret.data = sms_code;
        }else if(type == 1){
            //找回密码
//            User item = new User();
//            UserInfoBean info = item.getUserInfoByName(username);
//            if(info == null){
//                ret.code = 1002;
//                ret.message = "此用户不存在!";
//                return ret.toString();
//            }
            JsonObject js = new JsonObject();
            js.addProperty("uid", info.uid);
            js.addProperty("code", sms_code);
            ret.data = js.toString();
        }
        return ret.toString();
    }

    //重置密码
    @ResponseBody
    @RequestMapping(value = "api/user/testSms", method = RequestMethod.POST)
    public String testSms(@RequestBody String json){
        JObject jo = new JObject(json);
        String mobile = jo.getString("username");
        String child = jo.getString("child");
        String imei = jo.getString("imei");
        //SMSUtils.sendChangeImei(mobile, child, imei);
        SMSUtils.sendUnbind(mobile, child);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    //重置密码
    @ResponseBody
    @RequestMapping(value = "api/user/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String username = jo.getString("username");
        String password = jo.getString("password");
        User item = new User();
        if(uid > 0){
            item.changePassword(uid, password);
        }else if(!TextUtils.isEmpty(username)){
            item.resetPassword(username, password);
        }
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    //用户中心修改密码
    @ResponseBody
    @RequestMapping(value = "api/user/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String password = jo.getString("password");
        User item = new User();
        item.changePassword(uid, password);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    //更新用户资料
    @ResponseBody
    @RequestMapping(value = "api/user/updateNickname", method = RequestMethod.POST)
    public void updateNickname(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String nickname = jo.getString("value");
        User item = new User();
        item.updateNickname(uid, nickname);
    }

    @ResponseBody
    @RequestMapping(value = "api/user/updateSex", method = RequestMethod.POST)
    public void updateSex(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int sex = jo.getInt("value");
        User item = new User();
        item.updateSex(uid, sex);
    }

    @ResponseBody
    @RequestMapping(value = "api/user/updateHeadImage", method = RequestMethod.POST)
    public void updateHeadImage(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String headimage = jo.getString("value");
        User item = new User();
        item.updateHeadImage(uid, headimage);
    }

    @ResponseBody
    @RequestMapping(value = "api/user/updateSign", method = RequestMethod.POST)
    public void updateSign(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String sign = jo.getString("value");
        User item = new User();
        item.updateSign(uid, sign);
    }

    @ResponseBody
    @RequestMapping(value = "api/user/updateBirthday", method = RequestMethod.POST)
    public void updateBirthday(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String birthday = jo.getString("value");
        User item = new User();
        item.updateBirthday(uid, birthday);
    }

    @ResponseBody
    @RequestMapping(value = "api/user/updateUserInfo", method = RequestMethod.POST)
    public String updateUserInfo(@RequestBody String json){
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String nickname = jo.getString("nickname");
        String headimage = jo.getString("headimage");
        String phone = jo.getString("phone");
        String email = jo.getString("email");
        String birthday = jo.getString("birthday");
        int sex = jo.getInt("sex");

        User item = new User();
        item.updateUserInfo(uid, nickname, headimage, birthday, sex, phone, email);
        //返回信息
        UserInfoExBean info = new UserInfoExBean();
        info.uid = uid;
        info.nickname = nickname;
        info.headimage = headimage;
        info.birthday = birthday;
        info.sex = sex;
        info.phone = phone;
        info.email = email;
        HttpResult ret = new HttpResult(info);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/user/test1", method = RequestMethod.POST)
    public String test1(){
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://rm-m5ecft55499td54wbgo.mysql.rds.aliyuncs.com:3306/findyou";
        //MySQL配置时的用户名
        String user = "findyou";
        //MySQL配置时的密码
        String password = "Aa123456";
        System.out.println("init 1");
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            System.out.println("init 2");
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            System.out.println("init 3");
            //要执行的SQL语句
            String sql = "select * from fy_users";
            System.out.println("init 4");
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("init 5");
            System.out.println("-----------------");
            System.out.println("执行结果如下所示:");
            System.out.println("-----------------");
            System.out.println("姓名" + "\t" + "职称");
            System.out.println("-----------------");

            String userPassword = null;
            String username = null;
            while(rs.next()){
                //获取stuname这列数据
                userPassword = rs.getString("password");
                //获取stuid这列数据
                username = rs.getString("username");

                //输出结果
                System.out.println(username + "\t" + userPassword);
            }
            rs.close();
            con.close();
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            System.out.println("数据库数据成功获取！！");
        }
        return "ok";
    }

//    @ResponseBody
//    @RequestMapping(value = "api/user/test2", method = RequestMethod.POST)
//    public String test2(){
//        Object[] pams = {
//                1
//        };
//
//    }
}
