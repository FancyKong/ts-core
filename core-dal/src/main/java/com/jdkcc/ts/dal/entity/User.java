package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements java.io.Serializable {

    private static final long serialVersionUID = -3703091209635157421L;

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String telephone;

    private Date createdTime;

    private Date modifiedTime;

    private Integer active;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                ", active=" + active +
                '}';
    }
}
