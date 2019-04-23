package com.xmcc.service.impl;

import com.xmcc.commen.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import com.xmcc.entity.ProductCategory;
import com.xmcc.repository.ProductCategoryRepository;
import com.xmcc.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张兴林
 * @date 2019-04-16 17:32
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ResultResponse<List<ProductCategoryDto>> findAll() {
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();

        //利用lambda表达式流转换为dto集合
        return ResultResponse.success(productCategoryList.stream().map(productCategory ->
                ProductCategoryDto.build(productCategory))
        .collect(Collectors.toList()));
    }
}
