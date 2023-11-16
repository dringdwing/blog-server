package com.vector.service;

import com.vector.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @Title: UploadService
 * @Package com.vector.service
 * @Author 芝士汉堡
 * @Date 2022/9/20 14:08
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
