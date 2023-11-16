package com.vector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.domain.ResponseResult;
import com.vector.domain.entity.Article;
import com.vector.domain.entity.Category;
import com.vector.domain.to.AddArticleDto;
import com.vector.domain.to.ArticleDto;
import com.vector.domain.vo.ArticleVo;
import com.vector.domain.vo.PageVo;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @Title: ArticleService
 * @Package com.vector.service
 * @Author 芝士汉堡
 * @Date 2022/9/18 10:21
 */
@SuppressWarnings("all")
public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(@Param("id") Long id);

    ResponseResult updateViewCount(@Param("id") Long id);

    ResponseResult add(AddArticleDto article);

    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    ArticleVo getInfo(Long id);

    void edit(ArticleDto article);

}

