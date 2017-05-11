package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.Article;
import com.jdkcc.ts.dal.mapper.ArticleMapper;
import com.jdkcc.ts.dal.mapper.IBaseMapper;
import com.jdkcc.ts.service.dto.request.ArticleReq;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.response.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleService extends ABaseService<Article, Long> {

    private final ArticleMapper articleMapper;

    @Autowired
    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    protected IBaseMapper<Article, Long> getEntityDAO() {
        return articleMapper;
    }

    public ArticleDTO findOne(Long ArticleId) {
        Article article = articleMapper.findOne(ArticleId);
        return article == null ? null : ObjectConvertUtil.objectCopy(new ArticleDTO(), article);
    }

    @Transactional
    public void delete(Long articleId) {
        Article article = articleMapper.findOne(articleId);
        if (article == null) return;

        super.delete(articleId);
    }

    public Page<ArticleDTO> findAll(BasicSearchReq basicSearchReq) {
        Integer start = basicSearchReq.getStartIndex();
        Integer size = basicSearchReq.getPageSize();

        int pageNumber =  start / size + 1;
        PageRequest pageRequest = new PageRequest(pageNumber, size);

        List<Article> articleList = articleMapper.findAll( start, size);
        Long count = count();

        //有了其它搜索条件
        Page<Article> userPage = new PageImpl<>(articleList, pageRequest, count);

        return userPage.map(this::getArticleDTO);
    }

    private ArticleDTO getArticleDTO(Article source) {
        ArticleDTO articleDTO = new ArticleDTO();
        ObjectConvertUtil.objectCopy(articleDTO, source);
        return articleDTO;
    }

    @Transactional
    public void update(ArticleReq articleReq) {
        Article article = this.findById(articleReq.getId());
        if (article == null) return;

        ObjectConvertUtil.objectCopy(article, articleReq);
        article.setModifiedTime(new Date());
        this.update(article);

    }

    @Transactional
    public void insert(ArticleReq articleReq) {
        Article article = new Article();
        ObjectConvertUtil.objectCopy(article, articleReq);
        article.setCreatedTime(new Date());
        article.setModifiedTime(new Date());

        this.insert(article);
    }


}
