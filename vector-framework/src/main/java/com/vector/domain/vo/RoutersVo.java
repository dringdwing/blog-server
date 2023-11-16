package com.vector.domain.vo;

import com.vector.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @Title: RoutersVo
 * @Package com.vector.domain.vo
 * @Author 芝士汉堡
 * @Date 2022/9/23 10:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {

    private List<Menu> menus;
}
