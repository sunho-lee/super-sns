package com.example.supersns.auth;

import com.example.supersns.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    final private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .map(u -> {
                    CustomUserDetails userDetails = new CustomUserDetails(
                            u.getId(), u.getUsername(), u.getNickname(), u.getPassword());
                    u.getRoles().forEach(userDetails::addRole);
                    return userDetails;
                })
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
