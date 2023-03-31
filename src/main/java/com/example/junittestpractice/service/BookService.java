package com.example.junittestpractice.service;

import com.example.junittestpractice.domain.Book;
import com.example.junittestpractice.domain.BookRepository;
import com.example.junittestpractice.web.dto.BookReqDto;
import com.example.junittestpractice.web.dto.BookResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 1. 책 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookResDto createBook(BookReqDto bookReqDto) {
        Book savedBook = bookRepository.save(bookReqDto.toEntity());
        return BookResDto.toDto(savedBook);
    }

    // 2. 책 목록 보기
    @Transactional(readOnly = true)
    public List<BookResDto> getAllBook(BookReqDto bookReqDto) {
        return bookRepository.findAll().stream()
                .map(BookResDto::toDto)
                .collect(Collectors.toList());
    }

    // 3. 책 한건 보기
    @Transactional(readOnly = true)
    public BookResDto getBook(Long id) {
        return BookResDto.toDto(bookRepository.findById(id)
                .orElseThrow(RuntimeException::new));
    }

    // 4. 책 삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // 5. 책 수정
    @Transactional
    public BookResDto updateBook(Long id, BookReqDto bookReqDto) {
        Book findBook = bookRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        Book updateBook = findBook.update(bookReqDto);
        return BookResDto.toDto(updateBook);
    }
}
