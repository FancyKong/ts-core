/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.dto.request;

import com.jdkcc.ts.common.util.PatternReg;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author Jiangjiaze
 * @version Id: UserCreateReqDto.java, v 0.1 2016/9/27 0027 下午 4:01 FancyKong Exp $$
 */
@Data
public class UserCreateReqDto {

    /**
     * 微信openid
     */
    private String openid;
    /**
     * 昵称
     */
    @NotBlank
    private String nickname;
    /**
     * 城市
     */
    private String city;
    /**
     * 关注时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd",iso = DateTimeFormat.ISO.DATE)
    private Date subscribeTime;
    /**
     * 头像url
     */
    private String headimgurl;
    /**
     * 性别
     */
    @Pattern(regexp = PatternReg.GENDER, message = "认证方式不存在")
    private String gender;
}
