package com.numo.wordapp.security.oauth2.info;

import java.util.Map;

public class KaKaoServiceInfo implements OAuth2ServiceInfo {
    @Override
    public OAuth2UserInfo getUserInfo(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        String socialId = String.valueOf(attributes.get("id"));
        String nickname = String.valueOf(properties.get("nickname"));
        String email = String.valueOf(account.get("email"));

        return OAuth2UserInfo.builder()
                .socialId(socialId)
                .email(email)
                .nickname(nickname)
                .clientName("kakao")
                .build();
    }

}
