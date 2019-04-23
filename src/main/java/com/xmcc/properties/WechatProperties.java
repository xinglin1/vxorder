package com.xmcc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 张兴林
 * @date 2019-04-19 14:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    private String appid;

    private String secret;

    private String mchId;

    private String mchKey;

    private String keyPath;

    private String notifyUrl;

}
