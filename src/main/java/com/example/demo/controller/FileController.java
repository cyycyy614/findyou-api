package com.example.demo.controller;

import com.example.demo.service.User;
import com.example.demo.utils.TextUtils;
import com.example.demo.utils.http.HttpResult;
import com.example.demo.utils.json.JObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@Controller("api/file")
public class FileController {

//    @ResponseBody
//    @RequestMapping(value = "api/file/upload", method = RequestMethod.POST)
//    public String uploadFiles(@RequestParam("file") MultipartFile file) {
//        //JObject jo = new JObject(json);
//        //int uid = jo.getInt("uid");
//        if (!file.isEmpty()) {
//            try {
//                String fileName = file.getOriginalFilename();
//                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("F:\\files\\" + fileName)));
//                out.write(file.getBytes());
//                out.flush();
//                out.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return "上传失败," + e.getMessage();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "上传失败," + e.getMessage();
//            }
//            return "上传成功";
//        } else {
//            return "上传失败，因为文件是空的.";
//        }


//        //保存文件
//        //HttpResult ret = new HttpResult();
//        //return ret.toString();
//        //return "ok.";
//    }

//    String imagePath = "D:\\Tomcat 9.0\\webapps\\images\\";
//    String imageUrl = "http://192.168.0.105:8081/images/";
    String imagePath = "C:\\inetpub\\wwwroot\\images\\";
    String imageUrl = "http://sw.joyvc.com/images/";

    @ResponseBody
    @RequestMapping(value = "api/file/upload", method = RequestMethod.POST)
    public String uploadFiles(HttpServletRequest request) {
        String userid = request.getParameter("uid");
        String fileName = request.getParameter("fileName");
        int uid = 0;
        if(!TextUtils.isEmpty(userid)){
            uid = Integer.parseInt(userid);
        }
        HttpResult ret = new HttpResult();
        if(uid == 0){
            ret.code = 1000;
            ret.message = "未知用户!";
            return ret.toString();
        }
        //接收文件
        //MultipartHttpServletRequest multipartFiles = (MultipartHttpServletRequest)request.m;
//        String contentType = request.getContentType();
//        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
//            MultipartHttpServletRequest multipartRequest =
//                    WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
//            MultipartFile file = multipartRequest.getFile("file");
//            String str = "hello";
//        }
        //保存文件
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i =0; i< files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String name = file.getOriginalFilename();
                    String suffix = name.substring(name.lastIndexOf("."));
                    if(!TextUtils.isEmpty(fileName)){
                        fileName = fileName + suffix;
                    }
                    File savefile = new File(imagePath + fileName);
                    if(!savefile.exists()){
                        savefile.createNewFile();
                    }
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(savefile));
                    bufferedOutputStream.write(file.getBytes());
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    ret.data = imageUrl + fileName;
                } catch (Exception e) {
                    ret.code = 1001;
                    ret.message = "上传文件失败:" + e.getMessage();
                    return ret.toString();
                }
            } else {
                ret.code = 1002;
                ret.message = "上传文件失败:文件为空!";
                return ret.toString();
            }
        }
        return ret.toString();
    }
}
