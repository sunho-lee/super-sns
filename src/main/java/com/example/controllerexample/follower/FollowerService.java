package com.example.controllerexample.follower;

import com.example.controllerexample.user.User;
import com.example.controllerexample.user.UserInvalidException;
import com.example.controllerexample.user.UserNotFoundException;
import com.example.controllerexample.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


@Service
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    public FollowerService(FollowerRepository followerRepository,
                           UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Follow user follower.
     *
     * @param me     기준이 되는 유저
     * @param userId 팔로우할 유저의 아이디
     */
    @Transactional
    public void followUser(User me, Long userId) {
        if (me.getId().equals(userId)) {
            throw new UserInvalidException(userId);
        }
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Follower follower = new Follower(me, user);
        followerRepository.save(follower);
    }

    /**
     * Un follow user.
     *
     * @param me     기준이 되는 유저
     * @param userId 팔로우 해제할 유저의 아이디
     */
    @Transactional
    public void unFollowUser(User me, Long userId) {
        if (me.getId().equals(userId)) {
            throw new UserInvalidException(userId);
        }
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        followerRepository.deleteByFromAndTo(me, user);
    }

    /**
     * Gets user's followers.
     *
     * @param userId   기준이 되는 유저의 id
     * @param pageable the pageable
     * @return user가 팔로우한 유저 목록
     */
    public Slice<User> getUserFollowers(Long userId, Pageable pageable) {
        return followerRepository.getUserFollowers(userId, pageable);
    }



    /**
     * Gets my followees.
     *
     * @param userId   기준이 되는 유저의 id
     * @param pageable the pageable
     * @return user를 팔로우한 유저의 목록
     */
    public Slice<User> getUserFollowees(Long userId, Pageable pageable) {
        return followerRepository.getUserFollowees(userId, pageable);
    }

}
