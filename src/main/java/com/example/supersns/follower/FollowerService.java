package com.example.supersns.follower;

import com.example.supersns.user.User;
import com.example.supersns.user.UserInvalidException;
import com.example.supersns.user.UserNotFoundException;
import com.example.supersns.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


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
     * @param myId   기준이 되는 유저
     * @param userId 팔로우할 유저의 아이디
     */
    @Transactional
    public void followUser(Long myId, Long userId) {
        if (myId.equals(userId)) {
            throw new UserInvalidException(userId);
        }
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User me = userRepository.findUserById(myId).orElseThrow(() -> new UserNotFoundException(myId));
        Follower follower = new Follower(me, user);
        followerRepository.save(follower);
    }

    /**
     * Un follow user.
     *
     * @param myId   기준이 되는 유저
     * @param userId 팔로우 해제할 유저의 아이디
     */
    @Transactional
    public void unFollowUser(Long myId, Long userId) {
        if (myId.equals(userId)) {
            throw new UserInvalidException(userId);
        }
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User me = userRepository.findUserById(myId).orElseThrow(() -> new UserNotFoundException(myId));

        followerRepository.deleteByFromAndTo(me, user);
    }

    /**
     * Gets user's followers.
     *
     * @param userId 기준이 되는 유저의 id
     * @return user가 팔로우한 유저 목록
     */
    public List<User> getUserFollowers(Long userId) {
        return followerRepository.getUserFollowers(userId);
    }


    /**
     * Gets my followees.
     *
     * @param userId 기준이 되는 유저의 id
     * @return user를 팔로우한 유저의 목록
     */
    public List<User> getUserFollowees(Long userId) {
        return followerRepository.getUserFollowees(userId);
    }

}
