package com.imau.brms.controller;
import	java.net.Authenticator;

import com.imau.brms.entity.Admin;
import com.imau.brms.entity.ReaderCard;
import com.imau.brms.mapper.ReaderMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Reader;
import java.util.HashMap;

@Controller
public class LoginController {

    @Autowired
    private ReaderMapper readerMapper;

    /*
        前台登录功能
     */
    @RequestMapping("/login.html")
    public String toLogin(){
        return "login";
    }

    @RequestMapping(value = "/api/readerLogin", method = RequestMethod.POST)
    public @ResponseBody Object readerLoginDo(ReaderCard readerCard, HttpServletRequest request, HttpServletResponse response) {
        readerCard.setPasswd(new SimpleHash("SHA-256",readerCard.getPasswd(),null,20).toHex());
        boolean isReader = readerMapper.hasMatchReader(readerCard.getReaderId(), readerCard.getPasswd())>0;
        HashMap<String, String> res = new HashMap<String, String>();
        if (isReader==false){
            res.put("stateCode", "0");
            res.put("msg","账号或密码错误！");
        } else if(isReader){
            ReaderCard reader = readerMapper.findReaderCardByUserId(readerCard.getReaderId());
            request.getSession().setAttribute("READER", reader);
            res.put("stateCode", "1");
            res.put("msg","读者登陆成功！");
        }
        return res;
    }

    /*
        前台注销功能
     */
    @RequestMapping("/logout.html")
    public String toLogout(HttpServletRequest request){
        request.getSession().removeAttribute("READER");
        return "redirect:/index.html";
    }
    /*
        后台登录功能逻辑实现
     */

    @RequestMapping("/admin/login.html")
    public String toAdminLogin(){
        return "admin/login";
    }

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
