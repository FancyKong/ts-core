package com.jdkcc.ts.service.dto.request.wxuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxUserUpdateReq implements java.io.Serializable {

    private static final long serialVersionUID = -4788656471459908692L;

    private Long id;
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


}
