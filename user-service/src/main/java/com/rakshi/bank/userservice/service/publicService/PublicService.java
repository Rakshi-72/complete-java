package com.rakshi.bank.userservice.service.publicService;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;
import com.rakshi.bank.domains.dto.response.UserResponse;

public interface PublicService {

    UserResponse registerUser(CreateUserRequest userRequest);

}
