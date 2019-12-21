package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.entity.Admin;
import com.imau.brms.mapper.AdminMapper;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @RequestMapping("/admin/admin_alladmins.html")
    public String toAllAdmin(Model model, @RequestParam(value="pageNum",defaultValue="1")int pageNum , @RequestParam(value="pageSize",defaultValue="10")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Admin> allAdmin = adminMapper.getAllAdmin();
        PageInfo<Admin> pageInfo = new PageInfo<Admin>(allAdmin);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/admin_alladmins";
    }

    /*
        添加管理员
     */

    @RequestMapping("/admin/admin_add.html")
    public String toAdminAdd(){
        return "admin/admin_add";
    }

    @RequestMapping("/admin/admin_add_do.html")
    public void adminAddDo(Admin admin , HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        try {
            admin.setPassword(new SimpleHash("SHA-256",admin.getPassword(),null,20).toHex());
            System.out.println(admin.getPassword());
            admin.setPerms("admin");
            adminMapper.insert(admin);
        } catch (Exception e) {
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部管理员','admin_alladmins.html',false)</script>"
            );
        }finally {
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部管理员','admin_alladmins.html',true)</script>"
            );
        }
    }
    /*
        超级管理员修改密码
     */
    @RequestMapping("/admin/admin_change_password.html")
    public String toAdminChangePassword(@RequestParam("id")Integer id , Model model){
        Admin admin = adminMapper.findById(id);
        model.addAttribute("admin", admin);
        return "admin/admin_change_password";
    }

    @RequestMapping("/admin/admin_change_password_do.html")
    public String adminChangePasswordDo( @RequestParam("id")Integer id ,String password, RedirectAttributes redirectAttributes ){
        try {
            adminMapper.changePassword(id,password);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","修改失败！");
            return "redirect:/admin/admin_alladmins.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","修改成功！");
            return "redirect:/admin/admin_alladmins.html";
        }
    }

    /*
        管理员修改密码
     */
    @RequestMapping("/admin/admin_change_me_password.html")
    public String toAdminMePassword(Model model){
        Subject subject = SecurityUtils.getSubject();
        Admin a = (Admin) subject.getPrincipal();
        Admin admin = adminMapper.findByUserName(a.getUsername());
        model.addAttribute("admin", admin);
        return "admin/admin_change_me_password";
    }

    @RequestMapping("/admin/admin_change_me_password_do.html")
    public void adminMePasswordDo(@RequestParam("id")Integer id , String oldpasswd, String newpasswd, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
        try {
            Admin admin = adminMapper.findById(id);
            oldpasswd = new SimpleHash("SHA-256",oldpasswd,null,20).toHex();
            newpasswd = new SimpleHash("SHA-256",newpasswd,null,20).toHex();
            if(!admin.getPassword().equals(oldpasswd)){
                response.getWriter().print("<script language=javascript>alert('旧密码错误,修改密码失败！');window.location='/admin/admin_change_me_password.html'</script>");
            }else{
                adminMapper.changePassword(id,newpasswd);
                response.getWriter().print("<script language=javascript>alert('密码修改成功，请重新登录！');window.parent.location.href='/admin/logout.html'</script>");
            }
        }catch (Exception e){
            try {
                response.getWriter().print("<script language=javascript>alert('修改密码失败！');window.location='/admin/admin_change_me_password.html'</script>");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*
        超级管理员删除普通管理员
     */
    @RequestMapping("/admin/admin_delete.html")
    public String adminDeleteDo( @RequestParam("id")Integer id , RedirectAttributes redirectAttributes ){
        try {
            adminMapper.delete(id);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","删除失败！");
            return "redirect:/admin/admin_alladmins.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","删除成功！");
            return "redirect:/admin/admin_alladmins.html";
        }
    }
}
