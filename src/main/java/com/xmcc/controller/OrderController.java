package com.xmcc.controller;

import com.google.common.collect.Maps;
import com.xmcc.commen.PageQuery;
import com.xmcc.commen.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.service.OrderMasterService;
import com.xmcc.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张兴林
 * @date 2019-04-17 09:11
 */
@RestController
@RequestMapping("/buyer/order")
@Api(value = "订单相关接口",description = "订单的增删改查")
public class OrderController {

    @Autowired
    private OrderMasterService orderMasterService;

    @RequestMapping("/create")
    @ApiOperation(value = "创建订单",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse create(
            @Valid @ApiParam(name = "订单对象",value = "传入json格式",required = true)
            OrderMasterDto orderMasterDto, BindingResult bindingResult){
        HashMap<String, String> map = Maps.newHashMap();
        //判断参数校验是否有问题
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errors));
            return ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }

    @RequestMapping("/list")
    @ApiOperation(value = "订单列表",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse list(String openid, PageQuery pageQuery){
        return orderMasterService.queryOrder(openid,pageQuery);
    }

    @RequestMapping("/detail")
    @ApiOperation(value = "订单详情",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse detail(String openid, String orderId){
        return orderMasterService.queryDetail(openid,orderId);
    }

    @RequestMapping("/cancel")
    @ApiOperation(value = "取消订单",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse cancel(String openid, String orderId){
        return orderMasterService.cancel(openid,orderId);
    }

}
