package com.alink.control.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.alink.control.services")
public class AlinkServiceConfiguration {
}
