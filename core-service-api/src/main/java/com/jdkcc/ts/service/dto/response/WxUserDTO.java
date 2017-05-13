package com.jdkcc.ts.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxUserDTO implements java.io.Serializable {

    private static final long serialVersionUID = -3341000891957731091L;
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
	private Integer sex;
    /**
     * 国家
     */
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
	private Date subscribeTime;
    /**
     * 头像链接
     */
	private String headimgurl;


}
