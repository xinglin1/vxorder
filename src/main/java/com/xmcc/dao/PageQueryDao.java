package com.xmcc.dao;

import com.xmcc.commen.PageQuery;
import com.xmcc.entity.OrderMaster;

import java.util.List;

/**
 * @author 张兴林
 * @date 2019-04-18 09:19
 */
public interface PageQueryDao {

    List<OrderMaster> pageQueryOrder(String openId, PageQuery pageQuery);

}
