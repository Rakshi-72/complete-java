package com.rakshi.bank.userservice.service.userService;

import com.rakshi.bank.domains.dto.response.UserResponse;

public interface UserService {

    UserResponse findByIdentity(String identity);

}
