package com.example.demo.bean;

import java.util.List;

public class StoryInfoBean {
    public class LikeInfoBean{
        public int uid;
        public String name;
    }
//    public class CommentInfoBean{
//        public int id;
//        public int storyid;
//        public int uid;
//        public String name;
//        public int type;
//        public int touid; //回复的人
//        public String toname; //回复的人的名字
//        public String content;
//        public String create_time;
//    }
    public int id;
    public int uid;
    public String nickname;
    public String headimage;
    public String content;
    public String images;
    public List<LikeInfoBean> likes;
    public List<CommentInfoBean> comment;
    public int share;
    //public String comment;
    public String create_time;
}
