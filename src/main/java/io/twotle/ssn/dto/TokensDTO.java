package io.twotle.ssn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokensDTO {
    private String access;
    private String refresh;

    public TokensDTO(String accessToken, String refreshToken) {
        this.access = accessToken;
        this.refresh = refreshToken;
    }
}
