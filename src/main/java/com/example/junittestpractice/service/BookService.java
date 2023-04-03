package com.example.junittestpractice.service;

import com.example.junittestpractice.domain.Book;
import com.example.junittestpractice.domain.BookRepository;
import com.example.junittestpractice.util.MailSender;
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
    // 이렇게 Interface를 의존하게 되면 실제 구현체를 의존하지 않아서 결합도가 낮아질 수 있음.
    // 그리고 아직 구현되지 않은 기능들에 대한 테스트도 가능해짐.
    // MailSender를 Implements 한 실제 구현체에 기능이 완성되어도 여기 클래스의 코드는 변할 필요가 없음.
    private final MailSender mailSender;

    // 1. 책 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookResDto createBook(BookReqDto bookReqDto) {
        Book savedBook = bookRepository.save(bookReqDto.toEntity());
        if (savedBook != null) {
            if (!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다.");
            }
        }
        return BookResDto.toDto(savedBook);
    }

    // 2. 책 목록 보기
    @Transactional(readOnly = true)
    public List<BookResDto> getAllBook() {
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
        findBook.update(bookReqDto); // jpa-dirty-check
        return BookResDto.toDto(findBook);
    }
}
