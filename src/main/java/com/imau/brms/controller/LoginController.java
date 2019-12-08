package com.imau.brms.controller;

import com.imau.brms.entity.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class LoginController {

    @RequestMapping("/admin/login.html")
    public String toAdminLogin(){
        return "admin/login";
    }

    /*
        登录功能逻辑实现
     */
    @RequestMapping(value = "/api/loginCheck", method = RequestMethod.POST)
    public @ResponseBody Object loginCheck(Admin admin, HttpServletRequest request){
        HashMap<String, String> res = new HashMap<String, String>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(admin.getUsername(),admin.getPassword());
        try {
            subject.login(token);
            request.getSession().setAttribute("admin",admin);
            res.put("stateCode", "1");
            res.put("msg","管理员登陆成功！");
            return res;
        }catch (UnknownAccountException e){
            res.put("stateCode", "0");
            res.put("msg","管理员账号不存在！");
            return res;
        }catch (IncorrectCredentialsException e){
            res.put("stateCode", "0");
            res.put("msg","管理员密码错误！");
            return res;
        }
    }

    /*
        管理员注销
     */
    @RequestMapping("/admin/logout.html")
    public String adminLogout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/admin/index.html";
    }
}
