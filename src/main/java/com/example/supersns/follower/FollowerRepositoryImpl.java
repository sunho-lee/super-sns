package com.example.supersns.follower;

import com.example.supersns.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.supersns.follower.QFollower.follower;
import static com.example.supersns.user.QUser.user;

public class FollowerRepositoryImpl implements FollowerCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FollowerRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<User> getUserFollowers(Long userId) {
        return queryFactory
                .select(user)
                .from(user)
                .join(user.followers, follower)
                .where(follower.from.id.eq(userId))
                .fetch();
    }

    @Override
    public List<User> getUserFollowees(Long userId) {
        return queryFactory
                .select(user)
                .from(user)
                .join(user.followees, follower)
                .where(follower.to.id.eq(userId))
                .fetch();
    }
}
