package com.xmcc.service;

import com.xmcc.commen.ResultResponse;
import com.xmcc.entity.ProductInfo;
import org.springframework.stereotype.Service;

/**
 * @author 张兴林
 * @date 2019-04-16 17:23
 */
@Service
public interface ProductInfoService {

    ResultResponse queryList();

    ResultResponse<ProductInfo> queryById(String productId);

    void updateProductInfo(ProductInfo productInfo);

}
