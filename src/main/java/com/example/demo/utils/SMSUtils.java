package com.example.demo.utils;

import com.example.demo.bean.SmsCodeInfoBean;
import com.example.demo.tools.HttpUtil;
import com.example.demo.tools.StreamClosedHttpResponse;
import com.example.demo.utils.json.JsonUtils;
import org.apache.http.HttpResponse;

import java.net.URLEncoder;

public class SMSUtils {
    private static String sms_key = "5611035ae0a3fd2eecd9e0db06d8530a";
    private static String sms_code_url =  "http://v.juhe.cn/sms/send?mobile=%s&tpl_id=%d&tpl_value=%s&dtype=json&key=%s";

    public static void sendChangeImei(String mobile, String name, String imei){
        int tpl_id = 120186;
        String tpl = "#child#=%s&#imei#=%s";
        tpl = String.format(tpl, name, imei);
        String tpl_value = URLEncoder.encode(tpl);
        String url = String.format(sms_code_url, mobile, tpl_id, tpl_value, sms_key);
        HttpResponse res = HttpUtil.doGet(url, null);
        String str = ((StreamClosedHttpResponse) res).getContent();
        SmsCodeInfoBean bean = JsonUtils.convert(str, SmsCodeInfoBean.class);
    }

    public static void sendUnbind(String mobile, String name){
        int tpl_id = 120187;
        String tpl = "#child#=%s";
        tpl = String.format(tpl, name);
        String tpl_value = URLEncoder.encode(tpl);
        String url = String.format(sms_code_url, mobile, tpl_id, tpl_value, sms_key);
        HttpResponse res = HttpUtil.doGet(url, null);
        String str = ((StreamClosedHttpResponse) res).getContent();
        SmsCodeInfoBean bean = JsonUtils.convert(str, SmsCodeInfoBean.class);
    }
}
