package com.imau.brms.controller;

import com.imau.brms.entity.Book;
import com.imau.brms.entity.DownloadLog;
import com.imau.brms.entity.Notice;
import com.imau.brms.entity.Resource;
import com.imau.brms.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class IndexController {

    @Autowired
    private IndexMapper indexMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private DownloadLogMapper downloadLogMapper;

    @Autowired
    private BookMapper bookMapper;

    @GetMapping({"/","/index","/index.html"})
    public String toIndex(Model model){
        ArrayList<Notice> allNotice = noticeMapper.getAllNotice();
        ArrayList<Resource> downSumSort = resourceMapper.getResourceDownSumSort();
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
        ArrayList<DownloadLog> downloadLogs = downloadLogMapper.selectAllLog();
        model.addAttribute("bookcount", bookCount);
        model.addAttribute("downcount", downCount);
        model.addAttribute("readercount", readerCount);
        model.addAttribute("noticecount", noticeCount);
        model.addAttribute("downloadLogs", downloadLogs);
        return "admin/admin_main";
    }
}
