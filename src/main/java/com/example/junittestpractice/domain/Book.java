package com.example.junittestpractice.domain;

import com.example.junittestpractice.web.dto.req.BookReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;
    private String author;

    public void update(BookReqDto bookReqDto) {
        this.title = bookReqDto.getTitle();
        this.author = bookReqDto.getAuthor();
    }

}
