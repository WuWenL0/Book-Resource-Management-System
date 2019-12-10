package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.entity.DownloadLog;
import com.imau.brms.entity.ReaderCard;
import com.imau.brms.mapper.DownloadLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class DownloadLogController {

    @Autowired
    private DownloadLogMapper downloadLogMapper;

    @RequestMapping("/reader_downloadhistory.html")
    public String toReaderDownloadLog(HttpServletRequest request, Model model, @RequestParam(value="pageNum",defaultValue="1")int pageNum , @RequestParam(value="pageSize",defaultValue="10")int pageSize){
        ReaderCard reader = (ReaderCard) request.getSession().getAttribute("READER");
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<DownloadLog> downloadLogs = downloadLogMapper.selectByReaderId(reader.getReaderId());
        PageInfo<DownloadLog> pageInfo = new PageInfo<DownloadLog>(downloadLogs);
        model.addAttribute("pageInfo",pageInfo);
        return "reader_downloadhistory";
    }
}
