package com.vector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.domain.ResponseResult;
import com.vector.domain.entity.Category;
import com.vector.domain.vo.CategoryVo;
import com.vector.domain.vo.PageVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-09-18 13:49:49
 */
public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize);
}
