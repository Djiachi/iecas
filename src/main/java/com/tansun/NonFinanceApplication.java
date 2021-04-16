package com.tansun;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.tansun.ider.util.CacheUtilProperties;

import okhttp3.OkHttpClient;

/**
 * 信用卡核心启动类
 * 
 * @author wt
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.tansun.ider.dao.*.mapper")
@EnableConfigurationProperties(CacheUtilProperties.class)
public class NonFinanceApplication {
    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(NonFinanceApplication.class, args);
    }

    @Bean
    @LoadBalanced // 使用注册中心，请开启负载均衡。 开启后，配置文件中请使用服务名调用
    RestTemplate restTemplate() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true);

        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(builder.build()));
        return restTemplate;
    }

    @PostConstruct
    void testDBConnection() {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
