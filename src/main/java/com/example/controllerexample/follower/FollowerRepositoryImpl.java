package com.example.controllerexample.follower;

import com.example.controllerexample.user.User;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.controllerexample.follower.QFollower.follower;
import static com.example.controllerexample.user.QUser.user;

public class FollowerRepositoryImpl implements CustomFollowerRepository {

    private final JPAQueryFactory queryFactory;

    public FollowerRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Slice<User> getUserFollowers(Long userId, Pageable pageable) {
        List<User> content = queryFactory
                .select(user)
                .from(user)
                .join(user.followers, follower)
                .where(follower.from.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<User> getUserFollowees(Long userId, Pageable pageable) {
        List<User> content = queryFactory
                .select(user)
                .from(user)
                .join(user.followees, follower)
                .where(follower.to.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
