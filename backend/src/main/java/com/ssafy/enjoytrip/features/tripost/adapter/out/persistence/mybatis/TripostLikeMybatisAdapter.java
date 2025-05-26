package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.TripostLikeDao;
import com.ssafy.enjoytrip.features.tripost.application.port.out.*;
import com.ssafy.enjoytrip.features.tripost.domain.TripostId;
import com.ssafy.enjoytrip.features.tripost.domain.TripostLike;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripostLikeMybatisAdapter implements
        TripostLikePort {
    private final TripostLikeDao tripostLikeDao;

    @Override
    public int createAll(List<TripostLike> tripostLikes) {
        int updated = 0;
        if (!tripostLikes.isEmpty()) {
            updated = tripostLikeDao.bulkInsert(tripostLikes);
        }
        return updated;
    }

    @Override
    public int deleteAll(List<TripostLike> tripostLikes) {
        int deleted = 0;
        if (!tripostLikes.isEmpty()) {
            deleted = tripostLikeDao.bulkDelete(tripostLikes);
        }
        return deleted;
    }

    @Override
    public boolean check(TripostId tripostId, Uid uid) {
        boolean liked = tripostLikeDao.check(tripostId, uid);
        return liked;
    }

    @Override
    public long countByTripostId(TripostId tripostId) {
        long count = tripostLikeDao.countByTripostId(tripostId);
        return count;
    }
}
