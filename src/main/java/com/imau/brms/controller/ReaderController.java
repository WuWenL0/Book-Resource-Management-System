package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.entity.ReaderCard;
import com.imau.brms.entity.ReaderInfo;
import com.imau.brms.mapper.ReaderMapper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class ReaderController {

    @Autowired
    private ReaderMapper readerMapper;

    /*
        前台读者详情
     */
    @RequestMapping("/reader_detail.html")
    public String toReaderDetail(){
        return "reader_detail";
    }

    /*
        前台读者修改密码
     */
    @RequestMapping("/reader_pwd_change.html")
    public String toReaderChangePwd(){
        return "reader_pwd_change";
    }

    @RequestMapping("/reader_pwd_change_do.html")
    public void toReaderChangePwdDo(HttpServletRequest request , HttpServletResponse response , String oldpasswd, String newpasswd) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        ReaderCard readerCard = (ReaderCard) request.getSession().getAttribute("READER");
        ReaderCard readerCardPd = readerMapper.findReaderCardByUserId(readerCard.getReaderId());
        oldpasswd = new SimpleHash("SHA-256",oldpasswd,null,20).toHex();
        if (!readerCardPd.getPasswd().equals(oldpasswd)){
            response.getWriter().print("<script language=javascript>alert('旧密码错误，修改密码失败！');window.location='/reader_pwd_change.html'</script>");
        }else {
            try {
                newpasswd = new SimpleHash("SHA-256",newpasswd,null,20).toHex();
                readerMapper.changeReaderPassword(readerCardPd.getReaderId(),newpasswd);
                response.getWriter().print("<script language=javascript>alert('密码修改成功，请重新登陆！');window.location='/logout.html'</script>");
            } catch (Exception e) {
                response.getWriter().print("<script language=javascript>alert('密码修改失败！');window.location='/reader_pwd_change.html'</script>");
            }
        }
    }


    /*
        前台读者修改资料
     */

    @RequestMapping("/reader_info_change.html")
    public String toReaderChangeInfo(HttpServletRequest request , Model model){
        ReaderCard readerCard = (ReaderCard) request.getSession().getAttribute("READER");
        ReaderInfo readerInfo = readerMapper.findReaderInfoByReaderId(readerCard.getReaderId());
        model.addAttribute("readerInfo",readerInfo);
        return "reader_info_change";
    }

    @RequestMapping("/reader_info_change_do.html")
    public void toReaderChangeInfoDo(HttpServletRequest request , HttpServletResponse response , ReaderInfo readerInfo) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Long readerId = ((ReaderCard) request.getSession().getAttribute("READER")).getReaderId();
        readerInfo.setReaderId(readerId);
        try {
            readerMapper.updateReaderInfo(readerInfo);
            response.getWriter().print("<script language=javascript>alert('资料更新成功');window.location='/reader_detail.html'</script>");
        } catch (Exception e) {
            response.getWriter().print("<script language=javascript>alert('资料更新失败！');window.location='/reader_detail.html'</script>");
        }
    }

    /*
        管理员所有读者列表
     */
    @GetMapping("/admin/admin_allreaders.html")
    public String toAdminAllReader(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,@RequestParam(value="pageSize",defaultValue="5")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<ReaderCard> readerCards = readerMapper.selectAllReaderCard();
        PageInfo<ReaderCard> pageInfo =  new PageInfo<ReaderCard>(readerCards);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/admin_allreaders";
    }

    /*
        管理员添加读者
     */
    @GetMapping("/admin/admin_reader_add.html")
    public String toAdminAddReader(){
        return "admin/admin_reader_add";
    }
    @PostMapping("/admin/admin_reader_add_do.html")
    public String adminAddReaderDo(ReaderCard readerCard , RedirectAttributes redirectAttributes){
        try {
            readerCard.setPasswd("bb8ce661128c8341533f10b34576d49ecac94c0b31edc6864f13bb76171c92e9");//123456
            readerMapper.insertCard(readerCard);
            readerMapper.insertInfo(readerCard);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","读者添加失败！");
            return "redirect:/admin/admin_allreaders.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","读者添加成功！");
            return "redirect:/admin/admin_allreaders.html";
        }
    }

    /*
        管理员删除读者
     */
    @GetMapping("/admin/admin_reader_delete.html")
    public String adminReaderDeleteDo(@RequestParam("readerId")Long readerId , RedirectAttributes redirectAttributes ){
        try {
            readerMapper.deleteCard(readerId);
            readerMapper.deleteInfo(readerId);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","读者删除失败！");
            return "redirect:/admin/admin_allreaders.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","读者删除成功！");
            return "redirect:/admin/admin_allreaders.html";
        }
    }



}
