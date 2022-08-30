package com.archicloud.polypet20212022.catalogmanager.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableAsync
public class ResearchInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ResearchInterceptor.class.getName());

    @Value("${monitor.site.url}")
    private String monitorUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ResearchInterceptor(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public void incrementConsulting(String reference, SearchProductWS.ProductDTO value) {

        log.info("Intercepting");

        if (value != null) {

            log.info("reference = " + reference);

            var responseEntity = restTemplate.getForEntity(monitorUrl + "/update/consulting/" + reference,
                    String.class);

            if (responseEntity.getStatusCode().isError()) {
                log.info("Something went wrong while updating consulting counter :-/");
            }
        }

    }

}
