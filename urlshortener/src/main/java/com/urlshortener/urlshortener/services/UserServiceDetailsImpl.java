package com.urlshortener.urlshortener.services;


import com.urlshortener.urlshortener.models.Users;
import com.urlshortener.urlshortener.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserServiceDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Users> user = userRepository.findByMail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDetails ud = new User(user.get(0).getEmail(), user.get(0).getPassword(),emptyList());
        return ud;
    }
}

