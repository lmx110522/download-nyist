package com.nyist.download.controller;

import com.nyist.download.pojo.Download;
import com.nyist.download.pojo.TUser;
import com.nyist.download.service.DownLoadService;
import com.nyist.download.util.NyistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RequestMapping("/download")
@Controller
public class DownLoadController {

    @Autowired
    private DownLoadService downLoadService;

    @GetMapping("/list")
    public String list(HttpSession session,Model model){
        NyistResult result  = downLoadService.list(session);
        Page<Download> resultData = (Page<Download>) result.getData();
        model.addAttribute("resultData",resultData);
        return "list";
    }
    @GetMapping("/upload")
    public String upload(HttpSession session,Model model,String errorMsg){
        NyistResult result = downLoadService.upload(session);
        if(result.getStatus() != 200){
            return "redirect:/";
        }
        model.addAttribute("uploadUser",result.getData());
        model.addAttribute("errorMsg",errorMsg);
        return "upload";
    }
    @GetMapping("/notice")
    public String notice(){
        return "notice";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable String id, Model model){
        NyistResult result = downLoadService.findById(id);
        if(result.getStatus() == 200){
            List<Download> relateDownLoad = downLoadService.findRelateDownLoad(id);
            model.addAttribute("downLoad",result.getData());
            model.addAttribute("relateDownLoad",relateDownLoad);
            return "detail";
        }
        return "404";
    }

    @ResponseBody
    @GetMapping("/findByCate")
    public NyistResult findByCate(String cid){
        List<Download> downloadList = downLoadService.findByCate(cid);
        if(downloadList == null || downloadList.size() == 0){
            return NyistResult.build(500,"没有此类型的上传文件！");
        }
        return NyistResult.ok(downloadList);
    }

    @ResponseBody
    @GetMapping("/thumb")
    public NyistResult thumb(Integer flag, String did, HttpSession session){
        NyistResult result = downLoadService.thumb(flag,did,session);
        return result;
    }

    @ResponseBody
    @GetMapping("/findCondition")
    public NyistResult findCondition(String condition,String page,String cid) throws NoSuchFieldException {
        NyistResult result = downLoadService.findCondition(condition,page,cid);
        return result;
    }

    @ResponseBody
    @PostMapping("/finshUpload")
    public NyistResult finshUpload(MultipartFile file,Boolean open, Download download,HttpSession session) throws IOException {
        if(open == null){
            open = false;
        }
          NyistResult result = downLoadService.finshUpload(file,open,download,session);
          return result;
    }

    @ResponseBody
    @GetMapping("/downFile/{did}")
    public NyistResult downFile(HttpSession session,@PathVariable String did){
        NyistResult result = downLoadService.downFile(session,did);
        return result;
    }
}

