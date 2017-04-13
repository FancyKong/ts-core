/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.api;

import com.jdkcc.ts.service.dto.request.su.SuperRolePermissionReq;
import com.jdkcc.ts.service.dto.request.su.SuperUserRoleReq;
import com.jdkcc.ts.service.dto.response.RoleDTO;
import com.jdkcc.ts.service.dto.response.su.SuperRolePermissionDTO;
import com.jdkcc.ts.service.dto.response.su.SuperUserRoleDTO;

import java.util.List;

/**
 * @author Jiangjiaze
 * @version Id: UserFacade.java, v 0.1 2016/9/28 0028 上午 8:59 FancyKong Exp $$
 */
interface SuperFacade {

    List<RoleDTO> listAllRole();

    SuperUserRoleDTO findByUsername(String username);

    void updateUserRole(SuperUserRoleReq superUserRoleReq);

    SuperRolePermissionDTO findByRolename(String rolename);

    void updateRolePermission(SuperRolePermissionReq superRolePermissionReq);

}
