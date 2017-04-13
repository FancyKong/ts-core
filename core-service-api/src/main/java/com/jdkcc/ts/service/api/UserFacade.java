/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.api;

import com.jdkcc.ts.service.dto.request.user.UserSaveReq;
import com.jdkcc.ts.service.dto.request.user.UserUpdateReq;
import com.jdkcc.ts.service.dto.response.UserDTO;

/**
 * @author Jiangjiaze
 * @version Id: UserFacade.java, v 0.1 2016/9/28 0028 上午 8:59 FancyKong Exp $$
 */
 interface UserFacade {

    UserDTO findByUsername(String username);

    boolean exist(String username);

    Long count();

    void delete(Long id);

    void update(UserUpdateReq userUpdateReq);

    void insert(UserSaveReq userSaveReq);

    //TODO how?
//    Page<UserDTO> findAll(UserSearchReq userSearchReq, BasicSearchReq basicSearchReq);

}
