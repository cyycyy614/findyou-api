package com.example.demo.controller;

import com.example.demo.bean.*;
import com.example.demo.service.Common;
import com.example.demo.utils.JPushUtils;
import com.example.demo.utils.TextUtils;
import com.example.demo.utils.http.HttpResult;
import com.example.demo.utils.json.JObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller("api/common")
public class CommonController {
    //帮助列表
    @ResponseBody
    @RequestMapping(value = "api/common/help", method = RequestMethod.POST)
    public String getHelpList(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int pid = jo.getInt("pid");
        int pageIndex = jo.getInt("pageIndex");
        List<HelpInfoBean> list = Common.getHelpItems(pid, pageIndex);
        HttpResult ret = new HttpResult(list);
        return ret.toString();
    }

    //帮助Item
    @ResponseBody
    @RequestMapping(value = "api/common/help/getItem", method = RequestMethod.POST)
    public String getHelpItem(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        HelpInfoBean item = Common.getHelpItem(id);
        HttpResult ret = new HttpResult(item);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/common/help/search", method = RequestMethod.POST)
    public String searchHelpItem(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String keyword = jo.getString("keyword");
        List<HelpInfoBean> infos = Common.searchHelpItems(keyword);
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    //App信息
    @ResponseBody
    @RequestMapping(value = "api/common/getAppInfo", method = RequestMethod.POST)
    public String getAppInfo(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String packageName = jo.getString("packageName");
        String channel = jo.getString("channel");
        AppInfoBean item = Common.getAppInfo(packageName);
        if(item != null){
            item.channel = Common.getChannelInfo(item.id, channel);
        }
        HttpResult ret = new HttpResult(item);
        return ret.toString();
    }

    //消息
    @ResponseBody
    @RequestMapping(value = "api/common/msg", method = RequestMethod.POST)
    public String getMessage(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int type = jo.getInt("type");
        int pageIndex = jo.getInt("pageIndex");
        List<MsgInfoBean> infos = Common.getMsgItems(uid, type, pageIndex);
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    //广告图片展示
    @ResponseBody
    @RequestMapping(value = "api/common/banner", method = RequestMethod.POST)
    public String getBanners(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int type = jo.getInt("type");
        int count = jo.getInt("count");
        List<BannerInfoBean> infos = Common.getBanners(type, count);
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    //新闻列表
    @ResponseBody
    @RequestMapping(value = "api/common/news", method = RequestMethod.POST)
    public String getNewsItems(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int type = jo.getInt("type"); //预留
        int pageIndex = jo.getInt("pageIndex");
        List<NewsInfoBean> infos = Common.getNewsItems();
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    //单个新闻内容
    @ResponseBody
    @RequestMapping(value = "api/common/news/getItem", method = RequestMethod.POST)
    public String getNewsItem(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        NewsInfoBean info = Common.getNewsItem(id);
        HttpResult ret = new HttpResult(info);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/common/story", method = RequestMethod.POST)
    public String getStoryItems(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int type = jo.getInt("type");
        int pageIndex = jo.getInt("pageIndex");
        List<StoryInfoBean> infos = Common.getStoryItems(type, pageIndex);
        HttpResult ret = new HttpResult(infos);
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/common/story/add", method = RequestMethod.POST)
    public String addStoryItem(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int type = jo.getInt("type");
        String content = jo.getString("content");
        String images = jo.getString("images");
        Common.insertStory(uid, type, content, images);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/common/story/like", method = RequestMethod.POST)
    public String likeStoryItem(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        String like = jo.getString("like");
        Common.likeStoryItem(id, like);
        HttpResult ret = new HttpResult();
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/common/story/comment", method = RequestMethod.POST)
    public String commentStoryItem(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        int id = jo.getInt("id");
        String comment = jo.getString("comment");
        HttpResult ret = new HttpResult();
        Common.commentStoryItem(id, comment);
        return ret.toString();
    }

//    @ResponseBody
//    @RequestMapping(value = "api/common/story/comment", method = RequestMethod.POST)
//    public String getStoryCommentItems(@RequestBody String json) {
//        JObject jo = new JObject(json);
//        int uid = jo.getInt("uid");
//        int id = jo.getInt("id");
//        String comment = jo.getString("comment");
//        List<CommentInfoBean> infos = Common.getStoryCommentItems(id);
//        HttpResult ret = new HttpResult(infos);
//        return ret.toString();
//    }

    @ResponseBody
    @RequestMapping(value = "api/common/jpush/sendAll", method = RequestMethod.POST)
    public String jpushSendAll(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String touid = jo.getString("touid");
        String title = jo.getString("title");
        String content = jo.getString("content");
        int code = JPushUtils.sendAll(title, content);
        HttpResult ret = new HttpResult();
        if(code == -1){
            ret.code = 1000;
            ret.message = "极光推送失败!";
        }
        return ret.toString();
    }

    @ResponseBody
    @RequestMapping(value = "api/common/jpush/sendUser", method = RequestMethod.POST)
    public String jpushSendUser(@RequestBody String json) {
        JObject jo = new JObject(json);
        int uid = jo.getInt("uid");
        String jpushid = jo.getString("jpushid");
        String title = jo.getString("title");
        String content = jo.getString("content");
        List<String> aliaslist = new ArrayList<>();
        aliaslist.add(jpushid);
        int code = JPushUtils.sendUser(aliaslist, title, content);
        HttpResult ret = new HttpResult();
        if(code == -1){
            ret.code = 1000;
            ret.message = "极光推送失败!";
        }
        return ret.toString();
    }



}
