package com.example.junittestpractice.web.dto;

import com.example.junittestpractice.domain.Book;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookReqDto {
    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
