package com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.AttractionDao;
import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.ContentTypeDao;
import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.GugunDao;
import com.ssafy.enjoytrip.features.attraction.adapter.out.persistence.mybatis.dao.SidoDao;
import com.ssafy.enjoytrip.features.attraction.application.port.out.*;
import com.ssafy.enjoytrip.features.attraction.domain.Attraction;
import com.ssafy.enjoytrip.features.attraction.domain.ContentType;
import com.ssafy.enjoytrip.features.attraction.domain.Gugun;
import com.ssafy.enjoytrip.features.attraction.domain.Sido;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AttractionMyBatisAdapter implements SearchAttractionPort, CountAttractionPort, SearchContentTypePort, SearchGugunPort, SearchSidoPort {
    @Value("${paging.size}")
    private Integer pageSize;
    private final AttractionDao attractionDao;
    private final ContentTypeDao contentTypeDao;
    private final GugunDao gugunDao;
    private final SidoDao sidoDao;

    @Override
    public List<Attraction> searchAttractionsWithPaging(Long contentTypeId, Long areaCode, Long siGunGuCode, Integer page) {
        return attractionDao.findByContentByIdAndAreaCodeAndSiGunGuCode(contentTypeId, areaCode, siGunGuCode, pageSize, (long) (page - 1) * pageSize);
    }

    @Override
    public List<Attraction> searchAttractionsWithLimit(Long contentTypeId, Long areaCode, Long siGunGuCode, Long limit) {
        return attractionDao.findByContentByIdAndAreaCodeAndSiGunGuCodeLimit(contentTypeId, areaCode, siGunGuCode, limit);
    }

    @Override
    public Long countAttractions(Long contentTypeId, Long areaCode, Long siGunGuCode) {
        return attractionDao.countByContentByIdAndAreaCodeAndSiGunGuCode(contentTypeId, areaCode, siGunGuCode);
    }

    @Override
    public List<ContentType> searchContentTypes() {
        return contentTypeDao.findAll();
    }

    @Override
    public List<Gugun> searchGuguns(Long sidoCode) {
        return gugunDao.findBySidoCode(sidoCode);
    }

    @Override
    public List<Sido> searchSidos() {
        return sidoDao.findAll();
    }
}
