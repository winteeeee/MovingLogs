package com.ssafy.enjoytrip.features.auth.application.port.out;

import com.ssafy.enjoytrip.features.user.domain.Uid;

public interface SaveRefreshTokenPort {
    void saveRefreshToken(Uid uid, String refreshToken);
}
