/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2016 All Rights Reserved.
 */
package com.jdkcc.ts.service.api;

import com.jdkcc.ts.service.dto.request.ArticleReq;
import com.jdkcc.ts.service.dto.response.ArticleDTO;

/**
 * @author Jiangjiaze
 * @version Id: UserFacade.java, v 0.1 2016/9/28 0028 上午 8:59 FancyKong Exp $$
 */
interface ArticleFacade {

    //Page<ArticleDTO> findAll(BasicSearchReq basicSearchReq);

    void delete(Long articleId);

    void update(ArticleReq articleReq);

    ArticleDTO insert(ArticleReq articleReq);

}
