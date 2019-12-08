package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.dto.ResourseDTO;
import com.imau.brms.entity.Book;
import com.imau.brms.entity.Resource;
import com.imau.brms.mapper.BookMapper;
import com.imau.brms.mapper.ResourseMapper;
import com.imau.brms.service.ResourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResourseController {

    @Value("${upload.profile}")
    private String BASE_PATH;

    @Autowired
    private ResourseMapper resourseMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ResourseService resourseService;


    /*
        管理员获得所有书列表
     */

    @GetMapping("/admin/admin_allresources.html")
    public String toAdminAllResourses(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,@RequestParam(value="pageSize",defaultValue="10")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Book> allBooks = bookMapper.getAllBooks();
        PageInfo<Book> pageInfo = new PageInfo<Book>(allBooks);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/admin_allresources";
    }

    /*
        管理员根据书ID查询所有资源
     */
    @GetMapping("/admin/admin_resource_detail.html")
    public String adminBookAllResoursesByBookId(@RequestParam("bookId") Integer bookId , Model model){
        ArrayList<ResourseDTO> list = resourseService.list(bookId);
        model.addAttribute("bookId",bookId);
        model.addAttribute("resources",list);
        return "admin/admin_resource_detail";
    }

    /*
        管理员删除指定资源
     */
    @GetMapping("/admin/admin_resource_delete.html")
    public String adminDeleteResourceDo(Integer bookId , Integer resId , RedirectAttributes redirectAttributes){
        try{
            Resource resource = resourseMapper.findResourceByResId(resId);
            File delFile = new File(resource.getResSrc()+resource.getResName());
            delFile.delete();
            resourseMapper.delete(resId);
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error","资源删除失败！");
            return "redirect:/admin/admin_resource_detail.html?bookId="+bookId;
        }finally {
            redirectAttributes.addFlashAttribute("succ","资源删除成功！");
            return "redirect:/admin/admin_resource_detail.html?bookId="+bookId;
        }
    }

    /*
        管理员上传资源
     */

    @PostMapping("/admin/upload.html")
    public String handleFileUpload(HttpServletRequest request, @RequestParam("bookId") Integer bookId , RedirectAttributes redirectAttributes) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream out = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    File file1 = new File(BASE_PATH + bookId + "/" + file.getOriginalFilename());
                    if(!file1.getParentFile().exists()) {
                        file1.getParentFile().mkdirs();
                    }
                    System.out.println(file1.getParentFile());
                    byte[] bytes = file.getBytes();
                    out = new BufferedOutputStream(new FileOutputStream(file1));
                    out.write(bytes);
                    Resource resource = new Resource();
                    resource.setBookId(bookId);
                    resource.setResName(file.getOriginalFilename());
                    resource.setResSrc(BASE_PATH + bookId + "/");
                    resource.setResType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
                    resourseMapper.insert(resource);
                    redirectAttributes.addFlashAttribute("succ","资源添加成功");
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("succ","上传文件失败 " + i + " => " + e.getMessage());
                    return "redirect:/admin/admin_resource_detail.html?bookId="+bookId;
                } finally {
                    if (null != out) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        out = null;
                    }
                }
            } else {
                redirectAttributes.addFlashAttribute("succ","上传文件失败 " + i + " 因为文件不存在或损坏.");
                return "redirect:/admin/admin_resource_detail.html?bookId="+bookId;
            }
        }
        return "redirect:/admin/admin_resource_detail.html?bookId="+bookId;
    }

    /*
        下载功能（断点续传）
     */

    @RequestMapping(value = "/admin/download.html", method = RequestMethod.GET)
    public void getDownload(Integer resId, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // Get your file stream from wherever.
        Resource resource = resourseMapper.findResourceByResId(resId);
        String fullPath = resource.getResSrc() + resource.getResName();
        File downloadFile = new File(fullPath);

        ServletContext context = request.getServletContext();
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }

        // set content attributes for the response
        response.setContentType(mimeType);
        // response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", URLEncoder.encode(downloadFile.getName(), "utf-8"));
        response.setHeader(headerKey, headerValue);
        // 解析断点续传相关信息
        response.setHeader("Accept-Ranges", "bytes");
        long downloadSize = downloadFile.length();
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            response.setHeader("Content-Length", downloadSize + "");
        } else {
            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            response.setHeader("Content-Length", size + "");
            downloadSize = size;
        }
        // Copy the stream to the response's output stream.
        RandomAccessFile in = null;
        OutputStream out = null;
        try {
            in = new RandomAccessFile(downloadFile, "rw");
            // 设置下载起始位置
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            // 缓冲区大小
            int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
            byte[] buffer = new byte[bufLen];
            int num;
            int count = 0; // 当前写到客户端的大小
            out = response.getOutputStream();
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                //处理最后一段，计算不满缓冲区的大小
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize-count);
                    if(bufLen==0){
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
        前台读者下载功能
     */
    @GetMapping("/download.html")
    public void getReaderDownload(Integer resId, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // Get your file stream from wherever.
        Resource resource = resourseMapper.findResourceByResId(resId);
        String fullPath = resource.getResSrc() + resource.getResName();
        File downloadFile = new File(fullPath);

        ServletContext context = request.getServletContext();
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }

        // set content attributes for the response
        response.setContentType(mimeType);
        // response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", URLEncoder.encode(downloadFile.getName(), "utf-8"));
        response.setHeader(headerKey, headerValue);
        // 解析断点续传相关信息
        response.setHeader("Accept-Ranges", "bytes");
        long downloadSize = downloadFile.length();
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            response.setHeader("Content-Length", downloadSize + "");
        } else {
            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            response.setHeader("Content-Length", size + "");
            downloadSize = size;
        }
        // Copy the stream to the response's output stream.
        RandomAccessFile in = null;
        OutputStream out = null;
        try {
            in = new RandomAccessFile(downloadFile, "rw");
            // 设置下载起始位置
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            // 缓冲区大小
            int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
            byte[] buffer = new byte[bufLen];
            int num;
            int count = 0; // 当前写到客户端的大小
            out = response.getOutputStream();
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                //处理最后一段，计算不满缓冲区的大小
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize-count);
                    if(bufLen==0){
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
        读者在线浏览视频
     */
    @GetMapping("player.html")
    public void readerPlayerVideo(Integer resId ,HttpServletRequest request , HttpServletResponse response) throws IOException {
        Resource resource = resourseMapper.findResourceByResId(resId);
        String fullPath = resource.getResSrc() + resource.getResName();
        File file = new File(fullPath);
        Long fileSize = file.length();
        Long lasttime = file.lastModified();
        Long size = fileSize;
        int i = 0;

        String range = request.getHeader("Range");
        String ifm = request.getHeader("If-Modified-Since");
        if (ifm != null && Long.parseLong(ifm) < lasttime) {//检查文件修改时间判断是否启用缓存
            response.setStatus(304);
        } else {
            byte[] buff = new byte[131072];//1024*1024/2
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedOutputStream bfo = null;
            try {
                bfo = new BufferedOutputStream(response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (range != null) {//断点续传请求
                    size = (long) (131050);//限制每次视频输出的大小
                    String[] ranges = range.replace("bytes=", "").split("-");

                    if (ranges.length < 2) {//为Content-Range参数做铺垫
                        ranges = new String[]{ranges[0], "0"};
                        if (fileSize - Long.parseLong(ranges[0]) > size) {
                            ranges[1] = String.valueOf(Long.parseLong(ranges[0]) + size);
                        } else {
                            ranges[1] = String.valueOf(fileSize - 1);
                        }
                    }

                    raf.seek(Long.parseLong(ranges[0]));//从指定位置开始读取
                    response.setHeader("Last-Modified", String.valueOf(lasttime));//文件最后修改时间
                    response.setHeader("Content-Range", "bytes " + StringUtils.join(ranges, "-") + "/" + fileSize);//请求的数据 开始-结束/文件总大小
                    response.setStatus(206);
                }
                int len = 0;
                while ((len = raf.read(buff)) > 0 && i < size) {

                    bfo.write(buff);
                    bfo.flush();
                    i += len;

                }
            } catch (Exception e) {
                System.out.println("输出流非正常关闭：");
            } finally {
                raf.close();
                bfo.close();
            }

        }
    }

    /*
        文档在线浏览
     */

}
