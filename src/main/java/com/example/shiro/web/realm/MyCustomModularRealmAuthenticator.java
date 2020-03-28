package com.example.shiro.web.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;
import java.util.Iterator;


public class MyCustomModularRealmAuthenticator extends ModularRealmAuthenticator{

    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        AuthenticationStrategy authenticationStrategy = this.getAuthenticationStrategy();
        AuthenticationInfo authenticationInfo = authenticationStrategy.beforeAllAttempts(realms,token);

        Iterator var5 = realms.iterator();
        while(var5.hasNext()) {
            Realm realm = (Realm)var5.next();
            authenticationInfo = authenticationStrategy.beforeAttempt(realm, token, authenticationInfo);
            if (realm.supports(token)) {

                AuthenticationInfo info = null;
                Throwable t = null;

                info = realm.getAuthenticationInfo(token);

                authenticationInfo = authenticationStrategy.afterAttempt(realm, token, info, authenticationInfo, t);
            }
        }
        authenticationInfo = authenticationStrategy.afterAllAttempts(token, authenticationInfo);
        return authenticationInfo;
    }
}
