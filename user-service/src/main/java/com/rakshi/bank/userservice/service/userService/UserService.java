package com.rakshi.bank.userservice.service.userService;

import com.rakshi.bank.domains.dto.response.UserResponse;
import com.rakshi.bank.userservice.dtos.DeactivateAccountResponse;

public interface UserService {

    UserResponse findByIdentity(String identity);

    UserResponse getCurrentlyLoggedInUser();

    DeactivateAccountResponse deActivateCurrentUserAccount();
}
