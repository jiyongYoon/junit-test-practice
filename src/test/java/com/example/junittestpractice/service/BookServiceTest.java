package com.example.junittestpractice.service;

import com.example.junittestpractice.domain.Book;
import com.example.junittestpractice.domain.BookRepository;
import com.example.junittestpractice.util.MailSender;
import com.example.junittestpractice.web.dto.req.BookReqDto;
import com.example.junittestpractice.web.dto.res.BookResDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
        // given -> client로 받는 데이터
        BookReqDto bookReqDto = new BookReqDto();
        bookReqDto.setTitle("제목");
        bookReqDto.setAuthor("작가");

        // stub -> 메서드에 대해 미리 준비된 결과 값을 제공하는 것을 Test stubs 라고 함
        given(bookRepository.save(any()))
                .willReturn(bookReqDto.toEntity());
        given(mailSender.send())
                .willReturn(true);

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
        // given
        // stub
        BookReqDto bookReqDto1 = new BookReqDto();
        bookReqDto1.setTitle("제목1");
        bookReqDto1.setAuthor("작가1");

        BookReqDto bookReqDto2 = new BookReqDto();
        bookReqDto2.setTitle("제목2");
        bookReqDto2.setAuthor("작가2");

        List<Book> bookList = Arrays.asList(bookReqDto1.toEntity(), bookReqDto2.toEntity());

        given(bookRepository.findAll())
                .willReturn(bookList);

        // when
        List<BookResDto> getAllBookList = bookService.getAllBook();

        // then
        assertThat(getAllBookList.size()).isEqualTo(bookList.size());
        assertThat(getAllBookList.get(0).getTitle()).isEqualTo(bookList.get(0).getTitle());
        assertThat(getAllBookList.get(1).getAuthor()).isEqualTo(bookList.get(1).getAuthor());
    }

    @Test
    void getBook() {
        // given
        Long id = 1L;

        // stub
        Book book = Book.builder()
                        .id(id)
                        .author("작가1")
                        .title("제목1")
                        .build();

        given(bookRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(book));

        // when
        BookResDto findBook = bookService.getBook(id);

        // then
        assertThat(findBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(findBook.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void deleteBook() {
        // 현재 service 로직이 없어서 테스트 할 것이 없음
    }

    @Test
    void updateBook() {
        // given
        Long id = 1L;
        BookReqDto bookReqDto = new BookReqDto();
        bookReqDto.setAuthor("작가2");
        bookReqDto.setTitle("제목2");

        // stub
        Book book = Book.builder()
                .id(id)
                .title("제목1")
                .author("작가1")
                .build();

        given(bookRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(book));

        // when
        BookResDto bookResDto = bookService.updateBook(id, bookReqDto);

        // then
        assertThat(bookResDto.getTitle()).isEqualTo(bookReqDto.getTitle());
        assertThat(bookResDto.getAuthor()).isEqualTo(bookReqDto.getAuthor());
    }
}