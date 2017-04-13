package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements java.io.Serializable {

	private static final long serialVersionUID = 2285174464789310329L;

    private Long id;

    private String nickname;

    private String telephone;

    private String password;

    private Date createdTime;

    private Date modifiedTime;
    /**
     * 账户是否激活可用
     */
	private Integer active;


}
