package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxUser implements java.io.Serializable {

	private static final long serialVersionUID = 5654579397240647401L;

	private Long id;
    /**
     * 微信openid
     */
	private String openid;
    /**
     * 昵称
     */
	private String nickname;
    /**
     * 性别
     */
	private Short sex;

    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 城市
     */
	private String city;
    /**
     * 关注时间
     */
	private Date subscribeTime;
    /**
     * 头像链接
     */
	private String headimgurl;



}
