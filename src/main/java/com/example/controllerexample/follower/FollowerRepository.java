package com.example.controllerexample.follower;

import com.example.controllerexample.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Long>, FollowerCustomRepository {

    void deleteByFromAndTo(User from, User to);

}