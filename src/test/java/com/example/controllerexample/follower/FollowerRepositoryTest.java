package com.example.controllerexample.follower;

import com.example.controllerexample.config.TestQueryDslConfig;
import com.example.controllerexample.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@Sql({"/role.sql", "/userWithFollower.sql" })
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
        Pageable pageable = Pageable.ofSize(5);

        Slice<User> users = followerRepository.getUserFollowers(1L, pageable);

        List<User> userList = users.getContent();

        assertThat(userList.size()).isEqualTo(5);
        assertThat(userList.get(0).getId()).isEqualTo(2);
        assertThat(userList.get(1).getId()).isEqualTo(3);
        assertThat(userList.get(2).getId()).isEqualTo(4);
        assertThat(userList.get(3).getId()).isEqualTo(5);
        assertThat(userList.get(4).getId()).isEqualTo(6);
    }

    @Test
    @DisplayName("내가 팔로우한 유저 목록 가져오기")
    void getMyFollowees(){
        Pageable pageable = Pageable.ofSize(5);

        Slice<User> users = followerRepository.getUserFollowees(10L, pageable);

        List<User> userList = users.getContent();

        assertThat(userList.size()).isEqualTo(3);
        assertThat(userList.get(0).getId()).isEqualTo(1);
        assertThat(userList.get(1).getId()).isEqualTo(2);
        assertThat(userList.get(2).getId()).isEqualTo(5);
    }

}
