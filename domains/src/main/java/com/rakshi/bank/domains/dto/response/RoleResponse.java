package com.rakshi.bank.domains.dto.response;

import com.rakshi.bank.domains.enums.Roles;

import java.io.Serializable;

public record RoleResponse(
        String roleId,
        Roles role
) implements Serializable {
}