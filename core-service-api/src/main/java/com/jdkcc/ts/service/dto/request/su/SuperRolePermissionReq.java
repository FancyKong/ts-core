package com.jdkcc.ts.service.dto.request.su;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Cherish on 2017/2/9.
 */
@Data
public class SuperRolePermissionReq implements java.io.Serializable {

    private static final long serialVersionUID = 4048023739204872347L;

    @Length(min = 1,max = 16, message = "1-16个字符")
    String rolename;;

    @NotNull(message = "错误的ID")
    List<Long> permissionIds;


}
