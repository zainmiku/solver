package com.fanhoufang.solver.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.fanhoufang.solver.remoteApi.DateRepository;


@Configuration
public class HttpClientConfig {

    @Value("${external.api.base-url}")
    private String externalApiBaseUrl;
    
    @Bean
    public DateRepository dateRepository() {
         RestClient restClient = RestClient.builder()
                         .baseUrl(externalApiBaseUrl)
                         .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        return HttpServiceProxyFactory.builderFor(adapter).build().createClient(DateRepository.class);
    }
}
