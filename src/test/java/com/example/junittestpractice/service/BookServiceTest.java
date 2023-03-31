package com.example.junittestpractice.service;

import com.example.junittestpractice.domain.BookRepository;
import com.example.junittestpractice.util.MailSender;
import com.example.junittestpractice.util.MailSenderAdapter;
import com.example.junittestpractice.web.dto.BookReqDto;
import com.example.junittestpractice.web.dto.BookResDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookServiceTest {

    @Autowired
    BookRepository bookRepository;

    // 주석처리 테스트의 문제점
    // Service만 테스트하고 싶은데, 레포지토리 레이어가 함께 테스트 된다!
    @Test
    void createBook() {
        // given
        BookReqDto bookReqDto = new BookReqDto();
        bookReqDto.setTitle("제목");
        bookReqDto.setAuthor("작가");

        // stub
        MailSender mailSender = new MailSenderAdapter();

        // when
        BookService bookService = new BookService(bookRepository, mailSender);
        BookResDto savedBook = bookService.createBook(bookReqDto);

        // then
        assertEquals(bookReqDto.getTitle(), savedBook.getTitle());
        assertEquals(bookReqDto.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void getAllBook() {
    }

    @Test
    void getBook() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void updateBook() {
    }
}