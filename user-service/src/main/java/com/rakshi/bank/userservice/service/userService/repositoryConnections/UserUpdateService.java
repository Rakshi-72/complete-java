package com.rakshi.bank.userservice.service.userService.repositoryConnections;

import com.rakshi.bank.domains.models.User;
import com.rakshi.bank.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserRepository userRepository;

    public boolean updateUser(User user) {
        return saveUser(user) != null;
    }

    @Caching(put = {
            @CachePut(value = "users", key = "#user.getEmail()"),
            @CachePut(value = "users", key = "#user.getPhoneNumber()"),
            @CachePut(value = "users", key = "#user.getUserId()")
    })
    public User saveUser(User user) {
        return userRepository.save(user);
    }


}
