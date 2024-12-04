package com.web3.twitter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Configuration
public class RestTemplateConfig {

    private final boolean enableProxy = true;
    private final String host = "127.0.0.1";
    private final int port = 7890;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public SimpleClientHttpRequestFactory httpClientFactory() {
//        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
//        httpRequestFactory.setReadTimeout(35000);
//        httpRequestFactory.setConnectTimeout(35000);
//
//        if(enableProxy){

//            SocketAddress address = new InetSocketAddress(host, port);
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
//            httpRequestFactory.setProxy(proxy);
//        }
//
//        return httpRequestFactory;
//    }
//
//    @Bean
//    public RestTemplate restTemplate(SimpleClientHttpRequestFactory httpClientFactory) {
//        return new RestTemplate(httpClientFactory);
//    }
}
