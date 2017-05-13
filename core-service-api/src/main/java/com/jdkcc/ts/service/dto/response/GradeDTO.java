package com.jdkcc.ts.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 会员评分
 * @author Cherish
 * @version 1.0
 * @date 2017/5/13 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO implements java.io.Serializable {

    private static final long serialVersionUID = -6393775491544886981L;
    private Long id;

    private Long wxUserId;
    /**
     * 问题内容
     */
    private String content;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 建议的内容
     */
    private String suggest;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="UTC")
    private Date createdTime;


}
