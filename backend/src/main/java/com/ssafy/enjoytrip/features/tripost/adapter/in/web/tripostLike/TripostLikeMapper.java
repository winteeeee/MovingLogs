package com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostLike;

import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripostLike.response.CheckTripostLikedResponse;
import com.ssafy.enjoytrip.features.tripost.application.port.in.TripostLikeUseCase;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.user.domain.Uid;

public class TripostLikeMapper {
    public static TripostLikeUseCase.Command toTripostLikeCommand(String tripostId, String uid) {
        return TripostLikeUseCase.Command.builder()
                .tripostId(new TripostId(tripostId))
                .uid(new Uid(uid))
                .build();
    }

    public static CheckTripostLikedResponse toCheckTripostLikedResponse(boolean liked) {
        return CheckTripostLikedResponse.builder()
                .liked(liked)
                .build();
    }
}
