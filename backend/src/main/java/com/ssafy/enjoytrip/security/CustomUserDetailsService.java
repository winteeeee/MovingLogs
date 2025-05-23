package com.ssafy.enjoytrip.security;

import com.ssafy.enjoytrip.features.user.application.exception.UserNotFoundException;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.out.SearchUserPort;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import com.ssafy.enjoytrip.features.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final SearchUserPort searchUserPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = searchUserPort.searchUserByUid(new Uid(username)).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
        return new CustomUserDetails(user);
    }
}
