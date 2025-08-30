package com.rakshi.bank.domains.dto.response;

import com.rakshi.bank.domains.enums.Roles;

public record RoleResponse(
        String roleId,
        Roles name
) {
}