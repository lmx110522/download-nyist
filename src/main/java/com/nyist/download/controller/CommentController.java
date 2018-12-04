package com.nyist.download.controller;

import com.nyist.download.service.CommentService;
import com.nyist.download.util.NyistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping("/sendComment")
    public NyistResult sendComment(String did, String content, HttpSession session){
        NyistResult result = commentService.sendComment(did,content,session);
        return  result;
    }

    @ResponseBody
    @GetMapping("/getComments/{did}/{page}")
    public NyistResult getComments(@PathVariable String did,@PathVariable Integer page){
        NyistResult result = commentService.getComments(did,page);
        return result;
    }

    @ResponseBody
    @GetMapping("/delete/{cid}")
    public NyistResult delete(@PathVariable String cid,HttpSession session){
       NyistResult result =  commentService.delete(session,cid);
       return result;
    }

}
