package com.ssafy.enjoytrip.features.attraction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Attraction {
    private AttractionId id;
    private ContentId contentId;
    private String title;
    private ContentTypeId contentTypeId;
    //이 속성은 조인의 결과가 아니라면 null
    private String contentTypeName;
    private SidoId areaCode;
    //이 속성은 조인의 결과가 아니라면 null
    private String areaName;
    private GugunId siGunGuCode;
    //이 속성은 조인의 결과가 아니라면 null
    private String siGunGuName;
    private String firstImage1;
    private String firstImage2;
    private Long mapLevel;
    private Double latitude;
    private Double longitude;
    private String tel;
    private String addr1;
    private String addr2;
    private String homepage;
    private String overview;

    @Override
    public String toString() {
        return "Attraction[" +
                "id=" + id.getId() +
                ", title='" + title + '\'' +
                ", firstImage1='" + firstImage1 + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ']';
    }
}
