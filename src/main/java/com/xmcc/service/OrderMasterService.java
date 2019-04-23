package com.xmcc.service;

import com.xmcc.commen.PageQuery;
import com.xmcc.commen.ResultResponse;
import com.xmcc.dto.OrderMasterDto;

public interface OrderMasterService {

    ResultResponse insertOrder(OrderMasterDto orderMasterDto);

    ResultResponse queryOrder(String openId, PageQuery pageQuery);

    ResultResponse queryDetail(String openid, String orderId);

    ResultResponse cancel(String openid, String orderId);

}
