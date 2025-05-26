package com.ssafy.enjoytrip.tripost;

import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.TripostController;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.SearchHotTripostRequest;
import com.ssafy.enjoytrip.features.tripost.adapter.in.web.tripost.request.SearchLatestTripostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TripostTest {
    @Autowired
    TripostController tripostController;

    @Test
    public void 최신_게시글_조회() {
        var result = tripostController.getLatestTriposts(new SearchLatestTripostRequest(4));
        System.out.println(result);
    }

    @Test
    void 인기_게시글_조회() {
        var result = tripostController.getHotTriposts(new SearchHotTripostRequest(3));
        System.out.println(result);
    }
}
