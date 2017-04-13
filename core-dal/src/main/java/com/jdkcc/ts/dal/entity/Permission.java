package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements java.io.Serializable {
	
	private static final long serialVersionUID = 7008838524774836684L;

	private Long id;

	private String permit;

    private String description;

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permit='" + permit + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
