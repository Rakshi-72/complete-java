package com.rakshi.bank.userservice.service.validations;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;

public interface ValidationStrategy {

    boolean validate(CreateUserRequest input);

}
