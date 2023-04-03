package com.example.junittestpractice.web.dto.res;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CMResDto<T> {
    private Integer code; // 1 성공, -1 실패
    private String msg;
    private T body;
}
