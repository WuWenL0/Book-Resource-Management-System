package com.imau.brms.controller;

import com.imau.brms.entity.Book;
import com.imau.brms.entity.Notice;
import com.imau.brms.entity.Resource;
import com.imau.brms.mapper.BookMapper;
import com.imau.brms.mapper.IndexMapper;
import com.imau.brms.mapper.NoticeMapper;
import com.imau.brms.mapper.ResourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class IndexController {

    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private ResourseMapper resourseMapper;

    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/")
    public String toIndex(Model model){
        ArrayList<Notice> allNotice = noticeMapper.getAllNotice();
        ArrayList<Resource> downSumSort = resourseMapper.getResourceDownSumSort();
        ArrayList<Book> recommendBooks = bookMapper.getRecommendBooks();
        model.addAttribute("notices", allNotice);
        model.addAttribute("resources", downSumSort);
        model.addAttribute("books", recommendBooks);
        return "index";
    }

    /*
        后台入口
     */
    @GetMapping("/admin")
    public String toAdmin(){
        return "redirect:/admin/index.html";
    }

    @GetMapping({"/admin/","/admin/index","/admin/index.html"})
    public String toAdminIndex(){
        return "admin/index";
    }

    @GetMapping("/admin/admin_main.html")
    public String toAdminMain(Model model){
        int bookCount = indexMapper.getBookCount();
        int downCount = indexMapper.getDownCount();
        int noticeCount = indexMapper.getNoticeCount();
        int readerCount = indexMapper.getReaderCount();
        model.addAttribute("bookcount", bookCount);
        model.addAttribute("downcount", downCount);
        model.addAttribute("readercount", readerCount);
        model.addAttribute("noticecount", noticeCount);
        return "admin/admin_main";
    }
}
