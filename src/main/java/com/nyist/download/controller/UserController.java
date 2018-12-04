package com.nyist.download.controller;

import com.google.gson.Gson;
import com.nyist.download.config.ConstantQiniu;
import com.nyist.download.pojo.TUser;
import com.nyist.download.service.UserService;
import com.nyist.download.util.IDUtils;
import com.nyist.download.util.NyistResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConstantQiniu constantQiniu;

    @ResponseBody
    @GetMapping("/buildCode")
    public NyistResult buildCode(String phone){
       NyistResult result =  userService.buildCode(phone);
       return result;
    }

    @ResponseBody
    @PostMapping("/login")
    public NyistResult login(String phone, String password, String code, HttpSession session){
        NyistResult result = userService.login(phone,password,code,session);
        return result;
    }

    @GetMapping("/loginout")
    public String loginout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mainPage/{uid}")
    public String mainPage(@PathVariable String uid, HttpSession session, Integer index){
       NyistResult result =  userService.mainPage(uid,session);
       if(result.getStatus() != 200){
           return "redirect:/";
       }
       else{
           return "person";
       }
    }

    @ResponseBody
    @PostMapping("/changeHeadImg")
    public NyistResult changeHeadImg(String img_base64, HttpSession session) throws IOException {
        Integer pos = img_base64.indexOf("64,");
        img_base64 = img_base64.substring(pos+3);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(img_base64);
        for(int i=0;i<b.length;++i)
        {
            if(b[i]<0)
            {
                //调整异常数据
                b[i]+=256;
            }
        }
        //生成jpeg图片
        String path = uploadQNImg(new ByteArrayInputStream(b), IDUtils.genImageName()); // KeyUtil.genUniqueKey()生成图片的随机名
        path = "http://"+path;
        TUser user = (TUser) session.getAttribute("tuser");
        session.removeAttribute("tuser");
        session.removeAttribute("login_user");
        user.setHeadImg(path);
        userService.changeImage(user,session);
        session.setAttribute("user",user);
        session.setAttribute("login_user",user);
        return NyistResult.ok(path);
    }
    private String uploadQNImg(InputStream file, String key) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
            Auth auth = Auth.create(constantQiniu.getAccessKey(), constantQiniu.getSecretKey());
            String upToken = auth.uploadToken(constantQiniu.getBucket());
            try {
                Response response = uploadManager.put(file, key, upToken,null,null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                String returnPath = constantQiniu.getPath() + "/" + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @ResponseBody
    @PostMapping(value = "/edit")
    public NyistResult edit(TUser user,HttpSession session){

       NyistResult result = userService.edit(user,session);

       return result;
    }


    @ResponseBody
    @GetMapping("/hasRead")
    public NyistResult hasRead(String checkedId,HttpSession session){
        NyistResult result = userService.hasRead(checkedId,session);
        return result;
    }

    @ResponseBody
    @GetMapping("/deleteMsg")
    public NyistResult deleteMsg(String mid,HttpSession session){
        NyistResult result = userService.deleteMsg(mid,session);
        return result;
    }
}
