package com.example.junittestpractice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩함.
class BookRepositoryTest {

    @Autowired // DI
    private BookRepository bookRepository;

    @Test
    @DisplayName("책 등록 Test")
    public void saveTest() {
        // given
        String title = "제목1";
        String author = "작가1";
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

    // 2. 책 목록보기

    // 3. 책 한건보기

    // 4. 책 수정

    // 5. 책 삭제

}