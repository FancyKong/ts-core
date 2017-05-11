/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.component.kafka.process;

import com.jdkcc.ts.dal.entity.User;
import com.jdkcc.ts.service.component.kafka.common.BussinessProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jiangjiaze
 * @version Id: UserProcess.java, v 0.1 2016/10/15 1:28 FancyKong Exp $$
 */
@Slf4j
//@Component
public class UserProcess extends BussinessProcess<User>{

    @Override
    public void doBussiness(User user) {
        log.info("doing jUser process:{}",user);
    }
}
