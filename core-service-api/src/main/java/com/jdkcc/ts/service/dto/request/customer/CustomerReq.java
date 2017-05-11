package com.jdkcc.ts.service.dto.request.customer;

import lombok.Data;

import java.util.Date;

/**
 * Created by Cherish on 2017/2/17.
 */
@Data
public class CustomerReq implements java.io.Serializable {

    private static final long serialVersionUID = 8516268630403908505L;

    private Long id;

    private String nickname;

    private String telephone;

    private String password;

    private Date createdTime;

    private Date modifiedTime;

    private Integer active;

}
