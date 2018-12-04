package com.nyist.download.service;

import com.nyist.download.Dao.CommentDao;
import com.nyist.download.Dao.DownLoadDao;
import com.nyist.download.pojo.Comment;
import com.nyist.download.pojo.Download;
import com.nyist.download.pojo.TUser;
import com.nyist.download.util.DateFormat;
import com.nyist.download.util.IDUtils;
import com.nyist.download.util.NyistResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Optional;

@Transactional
@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private DownLoadDao downLoadDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public NyistResult sendComment(String did, String content, HttpSession session) {
        TUser user = (TUser) session.getAttribute("login_user");
        if(user == null){
            return NyistResult.build(500,"您还没有登录，请登录后再评论！");
        }
        else{
            Optional<Download> downloadOptional = downLoadDao.findById(did);
            if(downloadOptional.isPresent()){
                Download download = downloadOptional.get();
                download.setCommentCounts(download.getCommentCounts()+1);
                Comment comment = new Comment();
                comment.setcDate(DateFormat.formatDate(new Date()));
                comment.setContent(content);
                comment.setDownloadByDid(download);
                comment.setId(IDUtils.genItemId()+"");
                comment.settUserByUid(user);
                commentDao.save(comment);
                changeSomeMsg(session);
                return NyistResult.ok(comment);
            }
            else{
                return NyistResult.build(500,"系统维护中...");
            }

        }
    }
    private void changeSomeMsg(HttpSession session){
        redisTemplate.delete("categories");
        redisTemplate.delete("downloadNews");
        redisTemplate.delete("downloadCounts");
        session.removeAttribute("categories");
        session.removeAttribute("downloadNews");
        session.removeAttribute("downloadCounts");
    }
    public NyistResult getComments(String did, Integer page) {
        Integer pageSize = 5;
        Optional<Download> downloadOptional = downLoadDao.findById(did);
        if(downloadOptional.isPresent()){
            Download download = downloadOptional.get();
            Sort sort = new Sort(Sort.Direction.DESC,"cDate");
            Pageable pageable = PageRequest.of(page,pageSize,sort);
            Page<Comment> pageData = commentDao.findCommentByDownloadByDid(download,pageable);
            return NyistResult.ok(pageData.getContent());
        }
        return NyistResult.build(500,"系统维护中...");
    }

    public NyistResult delete(HttpSession session, String cid) {
        Optional<Comment> commentOptional = commentDao.findById(cid);
        if(!commentOptional.isPresent()){
            return NyistResult.build(500,"系统维护中...");
        }else{
            Comment comment = commentOptional.get();
            commentDao.delete(comment);
            Download download = comment.getDownloadByDid();
            download.setCommentCounts(download.getCommentCounts()-1);
            downLoadDao.save(download);
            changeSomeMsg(session);
            return NyistResult.ok();
        }
    }
}
