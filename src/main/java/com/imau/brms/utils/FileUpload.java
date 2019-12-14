package com.imau.brms.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
@Component
public class FileUpload {

    @Value("${upload.horse}")
    public String base_path;

    public static String BASE_PATH;

    @PostConstruct
    public void init() {
        BASE_PATH = base_path;
    }

    public static String fileUploadHorse(MultipartFile file,Integer id,String newName) {
        if (!file.isEmpty()) {
            BufferedOutputStream out = null;
            try {
                File file1 = new File(BASE_PATH + id + "/",newName + ".pdf");
                if(!file1.getParentFile().exists()) {
                    file1.getParentFile().mkdirs();
                }
                byte[] bytes = file.getBytes();
                out = new BufferedOutputStream(
                        new FileOutputStream(file1));
                out.write(bytes);

                return BASE_PATH + id + "/" + newName + ".pdf";
            } catch (Exception e) {
                return "false";
            } finally {
                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return "false";
        }
    }
    public static boolean delUploadHorse(String filesrc , Integer id) {
        try{
            File delFile = new File(filesrc);
            delFile.delete();
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
