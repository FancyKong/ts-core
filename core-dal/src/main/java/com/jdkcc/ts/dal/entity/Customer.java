package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_customer")
public class Customer implements java.io.Serializable {

	private static final long serialVersionUID = 2285174464789310329L;

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    /**
     * 昵称
     */
    @Column(name = "nickname", nullable = false, length = 32)
    private String nickname;
    /**
     * 头像链接
     */
    @Column(name = "headimgurl")
    private String headimgurl;
    /**
     * 手机
     */
    @Column(name = "telephone", nullable = false, length = 11)
    private String telephone;
    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 40)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, length = 19)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time", length = 19)
    private Date modifiedTime;
    /**
     * 账户是否激活可用
     */
	@Column(name = "is_active", nullable = false)
	private Integer active;
    /**
     * 微信账户ID
     */
    @Column(name = "wx_user_id")
    private Long wxUserId;

}
