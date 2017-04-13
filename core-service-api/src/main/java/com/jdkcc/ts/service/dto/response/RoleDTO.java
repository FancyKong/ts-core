package com.jdkcc.ts.service.dto.response;

import lombok.Data;

@Data
public class RoleDTO implements java.io.Serializable{

    private static final long serialVersionUID = -4798776753955957049L;

    private Long id;

    private String name;

    private String description;

}
