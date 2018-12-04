package com.nyist.download.controller;

import com.nyist.download.pojo.Category;
import com.nyist.download.service.IndexService;
import com.nyist.download.util.NyistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping(value = {"/","/index"})
    public String index(HttpSession session){

        NyistResult result = indexService.index(session);

        return "index";
    }


}
