package com.xmcc.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张兴林
 * @date 2019-04-16 14:41
 */
@Configuration //注明该类为配置类
public class DruidConfig {

    @Bean(value = "druidDataSource",initMethod = "init",destroyMethod = "close")//将方法返回值作为bean交给spring容器管理
    @ConfigurationProperties(prefix = "spring.druid")
    public DruidDataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return druidDataSource;
    }

    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        //慢查询是否记录日志
        statFilter.setLogSlowSql(true);
        //慢查询时间
        statFilter.setSlowSqlMillis(5);
        //格式化sql
        statFilter.setMergeSql(true);
        return statFilter;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        //druid监控平台，输入http://localhost:8888/druid即可访问平台
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }

}
