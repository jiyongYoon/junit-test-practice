package com.example.junittestpractice.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest(showSql = false) // DB와 관련된 컴포넌트만 메모리에 로딩함.
class BookRepositoryTest {

    @Autowired // DI
    private BookRepository bookRepository;

    String title = "제목1";
    String author = "작가1";

    @BeforeAll // 테스트 시작 전 한번만 실행
//    @BeforeEach // 각 테스트 시작 전 실행
    public void givenBook() {
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
        System.out.println("========== save one book ==========");
    }

    @Test
    @Order(1)
    @DisplayName("책 등록 Test")
    public void saveTest() {
        // given
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when
        Book savedBook = bookRepository.save(book);

        // then
        assertEquals(title, savedBook.getTitle());
        assertEquals(author, savedBook.getAuthor());
    }

    @Test
    @Order(2)
    @DisplayName("책 목록보기 Test")
    public void getAllBookTest() {
        // given

        // when
        List<Book> books = bookRepository.findAll();

        //then
        assertEquals(title, books.get(0).getTitle());
        assertEquals(author, books.get(0).getAuthor());
    }

    @Test
    @Order(3)
    @DisplayName("책 한건보기 Test")
    public void getBookTest() {
        // given
//        Book book = Book.builder()
//                .title(title)
//                .author(author)
//                .build();
//        Book savedBook = bookRepository.save(book);

        // when
//        Book findBook = bookRepository.findById(savedBook.getId()).get();
        Book findBook = bookRepository.findById(1L).get();

        // then
        assertEquals(findBook.getTitle(), title);
        assertEquals(findBook.getAuthor(), author);
    }

    // 4. 책 삭제
    @Test
    @Order(4)
    @DisplayName("책 삭제 Test")
    public void deleteBookTest() {
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then
        assertFalse(bookRepository.findById(id).isPresent());
    }

    // 5. 책 수정
    @Test
    @Order(5)
    @DisplayName("책 수정 Test")
    public void updateBookTest() {
        // given
        Long id = 1L;
        Book newBook = Book.builder()
                .id(id)
                .author("수정작가")
                .title("수정제목")
                .build();

        // when
        Book savedBook = bookRepository.save(newBook);

        // then
        assertEquals(savedBook.getId(), newBook.getId());
        assertEquals(savedBook.getAuthor(), newBook.getAuthor());
        assertEquals(savedBook.getTitle(), newBook.getTitle());
    }

}