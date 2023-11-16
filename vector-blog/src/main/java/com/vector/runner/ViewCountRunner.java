package com.vector.runner;

import com.vector.domain.entity.Article;
import com.vector.mapper.ArticleMapper;
import com.vector.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 在应用启动时把博客的浏览量存储到redis中
 * @Title: ViewCountRunner
 * @Package com.vector.runner
 * @Author 芝士汉堡
 * @Date 2022/9/21 14:48
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息 id viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString()
                        , article -> {
                            return article.getViewCount().intValue();
                        }));
        // 存储到redis
        redisCache.setCacheMap("article:viewCount", viewCountMap);

    }


}
