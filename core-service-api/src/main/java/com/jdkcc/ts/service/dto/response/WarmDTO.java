package com.jdkcc.ts.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 温馨话语
 * @author Cherish
 * @version 1.0
 * @date 2017/5/13 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarmDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3829076377334462405L;

    private Long id;

    private Long wxUserId;

    private String title;

    private String content;

    private Integer readSum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date modifiedTime;



}
