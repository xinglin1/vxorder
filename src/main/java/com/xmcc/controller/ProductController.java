package com.xmcc.controller;

import com.xmcc.commen.ResultResponse;
import com.xmcc.service.ProductInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张兴林
 * @date 2019-04-16 17:18
 */
@RestController
@RequestMapping("/buyer/product")
@Api(description = "商品信息接口")//使用swagger2的注解对类描述
public class ProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping("/list")
    @ApiOperation(value = "查询商品列表")
    public ResultResponse list(){
        return productInfoService.queryList();
    }

}
