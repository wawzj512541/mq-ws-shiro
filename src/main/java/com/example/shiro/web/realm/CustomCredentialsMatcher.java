package com.example.shiro.web.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.util.DigestUtils;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    private final String salt = "KECHAUNGHUI";//盐

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        Object tokenCredentials = encrypt(String.valueOf(token.getPassword()));
        Object accountCredentials = getCredentials(info);
        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        return equals(tokenCredentials, accountCredentials);
    }

    //将传进来密码加密方法
    private String encrypt(String data) {
        String encodePwd = DigestUtils.md5DigestAsHex((data + salt).getBytes());;//这里可以选择自己的密码验证方式 比如 md5或者sha256等
        return encodePwd;
    }
}
