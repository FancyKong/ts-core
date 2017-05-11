
package com.jdkcc.ts.service.dto.response.su;

import lombok.Data;

import java.util.List;

@Data
public class SuperUserRoleDTO implements java.io.Serializable {

    private static final long serialVersionUID = -3712969985463314297L;
    String username;

    List<SuperRoleDTO> roles;

}
