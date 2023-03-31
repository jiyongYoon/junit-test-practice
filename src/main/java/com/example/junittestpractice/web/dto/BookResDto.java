package com.example.junittestpractice.web.dto;

import com.example.junittestpractice.domain.Book;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookResDto {

    private Long id;
    private String title;
    private String author;

    public static BookResDto toDto(Book book) {
        return BookResDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }
}
