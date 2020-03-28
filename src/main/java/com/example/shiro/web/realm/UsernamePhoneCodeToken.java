package com.example.shiro.web.realm;

import com.example.shiro.web.entity.WebUserEntity;
import lombok.Data;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Data
public class UsernamePhoneCodeToken implements HostAuthenticationToken, RememberMeAuthenticationToken, Serializable {
    private String phoneNumber;
    private String code;
    private boolean rememberMe;
    private WebUserEntity principal;
    private HttpSession session;
    @Override
    public String getHost() {
        return code;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return phoneNumber;
    }

    public UsernamePhoneCodeToken(String phoneNumber, String code, HttpSession session) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.session = session;
    }
}
