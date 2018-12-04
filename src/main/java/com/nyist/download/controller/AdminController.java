package com.nyist.download.controller;

import com.nyist.download.pojo.Admin;
import com.nyist.download.pojo.Download;
import com.nyist.download.service.AdminService;
import com.nyist.download.service.DownLoadService;
import com.nyist.download.util.NyistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DownLoadService downLoadService;

    @GetMapping(value = {"/loginUI","/"})
    public String loginUI(){
        return "/admin/loginUI";
    }

    @PostMapping("/login")
    public String login(Admin admin,HttpSession session){
        admin = adminService.login(admin);
        if(admin == null){
            return "redirect:/admin/loginUI";
        }
        else{
            session.setAttribute("admin",admin);
            return "redirect:/admin/index";
        }
    }

    @GetMapping("/index")
    public String index(Model model,HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if(admin == null){
            return "redirect:/admin/loginUI";
        }
        Page<Download> downloadPage = downLoadService.findAll(null);
        model.addAttribute("downloadPage",downloadPage);
        return "/admin/index";
    }

    @ResponseBody
    @GetMapping("/findByPage/{page}")
    public NyistResult findByPage(@PathVariable String page,HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if(admin == null){
            return NyistResult.build(500,"您还没有登录！");
        }
        Integer value = Integer.valueOf(page);
        if(value != null && value >=1){
            Page<Download> downloadPage = downLoadService.findAll(value - 1);
            return NyistResult.ok(downloadPage);
        }else{
            return NyistResult.build(500,"系统正在维护中...");
        }
    }

    @ResponseBody
    @GetMapping("/findById")
    public NyistResult findById(String did,HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if(admin == null){
            return NyistResult.build(500,"您还没有登录！");
        }
        NyistResult nyistResult = downLoadService.findById(did);
        return nyistResult;
    }
    @ResponseBody
    @GetMapping("/downloadFile")
    public NyistResult downloadFile(String did,HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if(admin == null){
            return NyistResult.build(500,"您还没有登录！");
        }
        NyistResult nyistResult = downLoadService.adminDownloadFile(did);
        return nyistResult;
    }

    @ResponseBody
    @GetMapping("/doTask")
    public NyistResult doTask(String did, String desc, String flag, HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if(admin == null){
            return NyistResult.build(500,"您还没有登录！");
        }
        NyistResult nyistResult = downLoadService.doTask(did,desc,flag,session);
        return nyistResult;
    }
}
