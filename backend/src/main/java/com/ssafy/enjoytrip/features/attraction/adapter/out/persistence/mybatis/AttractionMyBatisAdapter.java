package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.AttractionDao;
import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.ContentTypeDao;
import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.GugunDao;
import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.SidoDao;
import com.ssafy.enjoytrip.features.attraction.application.port.out.*;
import com.ssafy.enjoytrip.features.attraction.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AttractionMyBatisAdapter implements
        SearchAttractionPort,
        CountAttractionPort,
        SearchContentTypePort,
        SearchGugunPort,
        SearchSidoPort {
    private final AttractionDao attractionDao;
    private final ContentTypeDao contentTypeDao;
    private final GugunDao gugunDao;
    private final SidoDao sidoDao;

    @Override
    public List<Attraction> searchAttractionsWithPaging(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer page, Integer pageSize, String query) {
        List<Map<String, Object>> resultMaps = attractionDao.findByContentByIdAndAreaCodeAndSiGunGuCode(contentTypeId, areaCode, siGunGuCode, pageSize, (long) (page - 1) * pageSize, query);
        return makeAttractions(resultMaps);
    }

    @Override
    public List<Attraction> searchAttractionsWithLimit(Long contentTypeId, Long areaCode, Long siGunGuCode, Long limit) {
        List<Map<String, Object>> resultMaps = attractionDao.findByContentByIdAndAreaCodeAndSiGunGuCodeLimit(contentTypeId, areaCode, siGunGuCode, limit);
        return makeAttractions(resultMaps);
    }

    @Override
    public List<Attraction> searchAttractionByIds(List<AttractionId> ids) {
        List<String> stringIds = ids.stream().map(AttractionId::getId).toList();
        List<Map<String, Object>> resultMaps = attractionDao.findByIds(stringIds);
        return makeAttractions(resultMaps);
    }

    @Override
    public Long countAttractions(Long contentTypeId, Long areaCode, Long siGunGuCode, String query) {
        return attractionDao.countByContentByIdAndAreaCodeAndSiGunGuCode(contentTypeId, areaCode, siGunGuCode, query);
    }

    @Override
    public List<ContentType> searchContentTypes() {
        List<Map<String, Object>> resultMaps = contentTypeDao.findAll();
        List<ContentType> contentTypeList = new ArrayList<>();
        for (Map<String, Object> resultMap : resultMaps) {
            contentTypeList.add(ContentType.builder()
                            .id(new ContentTypeId((long) (Integer) resultMap.get("content_type_id")))
                            .contentTypeName((String) resultMap.get("content_type_name"))
                    .build());
        }
        return contentTypeList;
    }

    @Override
    public List<Gugun> searchGuguns(Long sidoCode) {
        List<Map<String, Object>> resultMaps = gugunDao.findBySidoCode(sidoCode);
        List<Gugun> guguns = new ArrayList<>();
        for (Map<String, Object> resultMap : resultMaps) {
            guguns.add(Gugun.builder()
                            .id(new GugunId((long) (Integer) resultMap.get("id")))
                            .sidoCode(new SidoId((long) (Integer) resultMap.get("sido_code")))
                            .gugunCode((long) (Integer) resultMap.get("gugun_code"))
                            .gugunName((String) resultMap.get("gugun_name"))
                    .build());
        }

        return guguns;
    }

    @Override
    public List<Sido> searchSidos() {
        List<Map<String, Object>> resultMaps = sidoDao.findAll();
        List<Sido> sidos = new ArrayList<>();
        for (Map<String, Object> resultMap : resultMaps) {
            sidos.add(Sido.builder()
                            .id(new SidoId((long) (Integer) resultMap.get("sido_code")))
                            .sidoName((String) resultMap.get("sido_name"))
                    .build());
        }
        return sidos;
    }

    private List<Attraction> makeAttractions(List<Map<String, Object>> resultMaps) {
        List<Attraction> attractions = new ArrayList<>();
        for (Map<String, Object> resultMap : resultMaps) {
            attractions.add(Attraction.builder()
                    .id(new AttractionId(String.valueOf(resultMap.get("id"))))
                    .contentId(new ContentId((long) (Integer) resultMap.get("content_id")))
                    .title((String) resultMap.get("title"))
                    .contentTypeId(new ContentTypeId((long) (Integer) resultMap.get("content_type_id")))
                    .contentTypeName((String) resultMap.get("content_type_name"))
                    .areaCode(new SidoId((long) (Integer) resultMap.get("sido_code")))
                    .areaName((String) resultMap.get("sido_name"))
                    .siGunGuCode(new GugunId((long) (Integer) resultMap.get("gugun_code")))
                    .siGunGuName((String) resultMap.get("gugun_name"))
                    .firstImage1((String) resultMap.get("first_image1"))
                    .firstImage2((String) resultMap.get("first_image2"))
                    .mapLevel((long) (Integer) resultMap.get("map_level"))
                    .latitude(((BigDecimal) resultMap.get("latitude")).doubleValue())
                    .longitude(((BigDecimal) resultMap.get("longitude")).doubleValue())
                    .tel((String) resultMap.get("tel"))
                    .addr1((String) resultMap.get("addr1"))
                    .addr2((String) resultMap.get("addr2"))
                    .homepage((String) resultMap.get("homepage"))
                    .overview((String) resultMap.get("overview"))
                    .build());
        }

        return attractions;
    }
}
