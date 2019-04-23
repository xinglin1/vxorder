package com.xmcc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author 张兴林
 * @date 2019-04-17 14:24
 */
@Entity //表示该类为实体类
@DynamicUpdate //设置为true，表示update对象的时候，生成动态的update语句，如果这个字段的值为null就不会被加入到update语句中
@Data //相当于set、get、toString方法
@AllArgsConstructor
@NoArgsConstructor
@Table(name="order_detail")
@Builder
public class OrderDetail {
    @Id
    private String detailId;

    /** 订单id. */
    private String orderId;

    /** 商品id. */
    private String productId;

    /** 商品名称. */
    private String productName;

    /** 商品单价. */
    private BigDecimal productPrice;

    /** 商品数量. */
    private Integer productQuantity;

    /** 商品小图. */
    private String productIcon;
}
