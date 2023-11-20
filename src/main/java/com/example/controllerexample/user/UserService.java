package com.example.controllerexample.user;

import com.example.controllerexample.auth.CustomUserDetails;
import com.example.controllerexample.post.Post;
import com.example.controllerexample.post.PostNotFoundException;
import com.example.controllerexample.role.Role;
import com.example.controllerexample.role.RoleNotFoundException;
import com.example.controllerexample.role.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Collections;
import java.util.Optional;


@Service
public class UserService {

    final private UserRepository userRepository;
    final private BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User saveUser(User user) {
        userRepository.findUserByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new UserAlreadyExistException(u.getUsername());
                });

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));
        user.addRole(role);

        return userRepository.save(user);
    }


    public User findUser(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User updateMyUserDetails(Long userId, User user, CustomUserDetails me) {
        if (!userId.equals(me.getId())){
            throw new UserInvalidException(userId);
        }
        return userRepository.findUserById(userId)
                .map(u -> {
                    u.setNickname(user.getNickname());
                    u.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepository.save(u);
                })
                .orElseThrow(() -> new UserNotFoundException(user.getId()));
    }

    public void deleteUser(Long userId, CustomUserDetails me) {
        if (!userId.equals(me.getId())){
            throw new UserInvalidException(userId);
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
    }
}
