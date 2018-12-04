package com.nyist.download.service;

import com.nyist.download.pojo.Category;
import com.nyist.download.pojo.Download;
import com.nyist.download.util.JsonUtils;
import com.nyist.download.util.NyistResult;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class IndexService {

    @Autowired
    private DownLoadService downLoadService;

    @Autowired
    private CateService cateService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public NyistResult index(HttpSession session) {
//        首页的下载分类
        List<Category> categoryList = (List<Category>) session.getAttribute("categories");
        if(categoryList == null){
            String categories = redisTemplate.opsForValue().get("categories");
            if(categories != null){
                categoryList = JsonUtils.jsonToList(categories, Category.class);
            }
            else{
                categoryList = cateService.findAll();
                redisTemplate.opsForValue().set("categories",JsonUtils.objectToJson(categoryList),1, TimeUnit.HOURS);
            }
            session.setAttribute("categories",categoryList);
        }
//        首页的最新上传内容
        List<Download> downloadNewList  = new ArrayList<>();
        downloadNewList = (List<Download>) session.getAttribute("downloadNews");
        if(downloadNewList == null){
            String downloads = redisTemplate.opsForValue().get("downloadNews");
            if(downloads != null){
                downloadNewList = JsonUtils.jsonToList(downloads, Download.class);
            }
            else{
                downloadNewList = downLoadService.findNewAll();
                redisTemplate.opsForValue().set("downloadNews",JsonUtils.objectToJson(downloadNewList),1, TimeUnit.HOURS);
            }
            session.setAttribute("downloadNews",downloadNewList);
        }
//        首页的下载排行榜
        List<Download> downloadNumList  = new ArrayList<>();
        downloadNumList = (List<Download>) session.getAttribute("downloadCounts");
        if(downloadNumList == null){
            String downloadCounts = redisTemplate.opsForValue().get("downloadCounts");
            if(downloadCounts != null){
                downloadNumList = JsonUtils.jsonToList(downloadCounts, Download.class);
            }
            else{
                downloadNumList = downLoadService.findDownCountsAll();
                redisTemplate.opsForValue().set("downloadCounts",JsonUtils.objectToJson(downloadNumList),1, TimeUnit.HOURS);
            }
            session.setAttribute("downloadCounts",downloadNumList);
        }
        return NyistResult.ok();
    }
}
