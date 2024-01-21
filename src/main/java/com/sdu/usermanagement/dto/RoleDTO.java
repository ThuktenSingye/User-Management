package com.sdu.usermanagement.dto;

import com.sdu.usermanagement.model.Role.RoleName;


import lombok.Data;

@Data
public class RoleDTO {

    private Integer roleId;
    private RoleName roleName;
}
