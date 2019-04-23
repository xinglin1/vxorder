package com.xmcc.service.impl;

import com.xmcc.commen.ResultEnums;
import com.xmcc.commen.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import com.xmcc.dto.ProductInfoDto;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.ProductInfoRepository;
import com.xmcc.service.ProductCategoryService;
import com.xmcc.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 张兴林
 * @date 2019-04-16 17:24
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ResultResponse queryList() {

        ResultResponse<List<ProductCategoryDto>> categoryServiceResult = productCategoryService.findAll();
        //获取所有category
        List<ProductCategoryDto> categoryDtos = categoryServiceResult.getData();
        if (categoryDtos.isEmpty()){
            return ResultResponse.fail();
        }
        //获取所有type集合
        List<Integer> typeList = categoryDtos.stream().map(productCategoryDto ->
                productCategoryDto.getCategoryType()).collect(Collectors.toList());
        //根据typeList查询所有正常商品
        List<ProductInfo> productInfoList = productInfoRepository.findByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), typeList);

        //多线程遍历 取出每个商品类别对应的商品列表
        /**
         * 1、将productInfo设置到food中
         * 2、过滤 对不同的type进行不同的封装
         * 3、将productInfo转换成dto
         */
        List<ProductCategoryDto> categoryDtoList = categoryDtos.parallelStream().map(productCategoryDto -> {
            productCategoryDto.setProductInfoDtoList(productInfoList.stream()
                    .filter(productInfo -> productInfo.getCategoryType() == productCategoryDto.getCategoryType())
                    .map(productInfo -> ProductInfoDto.build(productInfo)).collect(Collectors.toList()));
            return productCategoryDto;
        }).collect(Collectors.toList());

        return ResultResponse.success(categoryDtoList);
    }

    /**
     * 根据产品id查询商品
     * @param productId
     * @return
     */
    @Override
    public ResultResponse<ProductInfo> queryById(String productId) {
        //判断传入的id是否为null
        if (StringUtils.isBlank(productId)){
            return ResultResponse.fail("商品id："+ResultEnums.PARAM_ERROR.getMsg());
        }
        //根据id查询商品信息
        Optional<ProductInfo> byId = productInfoRepository.findById(productId);
        //判断是否查询到商品
        //如果不存在
        if (!byId.isPresent()){
            return ResultResponse.fail(ResultEnums.NOT_EXITS.getMsg());
        }
        ProductInfo productInfo = byId.get();
        //如果存在，判断商品状态
        if (productInfo.getProductStatus() == ResultEnums.PRODUCT_DOWN.getCode()){
            return ResultResponse.fail(ResultEnums.PRODUCT_DOWN.getMsg());
        }
        return ResultResponse.success(productInfo);
    }

    @Override
    public void updateProductInfo(ProductInfo productInfo) {
        productInfoRepository.save(productInfo);
    }
}
