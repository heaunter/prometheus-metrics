package com.tongxiaoya.metrics;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tongxiaoya.metrics.business.DemoMetrics;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.internal.DefaultGauge;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CallApi {

    @Autowired
    private MeterRegistry meterRegistry;

    public Cache<String, String> cacheConnection = CacheBuilder.newBuilder()
            //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            //设置监听，当出现自动删除时的回调
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> removalNotification) {
                    List<Meter> meters = meterRegistry.getMeters();
                    meters.forEach(meter -> {
                                if (isSameMeter(meter, "", null))
                                    meterRegistry.remove(meter);
                            }
                    );
                }
            }).build();


    @GetMapping("/call")
    public String call() {
        String[] tags = {"host", "localhost", "name", "zhangsan"};
        DemoMetrics demoMetrics =
                new DemoMetrics("isup_index_56", "isup help", 32D, tags);
        demoMetrics.bindTo(meterRegistry);

        cacheConnection.put("isup_index_56", "isup_index_56");
        return "success";
    }


    @GetMapping("/remove")
    public String remove() {
        List<Meter> meters = meterRegistry.getMeters();
        meters.forEach(meter -> {
                    if (isSameMeter(meter, "", null))
                        meterRegistry.remove(meter);
                }
        );
        return "success";
    }

    private boolean isSameMeter(Meter meter, String name, String[] tags) {
        Meter.Id id = meter.getId();
        if (!id.getName().equals(name)) {
            return false;
        }
        Map<String, String> collect = id.getTags().stream().collect(Collectors.toMap(Tag::getKey, Tag::getValue));
        return true;
    }

}
