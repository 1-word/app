package com.numo.wordapp.security.dto;

import lombok.*;


public class TokenDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class response {
        private String grantType;
        private String refreshToken;
        private String accessToken;
        private Long accessTokenExpireDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class request {
        String accessToken;
        String refreshToken;

        @Builder
        public request(String accessToken, String refreshToken){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
