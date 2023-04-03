package com.example.junittestpractice.web.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookListResDto {
    @JsonProperty(value = "items")
    List<BookResDto> bookResDtoList = new ArrayList<>();

    /** CMResDto 처럼 공통적인 포멧을 가지고 갈 때는 <br>
     * 이런식으로 컬랙션을 반환할때도 Object를 동일하게 반환하도록 해주면 <br>
     * 페이징 처리 할 때 추가적인 정보를 넣을 수 있어서 좋음.
     */
    // int page;
    // boolean first;
    // boolean last;
}
