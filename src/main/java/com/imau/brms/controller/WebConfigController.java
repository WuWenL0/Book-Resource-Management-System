package com.imau.brms.controller;

import com.imau.brms.entity.Horse;
import com.imau.brms.entity.WebConfig;
import com.imau.brms.mapper.WebConfigMapper;
import com.mysql.jdbc.Blob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
public class WebConfigController {

    @Autowired
    private WebConfigMapper webConfigMapper;

    /*
        随书光盘配置页面
     */
    @RequestMapping("/admin/admin_brmsInfo.html")
    public String toBrmsConfig(Model model){
        WebConfig brms = webConfigMapper.findWebConfigByWebName("brms");
        if (brms == null){
            brms = new WebConfig();
        }
        model.addAttribute("brms",brms);
        return "admin/admin_brmsInfo";
    }

    @RequestMapping("/admin/admin_brmsInfo_do.html")
    public String BrmsConfigDo(WebConfig webConfig,@RequestParam("logo") MultipartFile file){
        webConfig.setWebName("brms");
        if (webConfigMapper.hasWebConfig(webConfig.getWebName())>0){
            try {
                if (!file.isEmpty()){
                    byte[] data = file.getBytes();
                    webConfig.setLogoImg(data);
                    webConfigMapper.updateWebLogoImg(webConfig);
                }
                webConfigMapper.updateWebConfig(webConfig);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try {
                if (!file.isEmpty()){
                    byte[] data = file.getBytes();
                    webConfig.setLogoImg(data);
                    webConfigMapper.updateWebLogoImg(webConfig);
                }
                webConfigMapper.insertWebConfig(webConfig);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "redirect:/admin/admin_brmsInfo.html";
    }

    /*
        运动马配置页面
     */
    @RequestMapping("/admin/admin_horseInfo.html")
    public String toHorseConfig(Model model){
        WebConfig horse = webConfigMapper.findWebConfigByWebName("horse");
        if (horse == null){
            horse = new WebConfig();
        }
        model.addAttribute("horse",horse);
        return "admin/admin_horseInfo";
    }

    @RequestMapping("/admin/admin_horseInfo_do.html")
    public String horseConfigDo(WebConfig webConfig,@RequestParam("logo") MultipartFile logo,@RequestParam("bg") MultipartFile bg){
        webConfig.setWebName("horse");
        if (webConfigMapper.hasWebConfig(webConfig.getWebName())>0){
            try {
                if (!logo.isEmpty()){
                    byte[] data = logo.getBytes();
                    webConfig.setLogoImg(data);
                    webConfigMapper.updateWebLogoImg(webConfig);
                }
                if (!bg.isEmpty()){
                    byte[] data = bg.getBytes();
                    webConfig.setBgImg(data);
                    webConfigMapper.updateWebbgImg(webConfig);
                }
                webConfigMapper.updateWebConfig(webConfig);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try {
                if (!logo.isEmpty()){
                    byte[] data = logo.getBytes();
                    webConfig.setLogoImg(data);
                    webConfigMapper.updateWebLogoImg(webConfig);
                }
                if (!logo.isEmpty()){
                    byte[] data = logo.getBytes();
                    webConfig.setBgImg(data);
                    webConfigMapper.updateWebbgImg(webConfig);
                }
                webConfigMapper.insertWebConfig(webConfig);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "redirect:/admin/admin_horseInfo.html";
    }

    /*
        获取logo
     */
    @RequestMapping(value = "/logoImg")
    public void getbrmsImage(HttpServletResponse response) throws Exception {
        WebConfig brmsLogo = webConfigMapper.findWebConfigByWebName("brms");
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputSream = response.getOutputStream();
        outputSream.write(brmsLogo.getLogoImg());
        outputSream.flush();
    }

    @RequestMapping(value = "/horse/logoImg")
    public void getHorseImage(HttpServletResponse response) throws Exception {
        WebConfig brmsLogo = webConfigMapper.findWebConfigByWebName("horse");
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputSream = response.getOutputStream();
        outputSream.write(brmsLogo.getLogoImg());
        outputSream.flush();
    }

}
