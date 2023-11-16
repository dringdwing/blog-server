package com.vector.cron;

import com.vector.domain.entity.Article;
import com.vector.service.ArticleService;
import com.vector.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 更新浏览量定时任务
 * @Title: UpdateViewCountJob
 * @Package com.vector.cron
 * @Author 芝士汉堡
 * @Date 2022/9/21 14:51
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount() {
        // 获取redis的浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");

        // 双列集合是不能转换为流对象的
        List<Article> articles = cacheMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        // 更新到数据库中
        articleService.updateBatchById(articles);
    }

}
