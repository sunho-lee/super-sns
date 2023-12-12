package com.example.supersns.follower;

import com.example.supersns.config.TestQueryDslConfig;
import com.example.supersns.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@DataJpaTest
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FollowerRepositoryTest {

    @Autowired
    private FollowerRepository followerRepository;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer =
            new MySQLContainer<>("mysql:8.0.35");

    static {
        mySQLContainer.start();
    }

    @Test
    @DisplayName("유저 팔로워 가져오기")
    public void getFollowerTest() {

        List<User> userList = followerRepository.getUserFollowers(1L );


    }

    @Test
    @DisplayName("내가 팔로우한 유저 목록 가져오기")
    void getMyFollowees(){
        List<User> userList = followerRepository.getUserFollowees(10L );

    }

}
