package com.ssafy.enjoytrip.features.tripost.domain;

import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TripostLike {
    TripostId tripostId;
    Uid uid;
}
