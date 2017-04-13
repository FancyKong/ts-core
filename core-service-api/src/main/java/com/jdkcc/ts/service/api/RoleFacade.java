/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.api;

import com.jdkcc.ts.service.dto.request.role.RoleSaveReq;
import com.jdkcc.ts.service.dto.request.role.RoleUpdateReq;
import com.jdkcc.ts.service.dto.response.RoleDTO;

import java.util.List;

/**
 * @author Jiangjiaze
 * @version Id: UserFacade.java, v 0.1 2016/9/28 0028 上午 8:59 FancyKong Exp $$
 */
interface RoleFacade {

    // Page<RoleDTO> findAll(BasicSearchReq basicSearchReq);

    void update(RoleUpdateReq roleUpdateReq);

    boolean exist(String name);

    void insert(RoleSaveReq roleSaveReq);

    List<RoleDTO> listByUserId(Long userId);

}
