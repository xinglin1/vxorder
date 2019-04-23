package com.xmcc.service.impl;

import com.xmcc.dao.BatchInsertDao;
import com.xmcc.dao.impl.BatchInsertDaoImpl;
import com.xmcc.entity.OrderDetail;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 张兴林
 * @date 2019-04-17 14:33
 */

/**
 * 订单生成实现步骤
 * 1、根据商品id查询商品是否存在，如果存在取出商品详细信息
 * 2、判断库存是否满足买家请求数量
 * 3、生成订单项Detail
 * 4、减少商品库存
 * 5、计算总价格，生成订单信息 插入数据库得到订单号
 * 6、批量插入订单项
 */
@Service
public class OrderDetailServiceImpl extends BatchInsertDaoImpl<OrderDetail> implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional //注意：需要开启事务管理
    public void batchInsert(List<OrderDetail> orderDetailList) {
        super.batchInsert(orderDetailList);
    }

    @Override
    public List<OrderDetail> findByOrderId(String orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }
}
