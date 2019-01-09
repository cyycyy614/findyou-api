package com.example.demo.utils;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.example.demo.utils.jpush.MsgItem;
import com.example.demo.utils.json.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JPushUtils {

    public static String masterSecret = "68776adf53ca999a96ee91a2";
    public static String appKey = "e025fa149ac64aa97d1fab35";
    public  static JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

//    public void sendAll(String title, String content){
//        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
//
//    }

    public static PushPayload sendAll_android_ios(String title, String content) {
        MsgItem msg = new MsgItem();
        msg.title = title;
        msg.content = content;
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(content)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtra("message", JsonUtils.toString(msg)).build())
                        .build())
                .build();
    }

    public static int sendAll(String title, String content) {
        MsgItem msg = new MsgItem();
        msg.title = title;
        msg.content = content;
        int code = 0;
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())//设置接受的平台
                .setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.alert(JsonUtils.toString(msg)))
                .build();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        try{
            PushResult result = jpushClient.sendPush(payload);
            return code;
        }catch (Exception e){
            code = -1;
        }
        return code;
    }

    public static PushPayload buildPushObject_android_tag_alertWithTitle(String message) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.android(message, "JiangNan", null))
                .build();
    }


    public static int sendUser(List<String> registrationId, String title, String content){
        Map<String,String> extras = new HashMap<>();
        extras.put("title", title);
        extras.put("content", content);
        PushPayload payload = PushPayload
                .newBuilder()
                .setPlatform(Platform.all())
                //.setAudience(Audience.alias(aliasList))
                .setAudience(Audience.registrationId(registrationId))
                .setNotification(
                        Notification
                                .newBuilder()
                                .setAlert(title)
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder().addExtras(extras).build())
                                .addPlatformNotification(
                                        IosNotification.newBuilder().addExtras(extras).build())
                                .build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
        int code = 0;
        try{
            PushResult result = jpushClient.sendPush(payload);
            return code;
        }catch (Exception e){
            code = -1;
        }
        return code;
    }
}
