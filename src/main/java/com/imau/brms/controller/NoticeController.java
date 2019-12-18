package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.entity.Notice;
import com.imau.brms.entity.WebConfig;
import com.imau.brms.mapper.NoticeMapper;
import com.imau.brms.mapper.WebConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class NoticeController {

    @Autowired
    private NoticeMapper noticeMapper;


    /*
        管理员获取全部通知列表
     */
    @GetMapping("/admin/admin_allnotices.html")
    public String adminAllNotice(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,@RequestParam(value="pageSize",defaultValue="10")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Notice> allNotice = noticeMapper.getAllNotice();
        PageInfo<Notice> pageInfo = new PageInfo<Notice>(allNotice);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/admin_allnotices";
    }
    /*
        管理员添加通知
     */
    @GetMapping("/admin/admin_notice_add.html")
    public String toAdminAddNotice(){
        return "admin/admin_notice_add";
    }

    @PostMapping("/admin/admin_notice_add_do.html")
    public void adminAddNoticeDo(String name , String context , HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        try {
            Notice notice = new Notice();
            notice.setName(name);
            notice.setContext(context);
            notice.setCreatTime(new Date());
            noticeMapper.insert(notice);
        }catch (Exception e){
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部通知','admin_allnotices.html',false)</script>"
            );
        }finally {
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部通知','admin_allnotices.html',true)</script>"
            );
        }
    }

    /*
        管理员通知详情
     */
    @GetMapping("/admin/admin_notice_detail.html")
    public String adminNoticeDetail(Integer id , Model model){
        Notice noticeById = noticeMapper.findNoticeById(id);
        model.addAttribute("notice", noticeById);
        return "admin/admin_notice_detail";
    }

    /*
        管理员通知修改
     */
    @GetMapping("/admin/admin_notice_update.html")
    public String adminNoticeUpdate(Integer id , Model model){
        Notice noticeById = noticeMapper.findNoticeById(id);
        model.addAttribute("notice", noticeById);
        return "admin/admin_notice_update";
    }
    @PostMapping("/admin/admin_notice_update_do.html")
    public String adminNoticeUpdateDo(Integer id , String name , String context , RedirectAttributes redirectAttributes){
        try {
            Notice notice = new Notice();
            notice.setId(id);
            notice.setName(name);
            notice.setContext(context);
            noticeMapper.update(notice);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","修改通知失败！");
            return "redirect:/admin/admin_allnotices.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","修改通知成功！");
            return "redirect:/admin/admin_allnotices.html";
        }
    }

    /*
        管理员删除通知
     */
    @GetMapping("/admin/admin_notice_delete.html")
    public String adminReaderDeleteDo(Integer id , RedirectAttributes redirectAttributes ){
        try {
            noticeMapper.delete(id);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","通知删除失败！");
            return "redirect:/admin/admin_allnotices.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","通知删除成功！");
            return "redirect:/admin/admin_allnotices.html";
        }
    }

    /*
        读者查看详情
     */
    @Autowired
    private WebConfigMapper webConfigMapper;

    @GetMapping("/notice_detail.html")
    public String readerNoticeDetail(Integer id , Model model){
        WebConfig brms = webConfigMapper.findWebConfigByWebName("brms");
        model.addAttribute("brms",brms);
        Notice noticeById = noticeMapper.findNoticeById(id);
        model.addAttribute("notice", noticeById);
        return "notice_detail";
    }
}
