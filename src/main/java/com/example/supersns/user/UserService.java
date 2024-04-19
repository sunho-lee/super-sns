package com.example.supersns.user;

import org.springframework.stereotype.Service;


@Service
public class UserService {

    final private UserRepository userRepository;

    public UserService(UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        userRepository.findUserByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new UserAlreadyExistException(u.getUsername());
                });
        return userRepository.save(user);
    }


    public User findUser(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User updateMyUserDetails(Long userId, User user) {
        if (!userId.equals(user.getId())) {
            throw new UserInvalidException(userId);
        }
        return userRepository.findUserById(userId)
                .map(u -> {
                    u.setNickname(user.getNickname());
                    u.setPassword(user.getPassword());
                    return userRepository.save(u);
                })
                .orElseThrow(() -> new UserNotFoundException(user.getId()));
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
    }
}
