package com.example.junittestpractice.web;

import com.example.junittestpractice.service.BookService;
import com.example.junittestpractice.web.dto.req.BookReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

// 통합 테스트 (Controller, Service, Repository 전체)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookApiControllerTest {

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper objectMapper;
    private static HttpHeaders headers;

    @BeforeAll
    void init() {
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void diTest() {
        assertNotNull(rt);
    }

    @Test
    void createBook() throws JsonProcessingException {
        /**
         * Client가 요청을 하게 되면, Springboot에서는 DispatcherServlet이 먼저 그 요청을 받게 된다. <br>
         * 이 후, 요청한 주소(url)에 따라 Controller를 호출하고, Body값이 있으면 그 값을 Controller가 받을 수 있는 형태로 변환해준다. <br>
         * ex) json 요청이 오게 되면 -> json 데이터를 Controller가 받는 RequestBody 클래스 형태로 만들어준다.
         * -> 따라서 우리가 통합 테스트를 하게 되면, json으로 데이터를 주는 것부터 시작해야 한다.
         */

        // 1) Client가 보낼 Request를 Json 데이터로 만들기
        // given
        BookReqDto bookReqDto = new BookReqDto();
        bookReqDto.setTitle("제목1");
        bookReqDto.setAuthor("작가1");

        String body = objectMapper.writeValueAsString(bookReqDto);

        // 2) Json 데이터를 Http 요청으로 만들어서 보내서 응답값 받기
        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity =
                rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);
        System.out.println("=========================================");
        System.out.println(responseEntity.getBody());

        // 3) 응답 받은 Json 값을 parsing 하여 검증하기
        // then
        DocumentContext dc = JsonPath.parse(responseEntity.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("제목1");
        assertThat(author).isEqualTo("작가1");
    }

    @Test
    void getAllBook() {
    }

    @Test
    void getBook() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void updateBook() {
    }
}