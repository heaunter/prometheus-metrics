package com.tongxiaoya.metrics;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.tongxiaoya.metrics.business.DemoMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MetricApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(MetricApplication.class, args);
    }

//    @Bean
//    public DemoMetrics demoMetrics() {
//        String[] tags = {"host", "localhost", "name", "zhangsan"};
//        DemoMetrics demoMetrics =
//                new DemoMetrics("isup", "isup help", 3D, tags);
//        return demoMetrics;
//    }

    @Bean
    public MeterFilter jvm() {
        return MeterFilter.denyNameStartsWith("jvm");
    }

    @Bean
    public MeterFilter tomcat() {
        return MeterFilter.denyNameStartsWith("tomcat");
    }

    @Bean
    public MeterFilter process() {
        return MeterFilter.denyNameStartsWith("process");
    }

    @Bean
    public MeterFilter system() {
        return MeterFilter.denyNameStartsWith("system");
    }

    @Bean
    public MeterFilter logback() {
        return MeterFilter.denyNameStartsWith("logback");
    }
}
