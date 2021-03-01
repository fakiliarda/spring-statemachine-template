package com.yaf.statemachine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:secret.properties")
@Getter
@Setter
public class SecretProperties {

    @Value("${username}")
    private String username;

}