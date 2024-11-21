package com.numo.wordapp.security.oauth2;

import com.numo.wordapp.dto.user.UserDto;
import com.numo.wordapp.entity.user.Authority;
import com.numo.wordapp.entity.user.Role;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.security.oauth2.info.KaKaoServiceInfo;
import com.numo.wordapp.security.oauth2.info.OAuth2ServiceInfo;
import com.numo.wordapp.security.oauth2.info.OAuth2UserInfo;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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

        UserDto userDto = userService.saveOrUpdate(oAuth2UserInfo);
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
