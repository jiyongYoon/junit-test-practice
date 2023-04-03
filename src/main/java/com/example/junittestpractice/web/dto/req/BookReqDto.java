package com.example.junittestpractice.web.dto.req;

import com.example.junittestpractice.domain.Book;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class BookReqDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
