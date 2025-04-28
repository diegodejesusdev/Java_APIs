package com.dfactory.TodoList_API.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }
}
