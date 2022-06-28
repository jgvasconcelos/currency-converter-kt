package br.com.jose.currencyconverter.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.util.TimeZone
import javax.annotation.PostConstruct

@Configuration
@MapperScan("br.com.jose.currencyconverter.mapper")
class AppConfig {

    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}