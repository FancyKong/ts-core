package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 温馨话语
 * @author Cherish
 * @version 1.0
 * @date 2017/5/13 16:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_warm")
public class Warm implements java.io.Serializable {

    private static final long serialVersionUID = 4174107925999053672L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "wx_user_id", nullable = false)
    private Long wxUserId;

    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @Column(name = "content", nullable=false, columnDefinition = "mediumtext ")
    private String content;

    @Column(name = "read_sum", nullable = false, columnDefinition = "int default 0 ")
    private Integer readSum;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, length = 19)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time", nullable = false, length = 19)
    private Date modifiedTime;



}
