package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 2207185006755635269L;

	private Long id;

	private String name;

	private String description;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
