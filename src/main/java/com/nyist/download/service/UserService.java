package com.nyist.download.service;

import com.nyist.download.Dao.MessageDao;
import com.nyist.download.Dao.UserDao;
import com.nyist.download.pojo.Identy;
import com.nyist.download.pojo.TMessage;
import com.nyist.download.pojo.TUser;
import com.nyist.download.util.DateFormat;
import com.nyist.download.util.IDUtils;
import com.nyist.download.util.NyistResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Service
public class UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageDao messageDao;


    @Autowired
    private UserDao userDao;

    public NyistResult buildCode(String phone) {

        boolean bool = isMobile(phone);
        if(!bool){
            return NyistResult.build(500,"手机号不存在");
        }
        String code = RandomStringUtils.randomNumeric(6);
        Map map = new HashMap();
        map.put("phone",phone);
        map.put("code",code);
        rabbitTemplate.convertAndSend("sms",map);
        redisTemplate.opsForValue().set("sms_"+phone,code,2, TimeUnit.HOURS);

        return NyistResult.ok();
    }

    public NyistResult login(String phone, String password, String code, HttpSession session) {
        TUser user = userDao.findByPhone(phone);
        if(user == null && StringUtils.isEmpty(code)){
            return NyistResult.build(500,"你还没有注册，所以请使用验证码登录！");
        }
        if(user != null && StringUtils.isEmpty(code)){
            boolean bool = (DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword()));
            if(bool){
                session.setAttribute("login_user",user);
                return NyistResult.build(200,"登录成功");
            }
            else{
                return  NyistResult.build(500,"登录密码错误！");
            }
        }
        String oldCode = (String) redisTemplate.opsForValue().get("sms_" + phone);
        if(oldCode == null){
            return NyistResult.build(500,"验证码不存在或者已经过期！");
        }
        if(oldCode.equals(code)){
            redisTemplate.delete("sms_" + phone);
            if(user == null){
                TUser tUser = new TUser();
                tUser.setId(IDUtils.genItemId()+"");
                tUser.setUsername(phone);
                tUser.setPassword(DigestUtils.md5DigestAsHex(code.getBytes()));
                tUser.setPhone(phone);
                tUser.setHeadImg("http://phxh0zvpl.bkt.clouddn.com/%E5%88%98%E5%BE%B7%E5%8D%8E.jpg");
                tUser.setScore(200);
                tUser.setDownCounts(0);
                tUser.setUploadCounts(0);
                tUser.setIsRead(0);
                Identy identy = new Identy();
                identy.setId(2);
                tUser.setIdentyByIid(identy);
                tUser.setuDesc("这个人很懒，什么也没有留下~");
                userDao.save(tUser);
                session.setAttribute("login_user",tUser);
                return NyistResult.build(200,"你已经成功注册，初始密码是你刚刚发送的验证码");
            }
            else{
                session.setAttribute("login_user",user);
                return NyistResult.build(200,"登录成功");
            }
        }
        return NyistResult.build(500,"验证码错误!");
    }
    private boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public NyistResult mainPage(String uid, HttpSession session) {
        TUser user = (TUser) session.getAttribute("login_user");
        if(user == null){
            return NyistResult.build(500,"用户没有登录！");
        }
        else{
            Optional<TUser> userOptional = userDao.findById(uid);
            if(userOptional.isPresent()) {
                session.setAttribute("tuser", userOptional.get());
            } else{
                return NyistResult.build(500,"用户不存在！");
            }
            return NyistResult.ok();
        }
    }

    public void changeImage(TUser user,HttpSession session) {
        userDao.save(user);
        changeSomeMsg(session);
    }

    public NyistResult edit(TUser user, HttpSession session) {

      TUser tUser = (TUser) session.getAttribute("login_user");
      if(tUser == null){
          return NyistResult.build(500,"您还没有登录，请先登录！");
      }
      else{
          tUser = (TUser) session.getAttribute("tuser");
          tUser.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
          tUser.setuDesc(user.getuDesc());
          tUser.setBirth(user.getBirth());
          tUser.setUsername(user.getUsername());
          userDao.save(tUser);
          session.setAttribute("tuser",tUser);
          session.setAttribute("login_user",tUser);
          changeSomeMsg(session);
          return NyistResult.ok();
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

    public NyistResult hasRead(String checkedId, HttpSession session) {
        TUser user = (TUser) session.getAttribute("login_user");
        if(user == null){
            return NyistResult.build(500,"请先登录..");
        }
        String[] mids = checkedId.split("@");
        Integer nums = 0;
        String callIds = "";
        for (String mid : mids) {
            Integer value = Integer.valueOf(mid);
            Optional<TMessage> messageOptional = messageDao.findById(value);
            if(messageOptional.isPresent()){
                nums++;
                TMessage message = messageOptional.get();
                callIds += message.getId()+"@";
                message.setIsRead(0);
                messageDao.save(message);
            }
        }
        user = userDao.findById(user.getId()).get();
        user.setIsRead(user.getIsRead()-nums);
        userDao.save(user);
        session.setAttribute("login_user",user);
        session.setAttribute("tuser",user);
        return NyistResult.build(200,user.getIsRead()+"",callIds);
    }

    public NyistResult deleteMsg(String mid, HttpSession session) {
        TUser user = (TUser) session.getAttribute("login_user");
        if(user == null){
            return NyistResult.build(500,"请先登录..");
        }
        Integer flag = 0;
        Integer value = Integer.valueOf(mid);
        Optional<TMessage> messageOptional = messageDao.findById(value);
        if(messageOptional.isPresent()){
            TMessage message = messageOptional.get();
            if(message.getIsRead() == 1){
                flag = 1;
            }
            messageDao.delete(message);
        }
        user = userDao.findById(user.getId()).get();
        if(flag != 0){
            user.setIsRead(user.getIsRead()-1);
            userDao.save(user);
        }
        session.setAttribute("login_user",user);
        session.setAttribute("tuser",user);
        return NyistResult.ok(user.getIsRead());
    }
}
