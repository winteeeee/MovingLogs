package com.ssafy.enjoytrip.features.auth.adapter.out;

import com.ssafy.enjoytrip.features.auth.application.port.out.SaveRefreshTokenPort;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import org.springframework.stereotype.Component;

@Component
public class TempAdapter implements SaveRefreshTokenPort {
    @Override
    public void saveRefreshToken(Uid uid, String refreshToken) {

    }
}
