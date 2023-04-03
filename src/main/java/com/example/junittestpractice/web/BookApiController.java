package com.example.junittestpractice.web;

import com.example.junittestpractice.service.BookService;
import com.example.junittestpractice.web.dto.req.BookReqDto;
import com.example.junittestpractice.web.dto.res.BookListResDto;
import com.example.junittestpractice.web.dto.res.BookResDto;
import com.example.junittestpractice.web.dto.res.CMResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookApiController {

    private final BookService bookService;

    // 1. 책 등록
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody @Valid BookReqDto bookReqDto,
                                        BindingResult bindingResult) {
        /*
        // 나중에는 AOP 처리하는 것이 좋음
        if (bindingResult.hasErrors()) { // 에러 발생 시 true 리턴
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(CMResDto.builder()
//                            .code(-1)
//                            .msg(errorMap.toString())
//                            .body(bookReqDto)
//                            .build()
//                    );

            throw new RuntimeException(errorMap.toString());
        }
         */
        bindingHasErrors(bindingResult);

        BookResDto savedBook = bookService.createBook(bookReqDto);

        CMResDto<?> cmResDto = CMResDto.builder()
                .code(1)
                .msg("저장 성공")
                .body(savedBook)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(cmResDto);
    }

    // 2. 책 목록 보기
    @GetMapping
    public ResponseEntity<?> getAllBook() {

//        List<BookResDto> allBook = bookService.getAllBook();
//
//        CMResDto<?> cmResDto = CMResDto.builder()
//                .code(1)
//                .msg("조회 성공")
//                .body(allBook)
//                .build();

        BookListResDto bookListResDto = bookService.getAllBook();

        CMResDto<?> cmResDto = CMResDto.builder()
                .code(1)
                .msg("조회 성공")
                .body(bookListResDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(cmResDto);
    }

    // 3. 책 한건 보기
    @GetMapping("{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {

        BookResDto findBook = bookService.getBook(id);

        CMResDto<?> cmResDto = CMResDto.builder()
                .code(1)
                .msg("조회 성공")
                .body(findBook)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(cmResDto);
    }

    // 4. 책 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        CMResDto<?> cmResDto = CMResDto.builder()
                .code(1)
                .msg("삭제 성공")
                .body(id)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(cmResDto);
    }

    // 5. 책 수정
    @PutMapping("{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id,
                                        @RequestBody @Valid BookReqDto bookReqDto,
                                        BindingResult bindingResult) {

        bindingHasErrors(bindingResult);

        BookResDto updateBook = bookService.updateBook(id, bookReqDto);

        CMResDto<?> cmResDto = CMResDto.builder()
                .code(1)
                .msg("수정 성공")
                .body(updateBook)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(cmResDto);
    }

    // 반복 메서드 추출
    private void bindingHasErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString());
        }
    }

}
