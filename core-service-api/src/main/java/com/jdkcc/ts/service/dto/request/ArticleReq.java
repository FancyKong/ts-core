package com.jdkcc.ts.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleReq implements java.io.Serializable {

    private static final long serialVersionUID = 3120001402512097515L;

    private Long id;

    private String title;

    private String content;

    private Integer readSum;

}