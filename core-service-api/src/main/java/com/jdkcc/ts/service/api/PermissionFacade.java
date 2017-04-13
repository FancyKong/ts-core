/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.api;

import com.jdkcc.ts.service.dto.request.permission.PermissionSaveReq;
import com.jdkcc.ts.service.dto.request.permission.PermissionUpdateReq;
import com.jdkcc.ts.service.dto.response.PermissionDTO;

import java.util.List;

/**
 * @author Jiangjiaze
 * @version Id: UserFacade.java, v 0.1 2016/9/28 0028 上午 8:59 FancyKong Exp $$
 */
interface PermissionFacade {

    //Page<PermissionDTO> findAll(BasicSearchReq basicSearchReq);

    void update(PermissionUpdateReq permissionUpdateReq);

    boolean exist(String permit);

    void insert(PermissionSaveReq permissionSaveReq);

    List<PermissionDTO> listByRoleId(Long roleId);

}
