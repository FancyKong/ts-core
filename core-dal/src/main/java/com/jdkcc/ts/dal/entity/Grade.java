package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 会员心理评分
 * @author Cherish
 * @version 1.0
 * @date 2017/5/13 20:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_grade")
public class Grade implements java.io.Serializable {

    private static final long serialVersionUID = 8244295477816260842L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "wx_user_id", nullable = false)
    private Long wxUserId;
    /**
     * 问题内容
     */
    @Column(name = "content", columnDefinition = "mediumtext ")
    private String content;
    /**
     * 分数
     */
    @Column(name = "score", nullable = false, columnDefinition = "int default 0 ")
    private Integer score;
    /**
     * 建议的内容
     */
    @Column(name = "suggest", columnDefinition = "mediumtext ")
    private String suggest;
    /**
     * 测试时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, length = 19)
    private Date createdTime;


}
