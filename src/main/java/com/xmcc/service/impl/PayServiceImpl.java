package com.xmcc.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.xmcc.commen.Constant;
import com.xmcc.commen.OrderEnum;
import com.xmcc.commen.OrderEnums;
import com.xmcc.commen.PayEnums;
import com.xmcc.entity.OrderMaster;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.PayService;
import com.xmcc.utils.BigDecimalUtil;
import com.xmcc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author 张兴林
 * @date 2019-04-22 15:09
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private BestPayService bestPayService;

    @Override
    public OrderMaster findOrderById(String orderId) {
        Optional<OrderMaster> byId = orderMasterRepository.findById(orderId);
        if (!byId.isPresent()){
            //订单不存在抛异常
            throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        return byId.get();
    }

    @Override
    public PayResponse create(OrderMaster orderMaster) {
        PayRequest payRequest = new PayRequest();
        //微信用户openid
        payRequest.setOpenid(orderMaster.getBuyerOpenid());
        //订单金额
        payRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        //订单id
        payRequest.setOrderId(orderMaster.getOrderId());
        //订单名字
        payRequest.setOrderName(Constant.orderName);
        //支付类型
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信支付的请求:{}", JsonUtil.object2string(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("微信支付的返回结果为：{}",JsonUtil.object2string(payResponse));
        return payResponse;
    }

    @Override
    public void weixin_notify(String notify) {
        //调用API 会自动完成支付状态签名等验证
        PayResponse response = bestPayService.asyncNotify(notify);
        //根据订单id查询订单
        OrderMaster orderMaster = findOrderById(response.getOrderId());
        //比较金额  这里注意 orderMaster中是BigDecimal 而 response里面是double
        //还需要注意的点 new BigDecimal的时候只能用字符串类型，不然精度会丢失
        if(!BigDecimalUtil.equals2(orderMaster.getOrderAmount(),new BigDecimal(String.valueOf(response.getOrderAmount())))){
            //有异常的地方必须打印日志
            log.error("微信支付回调，订单金额不一致.微信:{},数据库:{}",response.getOrderAmount(),orderMaster.getOrderAmount());
            throw new CustomException(OrderEnums.AMOUNT_CHECK_ERROR.getMsg());
        }
        //判断支付状态是否为可支付（ 等待支付才能支付） 避免重复通知等其他因素
        if(!(orderMaster.getPayStatus()== PayEnums.WAIT.getCode())){
            log.error("微信回调,订单状态异常：{}",orderMaster.getPayStatus());
            throw new CustomException(PayEnums.STATUS_ERROR.getMsg());
        }
        //比较结束以后 完成订单支付状态的修改
        //实际项目中 这儿还需要把交易流水号与订单的对应关系存入数据库，比较简单，这儿不做了,大家需要知道
        orderMaster.setPayStatus(PayEnums.FINISH.getCode());
        //注意:这儿只是支付状态OK  订单状态的修改 需要其他业务流程，发货，用户确认收货
        //修改支付状态
        orderMasterRepository.save(orderMaster);
        log.info("微信支付异步回调,订单支付状态修改完成");
    }

    @Override
    public RefundResponse refund(OrderMaster orderMaster) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderMaster.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信退款请求:{}",refundRequest);
        //执行退款
        RefundResponse refund = bestPayService.refund(refundRequest);
        log.info("微信退款请求响应:{}",refund);
        return refund;
    }
}
