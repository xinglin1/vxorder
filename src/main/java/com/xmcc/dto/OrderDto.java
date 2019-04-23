package com.xmcc.dto;

import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author 张兴林
 * @date 2019-04-18 09:11
 */
@Data
public class OrderDto extends OrderMaster {

    private List<OrderDetail> orderDetailList;

    public static OrderDto build(OrderMaster orderMaster){
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        return orderDto;
    }

}
