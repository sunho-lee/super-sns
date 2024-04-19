package com.example.supersns.user;

import com.example.supersns.user.dto.SignUpRequest;
import com.example.supersns.user.dto.UserRequest;
import com.example.supersns.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest req) {
        User user = userMapper.signUpRequestToUser(req);
        User savedUser = userService.saveUser(user);
        UserResponse res = userMapper.userToUserResponse(savedUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(res.id()).toUri();

        return ResponseEntity.created(location).body(res);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        User user = userService.findUser(userId);
        UserResponse res = userMapper.userToUserResponse(user);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> replaceUser(@PathVariable Long userId, @Valid @RequestBody UserRequest req) {
        User userReq = userMapper.userRequestToUser(req);
        User updatedUser = userService.updateMyUserDetails(userId, userReq);
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}
