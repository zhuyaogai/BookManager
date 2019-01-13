package com.nowcoder.project.service;


import com.nowcoder.project.dao.BookDAO;
import com.nowcoder.project.model.Book;
import com.nowcoder.project.model.enums.BookStatusEnum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nowcoder on 2018/08/04 下午3:41
 */
@Service
public class BookService {

    @Autowired
    private BookDAO bookDAO;

    public List<Book> getAllBooks() {
        return bookDAO.selectAll();
    }

    public int addBooks(Book book) {
        if (bookDAO.ifExistBook(book.getName()) > 0)
            throw new IllegalArgumentException("该书已存在于图书馆中，不需要再添加！");

        return bookDAO.addBook(book);
    }

    public void deleteBooks(int id) {
        if (bookDAO.getBookStatus(id) == BookStatusEnum.DELETE.getValue())
            throw new IllegalArgumentException("该书本已被借出！无法再借取！");

        bookDAO.updateBookStatus(id, BookStatusEnum.DELETE.getValue());
    }

    public void recoverBooks(int id) {
        if (bookDAO.getBookStatus(id) == BookStatusEnum.NORMAL.getValue())
            throw new IllegalArgumentException("该书本已被归还！无需再归还！");

        bookDAO.updateBookStatus(id, BookStatusEnum.NORMAL.getValue());
    }
}
