package com.rakshi.bank.domains.utils;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;
import com.rakshi.bank.domains.dto.response.UserResponse;
import com.rakshi.bank.domains.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperUtil {

    User fromRequest(CreateUserRequest createUserRequest);

    UserResponse toResponse(User user);

}
