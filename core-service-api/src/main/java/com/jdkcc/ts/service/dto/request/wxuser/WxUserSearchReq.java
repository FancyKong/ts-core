package com.jdkcc.ts.service.dto.request.wxuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxUserSearchReq implements java.io.Serializable {

    private static final long serialVersionUID = 2625688780316343868L;
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
    @DateTimeFormat(pattern="yyyy-MM-dd",iso = DateTimeFormat.ISO.DATE)
    private Date subscribeTime;


}
