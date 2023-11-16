package com.vector.controller;

import com.vector.domain.ResponseResult;
import com.vector.domain.entity.Article;
import com.vector.domain.to.AddArticleDto;
import com.vector.domain.to.ArticleDto;
import com.vector.domain.vo.ArticleVo;
import com.vector.domain.vo.PageVo;
import com.vector.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
@SuppressWarnings("all")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }

    /**
     * 文章列表接口
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize) {
        PageVo pageVo = articleService.selectArticlePage(article, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 查询文章接口设计
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        ArticleVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto article) {
        articleService.edit(article);
        return ResponseResult.okResult();
    }

    /**
     * 删除文章接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}