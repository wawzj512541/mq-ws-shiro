/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.shiro.web.controller;


import com.example.common.utils.RedisUtil;
import com.example.config.RabbitConfig;
import com.example.shiro.common.result.APIResponse;
import com.example.shiro.common.utils.ShiroUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController("webLogin")
public class WebLoginController {
    @Autowired
    private Producer producer;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${system.expire}")
    private Integer expire;

    /**
     * 获取验证码
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/web/captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }


    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/web/login", method = RequestMethod.POST)
    public APIResponse login(String username, String password, String captcha) {
        /*String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(kaptcha)) {
            return APIResponse.returnFail("验证码不正确");
        }*/
        String sessionId;
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            sessionId = subject.getSession().getId().toString();    //获取sessionId,返回给请求端
            //登录成功发送消息
            redisUtil.set("web_user:"+ShiroUtils.getUserId1().toString(), sessionId, expire);
            amqpTemplate.convertAndSend(RabbitConfig.WEB_LOGIN_QUEUE, ShiroUtils.getWebUser());
        } catch (UnknownAccountException e) {
            return APIResponse.returnFail(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return APIResponse.returnFail("账号或密码不正确");
        } catch (LockedAccountException e) {
            return APIResponse.returnFail("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return APIResponse.returnFail("账户验证失败");
        }
        return APIResponse.returnSuccess(sessionId);
    }

    /**
     * 退出
     */
    @RequestMapping(value = "/web/logout", method = RequestMethod.GET)
    public APIResponse logout() {
        Long id = ShiroUtils.getUserId1();
        ShiroUtils.logout();
        redisUtil.remove("web_user:"+id.toString());
        return APIResponse.returnSuccess();
    }

}
