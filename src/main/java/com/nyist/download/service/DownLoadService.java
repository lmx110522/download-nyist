package com.nyist.download.service;


import com.nyist.download.Dao.CateDao;
import com.nyist.download.Dao.DownLoadDao;
import com.nyist.download.Dao.MessageDao;
import com.nyist.download.Dao.UserDao;
import com.nyist.download.pojo.Category;
import com.nyist.download.pojo.Download;
import com.nyist.download.pojo.TMessage;
import com.nyist.download.pojo.TUser;
import com.nyist.download.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@PropertySource(value="classpath:ftpd.properties")
@Transactional
@Service
public class DownLoadService {

    @Value("${FTP_ADDRESS}")
    private String host;
    @Value("${FTP_PORT}")
    private int port;
    @Value("${FTP_USERNAME}")
    private String username;
    @Value("${FTP_PASSWORD}")
    private String password;
    @Value("${FTP_BASE_PATH}")
    private String basePath;

    @Value("${IMAGE_BASE_URL}")
    private String fileUrl;

    @Autowired
    private DownLoadDao downLoadDao;

    @Autowired
    private DownLoadService downLoadService;

    @Autowired
    private CateService cateService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CateDao cateDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public List<Download> findNewAll() {
        Integer isPass = 1;
        List<Download> downloadList = downLoadDao.findDownloadsTop10ByIsPassOrderByDwDateDesc(isPass);
        return downloadList;
    }

    public List<Download> findDownCountsAll() {
        Integer isPass = 1;
        List<Download> downloadList = downLoadDao.findDownloadsTop10ByIsPassOrderByDownCountsDesc(isPass);
        return downloadList;
    }

    public NyistResult findById(String id) {
        Optional<Download> downloadOptional = downLoadDao.findById(id);
        if(downloadOptional.isPresent()){
            return NyistResult.ok(downloadOptional.get());
        }
        return NyistResult.build(500,"系统维护中...");
    }

    public List<Download> findByCate(String id) {
        Integer isPass = 1;
        if(id == ""){
            Sort sort = new Sort(Sort.Direction.DESC,"dwDate");
            Pageable pageable= PageRequest.of(0, 5,sort);
            Page<Download> pageData = downLoadDao.findDownloadsByIsPass(isPass, pageable);
            return pageData.getContent();
        }
        Optional<Category> categoryOptional = cateDao.findById(id);
        if(categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            List<Download> downloadList = downLoadDao.findDownloadsTop10ByCategoryByCidAndIsPassOrderByDwDateDesc(category,isPass);
            return downloadList;
        }
        return null;
    }

    public NyistResult thumb(Integer flag, String did, HttpSession session) {
        TUser user = (TUser) session.getAttribute("login_user");
        if(user == null){
            return NyistResult.build(500,"你还没有登录！");
        }
        else{
            Optional<Download> downloadOptional = downLoadDao.findById(did);
            if(downloadOptional.isPresent()){
                Download download = downloadOptional.get();
                if(flag == 1){
                    download.setThumbCounts(download.getThumbCounts()+1);
                    String loveUsers = download.getLoveUser();
                    download.setLoveUser(loveUsers+user.getId()+"@");
                    download.setThumbCounts(download.getThumbCounts()+1);
                    downLoadDao.save(download);
                }
                else{
                    download.setThumbCounts(download.getThumbCounts()-1);
                    String[] loveUsers = download.getLoveUser().split("@");
                    for (String loveUser : loveUsers) {
                        if(loveUser.equals(user.getId())){
                            flag = 0;
                        }
                    }
                    if(flag == 0){
                        download.setLoveUser(download.getLoveUser().replace(user.getId()+"@",""));
                        download.setThumbCounts(download.getThumbCounts()-1);
                    }
                    downLoadDao.save(download);
                }
            }
            else{
                return NyistResult.build(500,"系统正在维护中....");
            }
        }
        return NyistResult.ok();
    }

    public NyistResult list(HttpSession session) {
        Integer pageSize = 10;
//        首页的下载分类
        List<Category> categoryList = (List<Category>) session.getAttribute("categories");
        if (categoryList == null) {
            String categories = redisTemplate.opsForValue().get("categories");
            if (categories != null) {
                categoryList = JsonUtils.jsonToList(categories, Category.class);
            } else {
                categoryList = cateService.findAll();
                redisTemplate.opsForValue().set("categories", JsonUtils.objectToJson(categoryList), 1, TimeUnit.HOURS);
            }
            session.setAttribute("categories", categoryList);
        }
        Sort sort = new Sort(Sort.Direction.DESC,"dwDate");
        Pageable pageable= PageRequest.of(0, pageSize,sort);
        Integer isPass = 1;
        Page<Download> pageData = downLoadDao.findDownloadsByIsPass(isPass,pageable);

        return NyistResult.ok(pageData);
    }

    public NyistResult findCondition(String condition, String page,String cid) throws NoSuchFieldException {
        Integer pageSize = 10;
        Integer pageNum = Integer.valueOf(page).intValue();
        Pageable pageable = null;
        if(pageNum == null ||  pageNum <= 0){
            pageNum = 1;
        }
        else{
            pageNum = pageNum-1;
        }
        if(condition != null){
            List<Sort.Order> orders=new ArrayList<Sort.Order>();


            String[] strings = condition.split("@");
            for (String string : strings) {
                Boolean bool = checkIsProperty(string);
                if(bool){
                    orders.add( new Sort.Order(Sort.Direction. DESC, string));
                }
            }
             pageable= PageRequest.of(pageNum, pageSize, Sort.by(orders));
        }
        else{
             pageable= PageRequest.of(pageNum, pageSize);
        }
        Page<Download> pageData = null;
        Integer isPass = 1;
        if(cid == ""){
             pageData = downLoadDao.findDownloadsByIsPass(isPass,pageable);
             return NyistResult.ok(pageData);
        }
        else{
            Optional<Category> categoryOptional = cateDao.findById(cid);

            if(categoryOptional.isPresent()){
                pageData = downLoadDao.findDownloadsByCategoryByCidAndIsPass(categoryOptional.get(),isPass,pageable);
                return NyistResult.ok(pageData);
            }
            else{
                return NyistResult.build(500,"系统维护中...");
            }
        }
    }

    private Boolean checkIsProperty(String name){
        Class downLoad = Download.class;
        Field[] fields=downLoad.getDeclaredFields();
        boolean b=false;
        for(int i=0;i<fields.length;i++){
            if(fields[i].getName().equals(name)){
                b=true;
                break;
            }
        }
        if(b)
        {
            return true;
        }
        else
        {
            return  false;
        }

    }

    public NyistResult upload(HttpSession session) {
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
        TUser user = (TUser) session.getAttribute("login_user");
        if(user == null){
            return NyistResult.build(500,"你还没有登录！");
        }else{
            Optional<TUser> userOptional = userDao.findById(user.getId());
            if(userOptional.isPresent()){
                TUser tUser = userOptional.get();
                return NyistResult.ok(tUser);
            }
            return null;
        }
    }
    public NyistResult finshUpload(MultipartFile file, Boolean open, Download download, HttpSession session) throws IOException {
        TUser tUser = (TUser) session.getAttribute("login_user");
        if(tUser == null){
            return NyistResult.build(500,"您还没有登陆");
        }
        Optional<Category> categoryOptional = cateDao.findById(download.getCategoryByCid().getId());
        if(!categoryOptional.isPresent()){
            return NyistResult.build(500,"系统正在维护中....");
        }
        if(open){
            download.setIsTop(1);
        }else{
            download.setIsTop(0);
        }
        String prefix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = RandomStringUtils.randomNumeric(6);
        String dir = tUser.getId()+"/"+tUser.getPhone()+"/"+categoryOptional.get().getId();
        //文件上传
        FtpUtil.uploadFile(host, port, username, password,dir,basePath
                , filename+prefix, file.getInputStream());
        TUser user1 = userDao.findById(tUser.getId()).get();
        user1.setScore(user1.getScore()+categoryOptional.get().getSocre());
        user1.setUploadCounts(user1.getUploadCounts()+1);
        userDao.save(user1);
        download.setDwDate(DateFormat.formatDate(new Date()));
        download.setLoveUser("");
        download.setThumbCounts(0);
        download.setIsPass(0);
        download.setName(filename+prefix);
        download.setDownCounts(0);
        download.settUserByUid(tUser);
        download.setCommentCounts(0);
        download.setId(IDUtils.genItemId()+"");
        String ftp_url = fileUrl+"/"+tUser.getId()+"/"+tUser.getPhone()+"/"+categoryOptional.get().getId()+"/"+filename+prefix;
        download.setFileUrl(ftp_url);
        downLoadDao.save(download);
        changeSomeMsg(session);
        Optional<TUser> tUserOptional = userDao.findById(tUser.getId());
        if(tUserOptional.isPresent()){
            TUser user = tUserOptional.get();
            session.removeAttribute("tuser");
            session.removeAttribute("login_user");
            session.setAttribute("tuser",user);
            session.setAttribute("login_user",user);
        }
        return NyistResult.ok();
    }

    private void changeSomeMsg(HttpSession session){

        redisTemplate.delete("categories");
        redisTemplate.delete("downloadNews");
        redisTemplate.delete("downloadCounts");
        session.removeAttribute("categories");
        session.removeAttribute("downloadNews");
        session.removeAttribute("downloadCounts");
    }

    public NyistResult downFile(HttpSession session, String did) {
        TUser user = (TUser) session.getAttribute("login_user");
        if(user == null){
            return NyistResult.build(500,"请先登录才可以下载呦！");
        }
        if(user.getScore() <= 0){
            return NyistResult.build(500,"您的积分已经不足，请多参与到上传大队中来提高您的积分吧！");
        }
        Optional<Download> downloadOptional = downLoadDao.findById(did);
        if(!downloadOptional.isPresent()){
            return NyistResult.build(500,"这个文件目前已经不存在了，您可以查找同类型的文件！");
        }
        Download download = downloadOptional.get();
//        boolean bool = FtpUtil.downloadFile(host, port, username, password, download.getFileUrl(), download.getName(), "c:\\lmx\\");
//        if(!bool){
//            return NyistResult.build(500,"下载失败，您可以通过邮箱反馈给站长！");
//        }
        Integer socre = download.getCategoryByCid().getSocre();
        user.setScore(user.getScore()-socre);
        user.setDownCounts(user.getDownCounts()+1);
        userDao.save(user);
        download.setDownCounts(download.getDownCounts()+1);
        downLoadDao.save(download);
        changeSomeMsg(session);
        return NyistResult.build(200,"下载成功,您的积分减少了"+socre+"分",download.getFileUrl());
    }

    public List<Download> findRelateDownLoad(String id) {
        Optional<Download> downloadOptional = downLoadDao.findById(id);
        if(downloadOptional.isPresent()){
            Download download = downloadOptional.get();
            String id1 = download.getCategoryByCid().getId();
            List<Download> downloadList = findByCate(id1);
            return downloadList;
        }
        return null;
    }

    public Page<Download> findAll(Integer page){
        Integer pageSize = 10;
        if (page == null){
            page = 0;
        }
        Sort sort = new Sort(Sort.Direction.ASC,"isPass");
        Pageable pageable = PageRequest.of(page,pageSize,sort);
        Page<Download> downloadPage = downLoadDao.findAll(pageable);
        return  downloadPage;
    }

    public NyistResult adminDownloadFile(String did) {
        Optional<Download> downloadOptional = downLoadDao.findById(did);
        if(!downloadOptional.isPresent()){
            return NyistResult.build(500,"这个文件目前已经不存在了，您可以查找同类型的文件！");
        }
        Download download = downloadOptional.get();
//        boolean bool = FtpUtil.downloadFile(host, port, username, password, download.getFileUrl(), download.getName(), "c:\\lmx\\");
//        if(!bool){
//            return NyistResult.build(500,"下载失败！");
//        }
        return NyistResult.ok(download.getFileUrl());
    }

    public NyistResult doTask(String did,String desc,String flag,HttpSession session) {
        Integer value = Integer.valueOf(flag);
        Optional<Download> downloadOptional = downLoadDao.findById(did);
        if(!downloadOptional.isPresent()){
            return NyistResult.build(500,"系统维护中...");
        }else{
            Download download = downloadOptional.get();
            TUser user = download.gettUserByUid();
            if(value == 1){
                user.setIsRead(user.getIsRead()+1);
                userDao.save(user);
                TMessage message = new TMessage();
                message.setIsRead(1);
                desc = "关于标题<<"+download.getTitle()+">>文件审核已经通过，理由是:"+desc;
                message.setContent(desc);
                message.setmDate(DateFormat.formatDate(new Date()));
                message.settUserByUid(user);
                messageDao.save(message);
                download.setIsPass(1);
                downLoadDao.save(download);
                changeSomeMsg(session);
                return NyistResult.ok();
            }
            else if(value == 0){
                user.setIsRead(user.getIsRead()+1);
                userDao.save(user);
                TMessage message = new TMessage();
                message.setIsRead(1);
                desc = "关于标题<<"+download.getTitle()+">>文件审核被驳回，理由是:"+desc;
                message.setContent(desc);
                message.setmDate(DateFormat.formatDate(new Date()));
                message.settUserByUid(user);
                messageDao.save(message);
                downLoadDao.delete(download);
                return NyistResult.ok();
            }
            else{
                return NyistResult.build(500,"系统维护中...");
            }
        }
    }
}
