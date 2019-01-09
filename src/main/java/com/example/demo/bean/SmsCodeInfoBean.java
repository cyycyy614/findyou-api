package com.example.demo.bean;

public class SmsCodeInfoBean {
    public class SmsResult{
        public int fee;
        public int count;
        public String sid;
    }
    public int error_code;
    public String reason;
    public SmsResult result;
}
