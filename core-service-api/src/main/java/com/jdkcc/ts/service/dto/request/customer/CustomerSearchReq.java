package com.jdkcc.ts.service.dto.request.customer;

import lombok.Data;

@Data
public class CustomerSearchReq implements java.io.Serializable {

    private static final long serialVersionUID = -6252802038931388533L;

    private String nickname;

    private String telephone;

    private Integer active;


}
