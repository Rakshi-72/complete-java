package com.rakshi.bank.authservice.service;

import com.rakshi.bank.domains.models.User;

public interface UserService {
    User findUserByIdentifier(String identifier);
}
