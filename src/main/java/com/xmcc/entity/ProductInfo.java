package com.xmcc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 张兴林
 * @date 2019-04-16 17:03
 */
@Entity //表示该类为实体类
@DynamicUpdate //设置为true，表示update对象的时候，生成动态的update语句，如果这个字段的值为null就不会被加入到update语句中
@Data //相当于set、get、toString方法
@AllArgsConstructor
@NoArgsConstructor
@Table(name="product_info")
public class ProductInfo {

    /** 类目id. */
    @Id  //主键
    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private BigDecimal productPrice;

    /** 库存. */
    private Integer productStock;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 状态, 0正常1下架. */
    private Integer productStatus;

    /** 类目编号. */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;
}
