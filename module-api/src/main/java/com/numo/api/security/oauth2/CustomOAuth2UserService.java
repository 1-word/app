package com.numo.api.security.oauth2;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.domain.user.dto.UserDto;
import com.numo.api.security.oauth2.info.*;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.domain.user.service.UserService;
import com.numo.domain.user.Authority;
import com.numo.domain.user.Role;
import com.numo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        // OAuth2 정보를 가져온다.
        OAuth2User oAuth2User = service.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String serviceName = userRequest.getClientRegistration().getClientName();
        OAuth2ServiceInfo oAuth2ServiceInfo = getOAuth2UserInfo(serviceName);
        OAuth2UserInfo oAuth2UserInfo = oAuth2ServiceInfo.getUserInfo(attributes);

        UserDto userDto = null;

        try {
            userDto = userService.saveOrUpdate(oAuth2UserInfo);
        } catch (CustomException e) {
           OAuth2Error oauth2Error = new OAuth2Error(String.valueOf(e.getErrorCode().getCode()), e.getErrorCode().getDescription() , serviceName);
           throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), e);
        }

        List<String> authorities = userDto.authorities();

        User user = User.builder()
                .userId(userDto.userId())
                .email(userDto.email())
                .authorities(getAuthorities(authorities))
                .build();

        return new UserDetailsImpl(user, attributes);
    }

    private OAuth2ServiceInfo getOAuth2UserInfo(String serviceName) {
        return switch (serviceName) {
            case "kakao" -> new KaKaoServiceInfo();
            case "naver" -> new NaverServiceInfo();
            case "google" -> new GoogleServiceInfo();
            default -> throw new IllegalStateException("Unexpected value: " + serviceName);
        };
    }

    private Set<Authority> getAuthorities(List<String> authorities) {
        Set<Authority> result = new HashSet<>();
        for (String authName: authorities) {
            Authority authority = Authority.builder()
                    .name(Role.valueOf(authName))
                    .build();
            result.add(authority);
        }
        return result;
    }

}
