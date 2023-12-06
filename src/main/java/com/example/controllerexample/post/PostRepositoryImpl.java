package com.example.controllerexample.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.controllerexample.post.QPost.post;


public class PostRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Slice<Post> getUserNewsfeed(Long userId, List<Long> users, Pageable pageable) {

        List<Post> content = queryFactory
                .selectFrom(post)
                .where(post.user.id.in(users)
                ).limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);

    }
}
