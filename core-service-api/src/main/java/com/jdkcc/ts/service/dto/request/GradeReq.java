package com.jdkcc.ts.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员评分
 * @author Cherish
 * @version 1.0
 * @date 2017/5/13 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeReq implements java.io.Serializable {

    private static final long serialVersionUID = -3040804949502120239L;
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


}
