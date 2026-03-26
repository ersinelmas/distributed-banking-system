package com.dbs.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@SpringBootApplication
@EnableJpaRepositories(
        basePackages = "com.dbs.customer.domain.repository",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "com.dbs.customer.domain.repository.elastic.*")
)
@EnableElasticsearchRepositories(
        basePackages = "com.dbs.customer.domain.repository.elastic"
)
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}