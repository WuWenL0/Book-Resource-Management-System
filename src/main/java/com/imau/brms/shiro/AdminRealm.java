package com.imau.brms.shiro;

import com.imau.brms.entity.Admin;
import com.imau.brms.mapper.AdminMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminRealm extends AuthorizingRealm {

    @Autowired
    private AdminMapper adminMapper;
    /*
        授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        Admin dbUser = adminMapper.findById(admin.getId());
        info.addStringPermission(dbUser.getPerms()!=null?dbUser.getPerms():"");
        return info;
    }

    /*
        认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Admin admin = adminMapper.findByUserName(token.getUsername());
        if(admin == null){
            return null;
        }
        return new SimpleAuthenticationInfo(admin,admin.getPassword(),"");
    }
}
