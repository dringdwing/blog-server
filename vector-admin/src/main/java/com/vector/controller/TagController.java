package com.vector.controller;

import com.vector.domain.ResponseResult;
import com.vector.domain.entity.Tag;
import com.vector.domain.to.AddTagDto;
import com.vector.domain.to.EditTagDto;
import com.vector.domain.to.TagListDto;
import com.vector.domain.vo.PageVo;
import com.vector.domain.vo.TagVo;
import com.vector.service.TagService;
import com.vector.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @Title: TagController
 * @Package com.vector.controller
 * @Author 芝士汉堡
 * @Date 2022/9/21 18:11
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    /**
     * 新增标签接口
     * @param tagDto
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 获取标签信息的接口 作为PutMapping 表单回显
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    /**
     * 修改标签接口
     * @param tagDto
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody EditTagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto,Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 查询所有标签接口
     * @return
     */

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
