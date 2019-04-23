package com.xmcc.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.xmcc.properties.WechatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张兴林
 * @date 2019-04-22 14:29
 */
@Configuration
public class PayConfig {

    @Autowired
    private WechatProperties wechatProperties;

    @Bean
    public BestPayService bestPayService(){
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        //设置公众号appid
        wxPayH5Config.setAppId(wechatProperties.getAppid());
        //设置公众号密钥
        wxPayH5Config.setAppSecret(wechatProperties.getSecret());
        //设置商户号
        wxPayH5Config.setMchId(wechatProperties.getMchId());
        //设置商户密钥
        wxPayH5Config.setMchKey(wechatProperties.getMchKey());
        //设置商户证书路径
        wxPayH5Config.setKeyPath(wechatProperties.getKeyPath());
        //设置异步通知路径
        wxPayH5Config.setNotifyUrl(wechatProperties.getNotifyUrl());
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config);
        return bestPayService;
    }

}
