package com.jdkcc.ts.service.dto.response.su;

import lombok.Data;

import java.util.List;

@Data
public class SuperRolePermissionDTO implements java.io.Serializable {

    private static final long serialVersionUID = 4048023739204872347L;
    String rolename;
    List<SuperPermissionDTO> permissions;

}
