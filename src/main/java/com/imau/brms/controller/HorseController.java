package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.entity.Horse;
import com.imau.brms.entity.WebConfig;
import com.imau.brms.mapper.HorseMapper;
import com.imau.brms.mapper.WebConfigMapper;
import com.imau.brms.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class HorseController {

    @Autowired
    private HorseMapper horseMapping;

    @Autowired
    private WebConfigMapper webConfigMapper;

    @RequestMapping("/horse")
    public String toAdmin(){
        return "redirect:/horse/index.html";
    }

    @RequestMapping({"/horse/","/horse/index","/horse/index.html"})
    public String toAdminIndex(Model model){
        WebConfig horse = webConfigMapper.findWebConfigByWebName("horse");
        WebConfig brms = webConfigMapper.findWebConfigByWebName("brms");
        model.addAttribute("horse",horse);
        model.addAttribute("brms",brms);
        return "horse/index";
    }

    /*
        管理员添加运动马项目
     */
    @RequestMapping("/admin/admin_horse_add.html")
    public String toAdminAddHorse(Model model){
        WebConfig horse = webConfigMapper.findWebConfigByWebName("horse");
        model.addAttribute("horse",horse);
        return "admin/admin_horse_add";
    }
    @RequestMapping("/admin/admin_horse_add_do.html")
    public void adminAddHorseDo(Horse horse, RedirectAttributes redirectAttributes , HttpServletResponse response , @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        try {
            horseMapping.insertInfo(horse);
            if (!file1.isEmpty()){
                String f1 = FileUpload.fileUploadHorse(file1, horse.getId() , "file1");
                if (!f1.equals("false")) horse.setFile1Src(f1);
            }
            if (!file2.isEmpty()){
                String f2 = FileUpload.fileUploadHorse(file2, horse.getId() , "file2");
                if (!f2.equals("false")) horse.setFile2Src(f2);
            }
            if (!file3.isEmpty()) {
                String f3 = FileUpload.fileUploadHorse(file3, horse.getId() , "file3");
                if (!f3.equals("false")) horse.setFile3Src(f3);
            }
            horseMapping.insertFile(horse);
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部项目','admin_allhorse.html',true)</script>"
            );
        } catch (Exception e) {
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部项目','admin_allhorse.html',false)</script>"
            );
        }
    }
    /*
        管理员删除运动马项目
     */
    @RequestMapping("/admin/admin_horse_delete.html")
    public String adminHorseDeleteDo(Integer id, RedirectAttributes redirectAttributes){
        try {
            Horse horseF = horseMapping.findHorseById(id);
            if (horseF.getFile1Src()!=null){
                FileUpload.delUploadHorse(horseF.getFile1Src(),horseF.getId());
            }
            if (horseF.getFile2Src()!=null){
                FileUpload.delUploadHorse(horseF.getFile2Src(),horseF.getId());
            }
            if (horseF.getFile3Src()!=null){
                FileUpload.delUploadHorse(horseF.getFile3Src(),horseF.getId());
            }
            horseMapping.delete(id);
            redirectAttributes.addFlashAttribute("succ","项目删除成功");
            return "redirect:/admin/admin_allhorse.html";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error","项目删除失败");
            return "redirect:/admin/admin_allhorse.html";
        }
    }

    /*
        管理员查询所有项目
     */
    @GetMapping("/admin/admin_allhorse.html")
    public String toAdminAllHorse(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,@RequestParam(value="pageSize",defaultValue="10")int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        ArrayList<Horse> allHorses = horseMapping.getAllhorse();
        PageInfo<Horse> pageInfo = new PageInfo<Horse>(allHorses);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/admin_allhorse";
    }

    /*
        管理员修改项目
     */
    @GetMapping("/admin/admin_horse_update.html")
    public String toAdminHorseUpdate( @RequestParam("id")Integer id , Model model ){
        WebConfig horseCon = webConfigMapper.findWebConfigByWebName("horse");
        model.addAttribute("horseCon",horseCon);
        Horse horse = horseMapping.findHorseById(id);
        model.addAttribute("horse",horse);
        return "admin/admin_horse_update";
    }
    @PostMapping("/admin/admin_horse_update_do.html")
    public String adminHorseUpdateDo(Horse horse, RedirectAttributes redirectAttributes , HttpServletRequest request , @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3){
        try {
            horseMapping.updateInfo(horse);
            Horse horseF = horseMapping.findHorseById(horse.getId());
            if (!file1.isEmpty()){
                if (horseF.getFile1Src()!=null){
                    FileUpload.delUploadHorse(horseF.getFile1Src(),horseF.getId());
                }
                String f1 = FileUpload.fileUploadHorse(file1, horse.getId() , "file1");
                if (!f1.equals("false")) {
                    horseMapping.updateFile("file1_src",f1,horseF.getId());
                }
            }
            if (!file2.isEmpty()){
                if (horseF.getFile2Src()!=null){
                    FileUpload.delUploadHorse(horseF.getFile2Src(),horseF.getId());
                }
                String f2 = FileUpload.fileUploadHorse(file2, horse.getId() , "file2");
                if (!f2.equals("false")) {
                    horseMapping.updateFile("file2_src",f2,horseF.getId());
                }

            }if (!file3.isEmpty()){
                if (horseF.getFile3Src()!=null){
                    FileUpload.delUploadHorse(horseF.getFile3Src(),horseF.getId());
                }
                String f3 = FileUpload.fileUploadHorse(file3, horse.getId() , "file3");
                if (!f3.equals("false")) {
                    horseMapping.updateFile("file3_src",f3,horseF.getId());
                }
            }
            redirectAttributes.addFlashAttribute("succ","项目修改成功");
            return "redirect:/admin/admin_allhorse.html";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error","项目修改失败");
            return "redirect:/admin/admin_allhorse.html";
        }
    }

    /*
        前台搜索功能
     */
    @RequestMapping("/horse/search.html")
    public String readerQueryhorse(String searchType , String searchWord , Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,@RequestParam(value="pageSize",defaultValue="20")int pageSize){
        WebConfig horse = webConfigMapper.findWebConfigByWebName("horse");
        model.addAttribute("horse",horse);
        PageHelper.startPage(pageNum,pageSize);
        String searchWordUp = "%"+searchWord+"%";
        ArrayList<Horse> horses = horseMapping.queryHorse(searchType, searchWordUp);
        Integer horsesSum = horseMapping.queryHorseSum(searchType, searchWordUp);
        if (horses.isEmpty()){
            model.addAttribute("error", "对不起没有找到相关项目！");
        }
        PageInfo<Horse> pageInfo = new PageInfo<Horse>(horses);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("searchType",searchType);
        model.addAttribute("horsesSum",horsesSum);
        model.addAttribute("searchWord",searchWord);
        return "horse/search";
    }

    /*
        前台项目详情页
     */
    @RequestMapping("/horse/horse_detail.html")
    public String toReaderHorseDetail(Integer id , Model model){
        WebConfig horse = webConfigMapper.findWebConfigByWebName("horse");
        model.addAttribute("horseCon",horse);
        Horse horseById = horseMapping.findHorseById(id);
        model.addAttribute("horse",horseById);
        return "horse/horse_detail";
    }

    /*
        文档在线浏览
     */

    @RequestMapping(value = "/horse/preview")
    public void viewerPDF(String path ,HttpServletRequest request , HttpServletResponse response) {
        File file = new File(path);
        if (file.exists()) {
            byte[] data = null;
            try {
                FileInputStream input = new FileInputStream(file);
                data = new byte[input.available()];
                input.read(data);
                response.getOutputStream().write(data);
                input.close();
            } catch (Exception e) {
                System.out.println("pdf文件处理异常...");
            }
        }
    }
}