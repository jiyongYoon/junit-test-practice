package com.example.junittestpractice.web;

import com.example.junittestpractice.domain.Book;
import com.example.junittestpractice.domain.BookRepository;
import com.example.junittestpractice.web.dto.req.BookReqDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

// 통합 테스트 (Controller, Service, Repository 전체)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
class BookApiControllerTest {

    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private BookRepository bookRepository;

    private static ObjectMapper objectMapper;
    private static HttpHeaders headers;
    String TITLE = "제목1";
    String AUTHOR = "작가1";

    @BeforeAll
    void init() {
        objectMapper = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    public void givenBook() {
        Book book = Book.builder()
                .title(TITLE)
                .author(AUTHOR)
                .build();
        bookRepository.save(book);
    }

    @Test
    @Order(1)
    void diTest() {
        assertNotNull(rt);
    }

    @Test
    @Order(2)
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
        bookReqDto.setTitle(TITLE);
        bookReqDto.setAuthor(AUTHOR);

        String body = objectMapper.writeValueAsString(bookReqDto);

        // 2) Json 데이터를 Http 요청으로 만들어서 보내서 응답값 받기
        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity =
                rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // 3) 응답 받은 Json 값을 parsing 하여 검증하기
        // then
        DocumentContext dc = JsonPath.parse(responseEntity.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo(TITLE);
        assertThat(author).isEqualTo(AUTHOR);
    }

    @Test
    @Order(3)
    void getAllBook() {
        // given

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity =
                rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(responseEntity.getBody());
        int code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");
        String author = dc.read("$.body.items[0].author");
        Object read = dc.read("$.body.items");
        int size = ((List<?>) read).size();

        assertThat(code).isEqualTo(1);
        assertThat(size).isEqualTo(4);
        assertThat(title).isEqualTo(TITLE);
        assertThat(author).isEqualTo(AUTHOR);
    }

    @Test
    @Order(4)
    void getBook() {
        // given
        Long id = 1L;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity =
                rt.exchange("/api/v1/book/" + id, HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(responseEntity.getBody());
        int code = dc.read("$.code");
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo(TITLE);
        assertThat(author).isEqualTo(AUTHOR);
    }

    @Test
    @Order(6)
    void deleteBook() {
        // given
        Long id = 1L;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity =
                rt.exchange("/api/v1/book/" + id, HttpMethod.DELETE, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(responseEntity.getBody());
        int code = dc.read("$.code");
        String msg = dc.read("$.msg");
        Object body = dc.read("$.body");

        assertThat(code).isEqualTo(1);
        assertThat(msg).isEqualTo("삭제 성공");
        assertThat(body.toString()).isEqualTo(id.toString());
    }

    @Test
    @Order(5)
    void updateBook() throws JsonProcessingException {
        // given
        Long id = 1L;

        BookReqDto bookReqDto = new BookReqDto();
        bookReqDto.setTitle(TITLE + "수정");
        bookReqDto.setAuthor(AUTHOR + "수정");

        String body = objectMapper.writeValueAsString(bookReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity =
                rt.exchange("/api/v1/book/" + id, HttpMethod.PUT, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(responseEntity.getBody());
        int code = dc.read("$.code");
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo(bookReqDto.getTitle());
        assertThat(author).isEqualTo(bookReqDto.getAuthor());
    }
}