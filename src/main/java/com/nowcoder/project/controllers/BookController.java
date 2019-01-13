package com.nowcoder.project.controllers;

import com.nowcoder.project.model.Book;
import com.nowcoder.project.model.User;
import com.nowcoder.project.service.BookService;
import com.nowcoder.project.service.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by nowcoder on 2018/08/04 下午3:41
 *
 *    forward和redirect是什么？
 *    是servlet种的两种主要的跳转方式。forward又叫转发，redirect叫做重定向。
 *
 *  forward(转发)：
 *      1.是服务器内部的重定向，服务器直接访问目标地址的 url网址，把里面的东西读取出来，但是客户端并不知道，因此用forward的话，客户端浏览器的网址是不会发生变化的。
 *      2.关于request: 由于在整个定向的过程中用的是同一个request，因此forward会将request的信息带到被重定向的jsp或者servlet中使用。
 *
 *  redirect（重定向）：
 *      1.是客户端的重定向，是完全的跳转。即服务器返回的一个url给客户端浏览器，然后客户端浏览器会重新发送一次请求，到新的url里面，因此浏览器中显示的url网址会发生变化。
 *      2.因为这种方式比forward多了一次网络请求，因此效率会低于forward。
 *
 */
@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = {"/index"}, method = {RequestMethod.GET})
    public String bookList(Model model) {

        User host = hostHolder.getUser();
        if (host != null) {
            model.addAttribute("host", host);
        }

        loadAllBooksView(model);
        return "book/books";          // jsp
    }

    @RequestMapping(path = {"/books/add"}, method = {RequestMethod.GET})
    public String addBook() {
        return "book/addbook";
    }


    @RequestMapping(path = {"/books/add/do"}, method = {RequestMethod.POST})
    public String doAddBook(
            @RequestParam("name") String name,
            @RequestParam("author") String author,
            @RequestParam("price") String price
    ) {

        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setPrice(price);
        bookService.addBooks(book);

        return "redirect:/index";

    }

    @RequestMapping(path = {"/books/{bookId:[0-9]+}/delete"}, method = {RequestMethod.GET})
    public String deleteBook(
            @PathVariable("bookId") int bookId
    ) {

        bookService.deleteBooks(bookId);
        return "redirect:/index";

    }

    @RequestMapping(path = {"/books/{bookId:[0-9]+}/recover"}, method = {RequestMethod.GET})
    public String recoverBook(
            @PathVariable("bookId") int bookId
    ) {

        bookService.recoverBooks(bookId);
        return "redirect:/index";
    }

    /**
     * 为model加载所有的book
     */
    private void loadAllBooksView(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
    }

    @ExceptionHandler()
    public String error(Exception e, Model model) {

        model.addAttribute("error", e.getMessage());
        return "error";
    }

}
