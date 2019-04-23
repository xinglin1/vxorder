package com.xmcc.repository;

import com.xmcc.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

//第一个参数是实体类，第二个是主键类型
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
}
