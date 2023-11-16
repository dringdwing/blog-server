package com.vector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.domain.ResponseResult;
import com.vector.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-09-19 14:58:43
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
