package com.archicloud.polypet20212022.catalogmanager.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableSpringConfigured
@EnableAspectJAutoProxy
public class SpringContextAOP {
}
