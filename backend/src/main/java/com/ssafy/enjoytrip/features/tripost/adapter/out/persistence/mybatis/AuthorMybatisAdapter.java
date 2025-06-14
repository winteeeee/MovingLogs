package com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis;

import com.ssafy.enjoytrip.features.tripost.adapter.out.persistence.mybatis.dao.AuthorDao;
import com.ssafy.enjoytrip.features.tripost.application.port.out.AuthorPort;
import com.ssafy.enjoytrip.features.tripost.domain.component.Author;
import com.ssafy.enjoytrip.features.user.domain.Uid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorMybatisAdapter implements AuthorPort {
    private final AuthorDao authorDao;

    @Override
    public Optional<Author> getAuthor(Uid uid) {
        Map<String, Object> resultMap = authorDao.findById(uid);
        Author author = new Author(
                uid,
                (String) resultMap.get("name")
        );
        return Optional.of(author);
    }
}
