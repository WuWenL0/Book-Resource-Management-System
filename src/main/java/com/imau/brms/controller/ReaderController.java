package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.entity.ReaderCard;
import com.imau.brms.entity.ReaderInfo;
import com.imau.brms.mapper.ReaderMapper;
import com.imau.brms.service.ImportReaderService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

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
        ReaderCard reader = (ReaderCard) request.getSession().getAttribute("READER");
        Long readerId = reader.getReaderId();
        readerInfo.setReaderId(readerId);
        try {
            readerMapper.updateReaderInfo(readerInfo);
            reader.setName(readerInfo.getName());
            readerMapper.updateReaderCardName(reader);
            request.getSession().setAttribute("READER",reader);
            response.getWriter().print("<script language=javascript>alert('资料更新成功');window.location='/reader_detail.html'</script>");
        } catch (Exception e) {
            response.getWriter().print("<script language=javascript>alert('资料更新失败！');window.location='/reader_detail.html'</script>");
        }
    }

    /*
        管理员所有读者列表
     */
    @GetMapping("/admin/admin_allreaders.html")
    public String toAdminAllReader(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,
                                   @RequestParam(value="pageSize",defaultValue="10")int pageSize,
                                   @RequestParam(value="searchWord",defaultValue="")String searchWord,
                                   @RequestParam(value="keyword",defaultValue="")String keyword){
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<ReaderInfo> readerInfos;
        if (!searchWord.equals("")&&!keyword.equals("")){
            keyword="%"+keyword+"%";
            readerInfos = readerMapper.queryAllReaderInfo(searchWord,keyword);
        }else{
            readerInfos = readerMapper.selectAllReaderInfo();
        }

        PageInfo<ReaderInfo> pageInfo =  new PageInfo<ReaderInfo>(readerInfos);
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
    public void adminAddReaderDo(ReaderCard readerCard , ReaderInfo readerInfo , HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        try {
            readerCard.setPasswd("bb8ce661128c8341533f10b34576d49ecac94c0b31edc6864f13bb76171c92e9");//123456
            readerMapper.insertCard(readerCard);
            readerMapper.insertInfo(readerInfo);
        }catch (Exception e){
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部读者','admin_allreaders.html',false)</script>"
            );
        }finally {
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部读者','admin_allreaders.html',true)</script>"
            );
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
    /**
     * 批量删除读者
     */
    @RequestMapping(value = "/admin/admin_reader_batchDelete", method = RequestMethod.POST)
    public @ResponseBody Object loginCheck(String delNames, HttpServletRequest request){
        String arrDel[] = delNames.split(",");
        HashMap<String, String> res = new HashMap<String, String>();
        try {
            readerMapper.batchDelete("db_reader_card",arrDel);
            readerMapper.batchDelete("db_reader_info",arrDel);
            res.put("stateCode", "1");
            res.put("msg","批量删除成功！");
            return res;
        } catch (Exception e) {
            res.put("stateCode", "0");
            res.put("msg","批量删除失败！");
            return res;
        }
    }

    /**
     * 导入excel
     */
    @Autowired
    private ImportReaderService importReaderService;

    @RequestMapping("/admin/import.html")
    public String excelImport(@RequestParam(value="file") MultipartFile file , RedirectAttributes redirectAttributes){

        int result = 0;

        try {
            result = importReaderService.addReader(file);
        } catch (Exception e) {
            String error = e.getMessage();
            if (error.equals("is not text")){
                redirectAttributes.addFlashAttribute("error","excel单元格类型不是文本类型！");
                return "redirect:/admin/admin_allreaders.html";
            }else {
                redirectAttributes.addFlashAttribute("error","上传失败请检查！");
                return "redirect:/admin/admin_allreaders.html";
            }
        }

        if(result > 0){
            redirectAttributes.addFlashAttribute("succ","excel文件数据导入成功！");
            return "redirect:/admin/admin_allreaders.html";
        }else{
            redirectAttributes.addFlashAttribute("error","excel文件数据导入成功！");
            return "redirect:/admin/admin_allreaders.html";
        }

    }

}
