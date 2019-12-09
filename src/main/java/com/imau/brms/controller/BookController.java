package com.imau.brms.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imau.brms.dto.ResourceDTO;
import com.imau.brms.entity.Book;
import com.imau.brms.entity.Resource;
import com.imau.brms.mapper.BookMapper;
import com.imau.brms.mapper.ResourceMapper;
import com.imau.brms.service.ResourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class BookController {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ResourseService resourseService;
    /*
        管理员添加图书
     */
    @GetMapping("/admin/admin_book_add.html")
    public String toAdminAddBook(){
        return "admin/admin_book_add";
    }
    @PostMapping("/admin/book_add_do.html")
    public String adminAddBookDo(Book book , RedirectAttributes redirectAttributes){
        book.setUpdateTime(new Date());
        try {
            bookMapper.insert(book);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","图书添加失败！");
            return "redirect:/admin/admin_allbooks.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","图书添加成功！");
            return "redirect:/admin/admin_allbooks.html";
        }
    }

    /*
        管理员获得全部图书列表
     */
    @GetMapping("/admin/admin_allbooks.html")
    public String toAdminAllBook(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum ,@RequestParam(value="pageSize",defaultValue="10")int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        ArrayList<Book> allBooks = bookMapper.getAllBooks();
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
            bookMapper.delete(bookId);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","图书删除失败！");
            return "redirect:/admin/admin_allbooks.html";
        }finally {
            redirectAttributes.addFlashAttribute("succ","图书删除成功！");
            return "redirect:/admin/admin_allbooks.html";
        }
    }

    /*
        读者图书详情页
     */
    @RequestMapping("/book_detail.html")
    public String readerBookDetail(Integer bookId , Model model){
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
        PageHelper.startPage(pageNum,pageSize);
        String searchWordUp = "%"+searchWord+"%";
        ArrayList<Book> books = bookMapper.queryBook(searchType, searchWordUp);
        if (books.isEmpty()){
            model.addAttribute("error", "对不起没有找到相关图书！");
        }
        PageInfo<Book> pageInfo = new PageInfo<Book>(books);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("searchType",searchType);
        model.addAttribute("searchWord",searchWord);
        return "search";
    }
}
