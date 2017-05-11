package com.jdkcc.ts.service.impl;

import com.jdkcc.ts.common.util.ObjectConvertUtil;
import com.jdkcc.ts.dal.entity.Article;
import com.jdkcc.ts.dal.repository.ArticleDAO;
import com.jdkcc.ts.dal.repository.IBaseDAO;
import com.jdkcc.ts.service.dto.request.ArticleReq;
import com.jdkcc.ts.service.dto.request.BasicSearchReq;
import com.jdkcc.ts.service.dto.response.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class ArticleService extends ABaseService<Article, Long> {

    private final ArticleDAO articleDAO;

    @Autowired
    public ArticleService(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    @Override
    protected IBaseDAO<Article, Long> getEntityDAO() {
        return articleDAO;
    }

    public ArticleDTO findOne(Long articleId) {
        Article article = articleDAO.findOne(articleId);
        return article == null ? null : ObjectConvertUtil.objectCopy(new ArticleDTO(), article);
    }

    @Transactional
    public void delete(Long articleId) {
        Article article = articleDAO.findOne(articleId);
        if (article == null) return;
        super.delete(articleId);
    }

    public Page<ArticleDTO> findAll(BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<Article> articlePage = this.findAll(pageNumber, basicSearchReq.getPageSize());

        return articlePage.map(source -> {
            ArticleDTO articleDTO = new ArticleDTO();
            ObjectConvertUtil.objectCopy(articleDTO, source);
            return articleDTO;
        });
    }

    @Transactional
    public void update(ArticleReq articleReq) {
        Article article = this.findById(articleReq.getId());
        if (article == null) return;

        ObjectConvertUtil.objectCopy(article, articleReq);
        article.setModifiedTime(new Date());
        if (article.getReadSum() == null){
            article.setReadSum(0);
        }
        this.update(article);
    }

    @Transactional
    public Article save(ArticleReq articleReq) {
        Article article = new Article();
        ObjectConvertUtil.objectCopy(article, articleReq);
        article.setCreatedTime(new Date());
        article.setModifiedTime(new Date());
        if (article.getReadSum() == null){
            article.setReadSum(0);
        }
        return this.save(article);
    }


}
