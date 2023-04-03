package com.example.junittestpractice.service;

import com.example.junittestpractice.domain.BookRepository;
import com.example.junittestpractice.util.MailSender;
import com.example.junittestpractice.util.MailSenderAdapter;
import com.example.junittestpractice.web.dto.BookReqDto;
import com.example.junittestpractice.web.dto.BookResDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

//@DataJpaTest
@ExtendWith(MockitoExtension.class) // 가짜 메모리 환경
class BookServiceTest {

//    @Autowired
//    BookRepository bookRepository;

    @InjectMocks // 가짜 환경의 가짜 객체가 주입됨
    private BookService bookService;
    @Mock // 가짜 환경에 가짜 객체가 뜸
    private BookRepository bookRepository;
    @Mock
    private MailSender mailSender;


    // 주석처리 테스트의 문제점 (맨 앞에 주석되어 있는 부분)
    // Service만 테스트하고 싶은데, 레포지토리 레이어가 함께 테스트 된다!
    @Test
    void createBook() {
        // given
        BookReqDto bookReqDto = new BookReqDto();
        bookReqDto.setTitle("제목");
        bookReqDto.setAuthor("작가");

        given(bookRepository.save(any()))
                .willReturn(bookReqDto.toEntity());
        given(mailSender.send())
                .willReturn(true);

//        // stub
//        MailSender mailSender = new MailSenderAdapter();

        // when
//        BookService bookService = new BookService(bookRepository, mailSender);
        BookResDto savedBook = bookService.createBook(bookReqDto);

        // then
        // junit test method
        assertEquals(savedBook.getTitle(), bookReqDto.getTitle());
        assertEquals(savedBook.getAuthor(), bookReqDto.getAuthor());
        // asssertj test method -> 어떤 것을 기대하는지 등 가독성이 더 좋음!
        assertThat(savedBook.getTitle()).isEqualTo(bookReqDto.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(bookReqDto.getAuthor());
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