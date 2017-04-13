package com.jdkcc.ts.service.dto.response.su;

import lombok.Data;

@Data
public class SuperPermissionDTO implements java.io.Serializable {

    private static final long serialVersionUID = 8048511489976115684L;
    private Long id;
    private String permit;
    private boolean have;

}
