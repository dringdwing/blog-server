package com.vector.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @Title: PageVo
 * @Package com.vector.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/9/18 15:08
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    private List rows;
    private Long total;
}
