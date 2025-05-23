package com.ssafy.enjoytrip.features.image.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.image.adapter.out.persistence.mybatis.dao.UploaderDao;
import com.ssafy.enjoytrip.features.image.application.port.out.UploaderPort;
import com.ssafy.enjoytrip.features.image.domain.component.Uploader;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UploaderMybatisAdapter implements
        UploaderPort {
    private final UploaderDao uploaderDao;

    @Override
    public Optional<Uploader> getAuthor(Uid uid) {
        Map<String, Object> resultMap = uploaderDao.findById(uid);

        Uploader uploader = new Uploader(new Uid((String) resultMap.get("uid")));
        return Optional.of(uploader);
    }
}
