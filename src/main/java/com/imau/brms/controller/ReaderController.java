package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.entity.ReaderCard;
import com.imau.brms.mapper.ReaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
