package com.xmcc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmcc.entity.ProductCategory;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author 张兴林
 * @date 2019-04-16 17:06
 */
@Data
public class ProductCategoryDto implements Serializable {

    @JsonProperty("name")
    private String CategoryName;

    @JsonProperty("type")
    private Integer CategoryType;

    @JsonProperty("foods")
    private List<ProductInfoDto> productInfoDtoList;

    public static ProductCategoryDto build(ProductCategory productCategory){
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        BeanUtils.copyProperties(productCategory,productCategoryDto);
        return productCategoryDto;
    }


}
