package com.debug.middleware.server.controller;

import com.debug.middleware.server.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图书接口
 */
@RequestMapping("/book")
@RestController
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    /*获取书籍对象信息*/
    //{"bookNo":10010,"name":"分布式中间件技术入门"}

    /**
     * 获取书籍对象信息
     * @param bookNo 图书编号
     * @param bookName 图书名称
     * @return
     */
    @RequestMapping(value = "info",method = {RequestMethod.GET,RequestMethod.POST})
    public Book info(Integer bookNo,String bookName){
        Book book = new Book();
        book.setBookNo(bookNo);
        book.setName(bookName);
        return book;
    }


}
