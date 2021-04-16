package com.tansun;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;

public class WebTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

    @Override
    public void customize(Connector connector) {
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        // 设置最大连接数
        protocol.setMaxConnections(1000);
        // 设置最大线程数
        protocol.setMaxThreads(400);
        protocol.setMinSpareThreads(200);
        protocol.setConnectionTimeout(60000);
        protocol.setAcceptCount(1000);
        //protocol.setMaxHeaderCount(8192);
    }


}
