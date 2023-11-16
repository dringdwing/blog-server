package com.vector.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.domain.entity.ArticleTag;
import com.vector.mapper.ArticleTagMapper;
import com.vector.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}