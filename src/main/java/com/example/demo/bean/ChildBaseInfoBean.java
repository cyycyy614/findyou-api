package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class ChildBaseInfoBean {
    public class ItemInfoBean{
        int type;
        int num;
        String name;
    }
//    public class CategoryInfo{
//        int seq;
//        String name;
//    }
//    public class HealthInfo{
//        int seq;
//        String name;
//    }
//    public class LooksInfo{
//        int seq;
//        String name;
//    }

    List<ItemInfoBean> sex;
    List<ItemInfoBean> category;
    List<ItemInfoBean> health;
    List<ItemInfoBean> looks;
    public ChildBaseInfoBean(){
        sex = new ArrayList<>();
        category = new ArrayList<>();
        health = new ArrayList<>();
        looks = new ArrayList<>();
    }

    public void setData(List<ItemInfoBean> data){
        for(int i=0; i<data.size(); i++) {
            ItemInfoBean item = data.get(i);
            if (item.type == 1) {
                //性别
                sex.add(item);
            } else if (item.type == 2) {
                //类别
                category.add(item);
            }else if (item.type == 3) {
                //健康
                health.add(item);
            }else if (item.type == 4) {
                //体态特征
                looks.add(item);
            }
        }
    }
}
