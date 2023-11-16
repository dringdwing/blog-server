package com.vector.controller;

import com.vector.domain.ResponseResult;
import com.vector.domain.entity.Link;
import com.vector.domain.vo.PageVo;
import com.vector.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("all")
@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 获取友链列表
     */
    @GetMapping("/list")
    public ResponseResult list(Link link, Integer pageNum, Integer pageSize) {
        PageVo pageVo = linkService.selectLinkPage(link, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 新增友联
     * @param link
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 更新友联
     * @param link
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    /**
     * 获取友联 数据回显
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }
}
