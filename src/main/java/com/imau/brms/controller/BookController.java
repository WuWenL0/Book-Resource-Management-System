package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.dto.ResourceDTO;
import com.imau.brms.entity.Book;
import com.imau.brms.entity.Resource;
import com.imau.brms.entity.WebConfig;
import com.imau.brms.mapper.BookMapper;
import com.imau.brms.mapper.ResourceMapper;
import com.imau.brms.mapper.WebConfigMapper;
import com.imau.brms.service.ResourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Controller
public class BookController {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ResourseService resourseService;

    @Autowired
    private WebConfigMapper webConfigMapper;
    /*
        管理员添加图书
     */
    @GetMapping("/admin/admin_book_add.html")
    public String toAdminAddBook(){
        return "admin/admin_book_add";
    }
    @PostMapping("/admin/book_add_do.html")
    public void adminAddBookDo(Book book , HttpServletResponse response) throws IOException {
        book.setUpdateTime(new Date());
        response.setContentType("text/html;charset=utf-8");
        try {
            bookMapper.insert(book);
        }catch (Exception e){
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                    "<script language=javascript>tagcl('全部图书','admin_allbooks.html',false)</script>"
            );
        }finally {
            response.getWriter().print(
                    "<script type='text/javascript' src='js/tag.js'></script>" +
                            "<script language=javascript>tagcl('全部图书','admin_allbooks.html',true)</script>"
            );
        }
    }

    /*
        管理员获得全部图书列表
     */
    @GetMapping("/admin/admin_allbooks.html")
    public String toAdminAllBook(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,
                                 @RequestParam(value="pageSize",defaultValue="10")int pageSize,
                                 @RequestParam(value="searchWord",defaultValue="")String searchWord,
                                 @RequestParam(value="keyword",defaultValue="")String keyword){
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Book> allBooks;
        if (!searchWord.equals("")&&!keyword.equals("")){
            keyword="%"+keyword+"%";
            allBooks = bookMapper.queryBook(searchWord,keyword);
        }else{
            allBooks = bookMapper.getAllBooks();
        }
        PageInfo<Book> pageInfo = new PageInfo<Book>(allBooks);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/admin_allbooks";
    }

    /*
        管理员图书详情
     */
    @GetMapping("/admin/admin_book_detail.html")
    public String toAdminBookDetail(@RequestParam("bookId")Integer bookId , Model model){
        Book book = bookMapper.findBookById(bookId);
        model.addAttribute("book",book);
        return "admin/admin_book_detail";
    }

    /*
        管理员图书修改
     */
    @GetMapping("/admin/admin_book_update.html")
    public String toAdminBookUpdate( @RequestParam("bookId")Integer bookId , Model model ){
        Book book = bookMapper.findBookById(bookId);
        model.addAttribute("book",book);
        return "admin/admin_book_update";
    }
    @PostMapping("/admin/admin_book_update_do.html")
    public String adminBookUpdateDo( Book book , RedirectAttributes redirectAttributes){
        try {
            bookMapper.update(book);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","图书修改失败！");
            return "redirect:/admin/admin_allbooks.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","图书修改成功！");
            return "redirect:/admin/admin_allbooks.html";
        }
    }

    /*
        管理员图书删除
     */
    @GetMapping("/admin/admin_book_delete.html")
    public String adminBookDeleteDo( @RequestParam("bookId")Integer bookId , RedirectAttributes redirectAttributes ){
        try {
            if (resourceMapper.findResourcesByBookId(bookId).size()!=0){
                redirectAttributes.addFlashAttribute("error","图书删除失败,请先删除图书对应资源！");
                return "redirect:/admin/admin_allbooks.html";
            }
            bookMapper.delete(bookId);
            redirectAttributes.addFlashAttribute("succ","图书删除成功！");
            return "redirect:/admin/admin_allbooks.html";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","图书删除失败！");
            return "redirect:/admin/admin_allbooks.html";
        }
    }
    /**
     * 批量删除图书
     */
    @RequestMapping(value = "/admin/admin_book_batchDelete", method = RequestMethod.POST)
    public @ResponseBody Object loginCheck(String delNames, HttpServletRequest request){
        String arrDel[] = delNames.split(",");
        String arrDel1 = "";
        String arrDel2 = "";
        HashMap<String, String> res = new HashMap<String, String>();
        try {
            for (String bookId: arrDel) {
                if (resourceMapper.findResourcesByBookId(Integer.parseInt(bookId)).size()!=0){
                    arrDel1+=bookId+",";
                }else{
                    arrDel2+=bookId+",";
                }
            }
            if (arrDel1.length()==0){
                bookMapper.batchDelete(arrDel);
                res.put("stateCode", "1");
                res.put("msg","批量删除成功！");
            }else{
                String arrDelNames[] = arrDel2.split(",");
                bookMapper.batchDelete(arrDelNames);
                res.put("stateCode", "1");
                res.put("msg","批量删除成功！ID为"+arrDel1+"的图书删除失败，请先删除图书中的资源！");
            }


            return res;
        } catch (Exception e) {
            res.put("stateCode", "0");
            res.put("msg","批量删除失败！");
            return res;
        }
    }
    /*
        读者图书详情页
     */
    @RequestMapping("/book_detail.html")
    public String readerBookDetail(Integer bookId , Model model){
        WebConfig brms = webConfigMapper.findWebConfigByWebName("brms");
        model.addAttribute("brms",brms);
        Book book = bookMapper.findBookById(bookId);
        ArrayList<ResourceDTO> resourceDTOS = resourseService.list(bookId);
        model.addAttribute("book",book);
        model.addAttribute("resources", resourceDTOS);
        return "book_detail";
    }

    /*
        读者搜索功能
     */

    @RequestMapping("/search.html")
    public String readerQuerybook(String searchType , String searchWord , Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,@RequestParam(value="pageSize",defaultValue="20")int pageSize){
        WebConfig brms = webConfigMapper.findWebConfigByWebName("brms");
        model.addAttribute("brms",brms);
        PageHelper.startPage(pageNum,pageSize);
        String searchWordUp = "%"+searchWord+"%";
        ArrayList<Book> books = bookMapper.queryBook(searchType, searchWordUp);
        Integer booksSum = bookMapper.queryBookSum(searchType, searchWordUp);
        if (books.isEmpty()){
            model.addAttribute("error", "对不起没有找到相关图书！");
        }
        PageInfo<Book> pageInfo = new PageInfo<Book>(books);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("searchType",searchType);
        model.addAttribute("booksSum",booksSum);
        model.addAttribute("searchWord",searchWord);
        return "search";
    }
}
