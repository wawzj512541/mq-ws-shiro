package com.example.shiro.common.shiro;

import com.example.common.utils.RedisUtil;
import com.example.shiro.sys.entity.SysUserEntity;
import com.example.shiro.web.entity.WebUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class MyWebSessionManager extends DefaultWebSessionManager {

    @Autowired
    RedisUtil redisUtil;

    public final static String token_name = "token";

    public final static String session_id_Source = "request_header";

    @Value("${system.expire}")
    private Integer expire;
    private String sessionPrefix;   //session前缀,用于存储用户会话信息

    @Value("${system.manager}")
    private void setShiroPrefix(Boolean manager) {
        if (manager) {
            sessionPrefix = "sys_user";
        } else {
            sessionPrefix = "web_user";
        }
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String token = WebUtils.toHttp(request).getHeader(token_name); //这个修改就是从请求头里来获取
        if (StringUtils.isEmpty(token)) {
            //如果没有携带token参数则按照父类的方式在cookie进行获取
            return super.getSessionId(request, response);
        } else {
            //如果请求头中有 token 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, session_id_Source); //session_id  来源
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return token;
        }
    }

    //session刷新的时候同步刷新在线用户的Redis过期时间
    @Override
    protected void onChange(Session session) {
        SimplePrincipalCollection coll = (SimplePrincipalCollection) (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
        if (coll != null) {
            Object obj = coll.getPrimaryPrincipal();
            if (obj.getClass().getName().indexOf("SysUserEntity") > 0) {
                redisUtil.hset(sessionPrefix, ((SysUserEntity) obj).getUserId().toString(), session.getId(), expire);
            } else {
                redisUtil.hset(sessionPrefix, ((WebUserEntity) obj).getUserId().toString(), session.getId(), expire);
            }
        }
        super.onChange(session);
    }
}
