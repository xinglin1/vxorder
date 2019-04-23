package com.xmcc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.commen.*;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.utils.BigDecimalUtil;
import com.xmcc.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author 张兴林
 * @date 2019-04-17 14:34
 */
@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        //取出订单项
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //创建订单集合 进行批量插入
        List<OrderDetail> orderDetailList = Lists.newArrayList();
        //设置变量计算订单总金额
        BigDecimal totalPrice = new BigDecimal("0");

        for (OrderDetailDto item : items) {
            //从数据库中查询商品
            ResultResponse<ProductInfo> resultResponse = productInfoService.queryById(item.getProductId());
            //判断商品是否存在，如果不存在就抛出异常，进行事务回滚
            if (resultResponse.getCode() == ResultEnums.FAIL.getCode()) {
                throw new CustomException(resultResponse.getMsg());
            }
            //获取商品
            ProductInfo productInfo = resultResponse.getData();
            //判断商品库存是否满足用户所需数量
            if (productInfo.getProductStock() < item.getProductQuantity()) {
                throw new CustomException(ResultEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //构建订单对象并且封装到订单集合中 将生成的订单项id设置到订单项detail中
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productId(item.getProductId())
                    .productIcon(productInfo.getProductIcon()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(item.getProductQuantity())
                    .build();
            //将构建的对象添加到detail集合中
            orderDetailList.add(orderDetail);
            //减少商品库存
            productInfo.setProductStock(productInfo.getProductStock() - item.getProductQuantity());
            //修改数据库数据
            productInfoService.updateProductInfo(productInfo);
            //计算价格
            totalPrice = BigDecimalUtil.add(totalPrice,
                    BigDecimalUtil.multi(productInfo.getProductPrice(), item.getProductQuantity()));
        }
        /** 订单与订单项为一对多关系 */
        //生成订单id
        String orderId = IDUtils.createIdbyUUID();
        //构建订单信息
        OrderMaster orderMaster = OrderMaster.builder().buyerAddress(orderMasterDto.getAddress())
                .buyerName(orderMasterDto.getName()).buyerOpenid(orderMasterDto.getOpenid())
                .buyerPhone(orderMasterDto.getPhone()).orderAmount(totalPrice)
                .orderId(orderId).orderStatus(OrderEnums.NEW.getCode()).payStatus(PayEnums.WAIT.getCode())
                .build();
        //批量修改订单项的order_id
        orderDetailList.stream().forEach(orderDetail -> orderDetail.setOrderId(orderId));
        //批量插入订单项
        orderDetailService.batchInsert(orderDetailList);
        //插入订单
        orderMasterRepository.save(orderMaster);
        //创建map集合封装返回前端的数据
        HashMap<String, String> map = Maps.newHashMap();
        map.put("orderId",orderId);
        return ResultResponse.success(map);
    }

    /**
     * 订单列表
     * 1、根据openid查询订单
     * 2、根据订单id查询订单项封装到List集合中
     * @param pageQuery
     * @return
     */
    @Override
    public ResultResponse<OrderDto> queryOrder(String openId, PageQuery pageQuery) {

        Pageable pageable = PageRequest.of(pageQuery.getPage() - 1,pageQuery.getSize());
        Page<OrderMaster> page = orderMasterRepository.findOrderMasterByOpenIdPageable(openId, pageable);
        List<OrderMaster> orderMasterList = page.getContent();
        List<OrderDto> orderDtoList = new ArrayList<>();

        //根据订单id查询订单详细信息
        orderMasterList.stream().forEach(orderMaster -> {
            orderDtoList.add(OrderDto.build(orderMaster));
        });
        orderDtoList.stream().forEach(orderDto -> {
            orderDto.setOrderDetailList(orderDetailService.findByOrderId(orderDto.getOrderId()));
        });
        return ResultResponse.success(orderDtoList);
    }

    @Override
    public ResultResponse queryDetail(String openid, String orderId) {
        Optional<OrderMaster> byId = orderMasterRepository.findById(orderId);
        if (!byId.isPresent()){
            return ResultResponse.fail(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        OrderMaster orderMaster = byId.get();
        //判断订单是否是当前用户所属
        if (!orderMaster.getBuyerOpenid().equals(openid)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg());
        }
        List<OrderDetail> orderDetailList = orderDetailService.findByOrderId(orderId);
        if (orderDetailList == null || orderDetailList.size() == 0){
            return ResultResponse.fail(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        OrderDto orderDto = OrderDto.build(orderMaster);
        orderDto.setOrderDetailList(orderDetailList);
        return ResultResponse.success(orderDto);
    }

    @Transactional
    @Override
    public ResultResponse cancel(String openid, String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findByOrderIdAndBuyerOpenid(orderId, openid);
        if (orderMaster == null) {
            return ResultResponse.fail(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        //如果订单已经成功支付，则不能取消
        if (orderMaster.getPayStatus() == PayEnums.FINISH.getCode()) {
            return ResultResponse.fail("已完成订单不能取消");
        }
        orderMaster.setOrderStatus(OrderEnums.CANCEL.getCode());
        orderMasterRepository.save(orderMaster);
        return ResultResponse.success();
    }

}
