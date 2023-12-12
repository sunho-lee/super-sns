package com.example.supersns.follower;

import com.example.supersns.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Long>, FollowerCustomRepository {

    void deleteByFromAndTo(User from, User to);

}