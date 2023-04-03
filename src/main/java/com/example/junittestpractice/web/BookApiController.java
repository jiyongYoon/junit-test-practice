package com.example.junittestpractice.web;

import com.example.junittestpractice.service.BookService;
import com.example.junittestpractice.web.dto.req.BookReqDto;
import com.example.junittestpractice.web.dto.res.BookResDto;
import com.example.junittestpractice.web.dto.res.CMResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
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

        BookResDto savedBook = bookService.createBook(bookReqDto);

        CMResDto<?> cmResDto = CMResDto.builder()
                .code(1)
                .msg("저장 성공")
                .body(savedBook)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(cmResDto);
    }

    // 2. 책 목록 보기
    public void getAllBook() {

    }

    // 3. 책 한건 보기

    public void getBook() {

    }

    // 4. 책 삭제
    public void deleteBook() {

    }

    // 5. 책 수정
    public void updateBook() {

    }

}
