package com.example.demo.service;

import com.example.demo.bean.*;
import com.example.demo.utils.db.DataRow;
import com.example.demo.utils.db.SqlHelper;
import com.example.demo.utils.json.JsonUtils;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Common {
    private static String sql_select_help_info = "select id,pid,title from fy_help order by seq";
    private static String sql_select_help_item_info = "select title,content from fy_help where id = ?";
    private static String sql_select_app_info = "select id,versionCode,versionName,description from fy_app where packageName = ?";
    private static String sql_select_channel_info = "select name,url,urlType,status,isupdate from fy_channel where appid = ? and code = ?";
    private static String sql_select_msg_infos = "select id, title, msg, create_time from fy_msg where uid = ? and type = ?";
    private static String sql_select_search_help_infos = "select id,pid,title from fy_help where title like '%' ? '%'";
    //广告图片
    private static String sql_select_ads_infos = "select name, image, url from fy_ads where type = ? and isvalid = true order by id desc limit 0,?";
    //新闻
    private static String sql_select_news_infos = "select * from fy_news where type = 0 order by id desc";
    private static String sql_select_news_info = "select * from fy_news where id = ?";
    //故事
    private static String sql_insert_story_info = "insert into fy_story(uid, type, content, images, `like`, comment) values(?,?,?,?,'','')";
    private static String sql_select_story_infos = "select s.*, u.nickname,u.headimage from fy_story s join fy_userex u on s.uid = u.uid where s.type = ? order by s.id desc limit ?, 30";
    private static String sql_update_story_info_like = "update fy_story set `like` = ? where id = ?";
    private static String sql_update_story_info_comment = "update fy_story set `comment` = ? where id = ?";
    //故事评论
    private static String sql_insert_story_comment_info = "insert into fy_story_comment values(?,?,?,?,?,?,?,?,?)";
    private static String sql_select_story_comment_infos = "select * from fy_story_comment where storyid = ?";

    public static List<HelpInfoBean> getHelpItems(int pid, int pageIndex){
        Object[] pams = {
                pid,
                pageIndex
        };
        List<HelpInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_help_info, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                HelpInfoBean item = new HelpInfoBean();
                item.id = list.get(i).getInt(0);
                item.pid = list.get(i).getInt(1);
                item.title = list.get(i).getString(2);
                infos.add(item);
            }
        }
        return infos;
    }

    public static List<HelpInfoBean> searchHelpItems(String keyword){
        Object[] pams = {
                keyword
        };
        List<HelpInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_search_help_infos, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                HelpInfoBean item = new HelpInfoBean();
                item.id = list.get(i).getInt(0);
                item.pid = list.get(i).getInt(1);
                item.title = list.get(i).getString(2);
                infos.add(item);
            }
        }
        return infos;
    }

    public static HelpInfoBean getHelpItem(int id){
        Object[] pams = {
                id
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_help_item_info, pams);
        HelpInfoBean item = null;
        if(dr != null) {
            item = new HelpInfoBean();
            item.title = dr.getString(0);
            item.content = dr.getString(1);
        }
        return item;
    }

    public static AppInfoBean getAppInfo(String packageName){
        Object[] pams = {
                packageName
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_app_info, pams);
        AppInfoBean item = null;
        if(dr != null) {
            item = new AppInfoBean();
            item.id = dr.getInt(0);
            item.versionCode = dr.getInt(1);
            item.versionName = dr.getString(2);
            item.description = dr.getString(3);
        }
        return item;
    }

    public static ChannelInfoBean getChannelInfo(int appid, String code){
        Object[] pams = {
                appid,
                code
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_channel_info, pams);
        ChannelInfoBean item = null;
        if(dr != null) {
            item = new ChannelInfoBean();
            item.name = dr.getString(0);
            item.url = dr.getString(1);
            item.urlType = dr.getInt(2);
            item.status = dr.getInt(3);
            item.update = dr.getInt(4);
        }
        return item;
    }

    public static List<MsgInfoBean> getMsgItems(int uid, int type, int pageIndex){
        Object[] pams = {
                uid,
                type,
        };
        List<MsgInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_msg_infos, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                MsgInfoBean item = new MsgInfoBean();
                item.id = list.get(i).getInt(0);
                item.title = list.get(i).getString(1);
                item.msg = list.get(i).getString(2);
                item.create_time = list.get(i).getString(3);
                infos.add(item);
            }
        }
        return infos;
    }

    public static List<BannerInfoBean> getBanners(int type, int count){
        Object[] pams = {
                type,
                count
        };
        List<BannerInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_ads_infos, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                BannerInfoBean item = new BannerInfoBean();
                item.name = list.get(i).getString(0);
                item.image = list.get(i).getString(1);
                item.url = list.get(i).getString(2);
                infos.add(item);
            }
        }
        return infos;
    }

    public static List<NewsInfoBean> getNewsItems(){
        Object[] pams = {
        };
        List<NewsInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_news_infos, null);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                NewsInfoBean item = new NewsInfoBean();
                item.id = list.get(i).getInt(0);
                //item.type = list.get(i).getInt(1);
                item.title = list.get(i).getString(2);
                item.image = list.get(i).getString(3);
                item.create_time = list.get(i).getString(4);
                infos.add(item);
            }
        }
        return infos;
    }

    public static NewsInfoBean getNewsItem(int id){
        Object[] pams = {
                id
        };
        DataRow dr = SqlHelper.executeQuery(sql_select_news_info, pams);
        NewsInfoBean item = null;
        if(dr != null) {
            item = new NewsInfoBean();
            item.id = dr.getInt(0);
            item.type = dr.getInt(1);
            item.title = dr.getString(2);
            item.image = dr.getString(3);
            item.create_time = dr.getString(4);
            item.content = dr.getString(5);
        }
        return item;
    }

    public static void insertStory(int uid, int type, String content, String images){
        Object[] pams = {
                uid,
                type,
                content,
                images
        };
        SqlHelper.executeUpdate(sql_insert_story_info, pams);
    }

    public static void likeStoryItem(int id, String like){
        Object[] pams = {
                like,
                id
        };
        SqlHelper.executeUpdate(sql_update_story_info_like, pams);
    }

    public static void commentStoryItem(int id, String comment){
        Object[] pams = {
                comment,
                id
        };
        SqlHelper.executeUpdate(sql_update_story_info_comment, pams);
    }

    public static void insertStoryCommentItem(int storyid, int uid, int type, int touid, String msg){
        Object[] pams = {
                0,
                storyid,
                uid,
                type,
                touid,
                msg,
                new Date(),
                "",
                ""
        };
        SqlHelper.executeUpdate(sql_insert_story_comment_info, pams);
    }

    public static List<StoryInfoBean> getStoryItems(int type, int pageIndex){
        int offset = pageIndex * 30;
        Object[] pams = {
                type,
                offset
        };
        List<StoryInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_story_infos, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                StoryInfoBean item = new StoryInfoBean();
                item.id = list.get(i).getInt(0);
                item.uid = list.get(i).getInt(1);
                //item.type = list.get(i).getInt(0);
                item.content = list.get(i).getString(3);
                item.images = list.get(i).getString(4);
                item.likeCount = list.get(i).getInt(5);
//                item.likes = JsonUtils.convert_array(list.get(i).getString(5), StoryInfoBean.LikeInfoBean.class);
                item.share = list.get(i).getInt(6);
//                item.commentCount = JsonUtils.convert_array(list.get(i).getString(7),CommentInfoBean.class);
                item.commentCount = list.get(i).getInt(7);
                item.create_time = list.get(i).getString(8);
                item.nickname = list.get(i).getString(10);
                item.headimage = list.get(i).getString(11);
                item.comment = getStoryCommentItems(item.id);
                infos.add(item);
            }
        }
        return infos;
    }

    public static List<CommentInfoBean> getStoryCommentItems(int storyid){
        Object[] pams = {
                storyid
        };
        List<CommentInfoBean> infos = null;
        List<DataRow> list = SqlHelper.executeQueryArray(sql_select_story_comment_infos, pams);
        if(list != null) {
            infos = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                CommentInfoBean item = new CommentInfoBean();
                item.id = list.get(i).getInt(0);
                item.storyid = list.get(i).getInt(1);
                item.uid = list.get(i).getInt(2);
                item.type = list.get(i).getInt(3);
                item.touid = list.get(i).getInt(4);
                item.content = list.get(i).getString(5);
                item.create_time = list.get(i).getString(6);
                if(item.uid > 0) {
                    item.name = User.getName(item.uid);
                }
                if(item.touid > 0) {
                    item.toname = User.getName(item.touid);
                }
                infos.add(item);
            }
        }
        return infos;
    }
}
