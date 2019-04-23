package com.xmcc.controller;

import com.lly835.bestpay.model.PayResponse;
import com.xmcc.entity.OrderMaster;
import com.xmcc.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author 张兴林
 * @date 2019-04-22 15:03
 */
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map map){
        //根据订单id查询订单
        OrderMaster orderMaster = payService.findOrderById(orderId);

        //根据订单创建支付
        PayResponse payResponse = payService.create(orderMaster);

        map.put("payResponse",payResponse);
        map.put("returnUrl",returnUrl);
        return new ModelAndView("weixin/pay",map);
    }

    @RequestMapping("/notify")
    public ModelAndView weixin_notify(@RequestBody String notify){
        log.info("微信支付，异步回调");
        payService.weixin_notify(notify);
        return new ModelAndView("weixin/success");
    }

}
