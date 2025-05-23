package com.ssafy.enjoytrip.features.image.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.image.adapter.out.persistence.mybatis.dao.ImageDao;
import com.ssafy.enjoytrip.features.image.application.port.out.ImagePort;
import com.ssafy.enjoytrip.features.image.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageMybatisAdapter implements
        ImagePort {
    private final ImageDao imageDao;

    @Override
    public int create(Image image) {
        int created = imageDao.create(image);
        return created;
    }
}
