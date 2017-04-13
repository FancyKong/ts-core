package com.jdkcc.ts.service.dto.response;

import lombok.Data;

@Data
public class PermissionDTO implements java.io.Serializable{

    private static final long serialVersionUID = -1801100759054480063L;
    private Long id;

    private String permit;

    private String description;

}
