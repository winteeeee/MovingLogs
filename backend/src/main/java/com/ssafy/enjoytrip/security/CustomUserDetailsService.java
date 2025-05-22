package com.ssafy.enjoytrip.security;

import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final SearchUserUseCase searchUserUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SearchUserUseCase.Result result = searchUserUseCase.searchUser(UserSecurityMapper.toSearchUserCommand(username));
        return new CustomUserDetails(UserSecurityMapper.toUser(result));
    }
}
