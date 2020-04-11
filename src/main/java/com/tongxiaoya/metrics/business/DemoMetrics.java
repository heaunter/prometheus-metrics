package com.tongxiaoya.metrics.business;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class DemoMetrics implements MeterBinder {

    private String name;
    private String help;
    private Double value;
    private String[] tags;

    public DemoMetrics(String indexName, String helpText, Double aDouble, String[] paramTags) {
        name = indexName;
        help = helpText;
        value = aDouble;
        tags = paramTags;
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.Builder<Double> description = Gauge.builder(name, value, Double::doubleValue)
                .tags(tags)
                .description(help);
        description
                .register(meterRegistry);
    }
}
