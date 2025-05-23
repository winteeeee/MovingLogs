package com.ssafy.enjoytrip.features.image.adapter.out.persistance.mybatis;

import com.ssafy.enjoytrip.features.image.adapter.out.persistance.mybatis.dao.ImageDao;
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
    public Image save(Image image) {
        imageDao.upsert(image);
        return image;
    }
}
