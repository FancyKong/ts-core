package com.jdkcc.ts.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article implements java.io.Serializable {

	private static final long serialVersionUID = -9016983856075764634L;

	private Long id;

	private String title;

    private String content;

	private Integer readSum;

	private Date createdTime;

	private Date modifiedTime;


}
