package com.ssafy.enjoytrip.attraction;

import com.ssafy.enjoytrip.common.dto.PageDto;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.AttractionController;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.AttractionResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.ContentTypeResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.GugunResponse;
import com.ssafy.enjoytrip.features.attraction.adapter.in.web.response.SidoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest
public class AttractionTest {
    @Autowired
    AttractionController attractionController;

    @Test
    void 관광지_컨텐츠_타입_조회() {
        ResponseEntity<List<ContentTypeResponse>> response = attractionController.findContentTypes();
        System.out.println(response);
    }

    @Test
    void 관광지_지역_조회() {
        ResponseEntity<List<SidoResponse>> response = attractionController.findSidos();
        System.out.println(response);
    }

    @Test
    void 관광지_구군_조회() {
        ResponseEntity<List<GugunResponse>> response = attractionController.findGuguns(7L);
        System.out.println(response);
    }

    @Test
    void 관광지_조회() {
        ResponseEntity<PageDto<AttractionResponse>> response = attractionController.findAttractions(
                12L,
                7L,
                1L,
                "",
                1,
                5
        );
        System.out.println(response);
    }
}
