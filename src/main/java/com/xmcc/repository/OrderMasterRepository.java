package com.xmcc.repository;

import com.xmcc.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

    @Query(value = "select s from OrderMaster s where buyer_openid=:openid order by create_time desc")
    Page<OrderMaster> findOrderMasterByOpenIdPageable(@Param("openid") String openid, Pageable pageable);

    OrderMaster findByOrderIdAndBuyerOpenid(String orderId,String openid);

}
