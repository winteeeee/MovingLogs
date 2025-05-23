package com.ssafy.enjoytrip.features.auth.adapter.in.kakao;


import com.ssafy.enjoytrip.features.auth.application.port.in.CreateOAuth2UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoOAuthAdapter extends DefaultOAuth2UserService {
    private final CreateOAuth2UserUseCase createOAuth2UserUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //카카오로부터 인증 정보를 수신
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String kakaoId = attributes.get("id").toString();
        String name = ((LinkedHashMap<?, ?>) attributes.get("properties")).get("nickname").toString();
        //서비스 호출
        createOAuth2UserUseCase.createOAuth2User(KakaoOAuthMapper.toCreateOAuth2UserUseCaseCommand(kakaoId, name));
        return oAuth2User;
    }
}
