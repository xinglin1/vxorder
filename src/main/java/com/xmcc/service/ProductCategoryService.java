package com.xmcc.service;

import com.xmcc.commen.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 张兴林
 * @date 2019-04-16 17:31
 */
@Service
public interface ProductCategoryService {

    ResultResponse<List<ProductCategoryDto>> findAll();

}
