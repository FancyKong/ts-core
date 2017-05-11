package com.jdkcc.ts.dal.mapper;

import com.jdkcc.ts.dal.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ArticleMapper extends IBaseMapper<Article, Long> {

    Article findByTitle(String title);

}
