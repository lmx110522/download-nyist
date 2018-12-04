package com.nyist.download.config;

import com.aliyuncs.exceptions.ClientException;
import com.nyist.download.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues="sms")
public class ReceiveHandler {
//
//    @Autowired
//    private UserService userService;

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;



    @RabbitHandler
    public void getMsg(Map map){
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        System.out.println("phone:"+phone);
        System.out.println("code:"+code);
        try {
            smsUtil.sendSms(phone,template_code,sign_name,"{\"code\":\""+code+"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
