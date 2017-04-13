/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.api;

import com.jdkcc.ts.service.dto.request.customer.CustomerReq;
import com.jdkcc.ts.service.dto.response.CustomerDTO;

/**
 * @author Jiangjiaze
 * @version Id: UserFacade.java, v 0.1 2016/9/28 0028 上午 8:59 FancyKong Exp $$
 */
interface CustomerFacade {

    //Page<CustomerDTO> findAll(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq);

    boolean exist(String telephone);

    void delete(Long customerId);

    void update(CustomerReq customerReq);

    CustomerDTO insert(CustomerReq customerReq);

}
