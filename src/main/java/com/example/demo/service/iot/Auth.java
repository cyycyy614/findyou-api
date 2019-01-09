package com.example.demo.service.iot;

import com.example.demo.tools.app.AppTask;

public class Auth {
    private static AppTask myApp;
    public static boolean login(){
        String strIP = "180.101.147.89";
        String strPort = "8743";
        String strAppId = "lTz5H5De9zsnTkeYURkD9A8LM78a";
        String strPassword = "2X9NEhWwFYaJFkwOcUpLJR7HxVAa";
        myApp = null;
        myApp = createApp(strIP, strPort, strAppId, strPassword);
        boolean ret = false;
        try {
            ret = myApp.login();
        }catch (Exception e){
        }
        return ret;
    }

    private static AppTask createApp(String ip, String port, String appid, String password){
        // Create App Task
        try {
            if(myApp == null) {
                myApp = new AppTask(ip, port, appid, password);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return myApp;
    }

    public static AppTask getMyApp(){
        return myApp;
    }
}
