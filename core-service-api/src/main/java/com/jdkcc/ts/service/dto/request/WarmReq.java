package com.jdkcc.ts.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 温馨话语
 * @author Cherish
 * @version 1.0
 * @date 2017/5/13 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarmReq implements java.io.Serializable {

    private static final long serialVersionUID = -3096551813678230487L;

    private Long id;

    private Long wxUserId;

    private String title;

    private String content;

    private Integer readSum;


}
