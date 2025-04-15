package com.example.configservice.config;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Component
public class PropertySourceRefresher {

    private static final String DYNAMIC_PROPERTY_SOURCE_NAME = "dynamicProperties";

    private final ConfigurableEnvironment environment;
    private final ApplicationContext applicationContext;
    private final ConfigurationPropertiesRebinder rebinder;

    public PropertySourceRefresher(ConfigurableEnvironment environment,
                                 ApplicationContext applicationContext,
                                 ConfigurationPropertiesRebinder rebinder) {
        this.environment = environment;
        this.applicationContext = applicationContext;
        this.rebinder = rebinder;
    }

    public synchronized void updateProperties(Map<String, Object> properties) {
        MutablePropertySources propertySources = environment.getPropertySources();
        MapPropertySource target = null;

        if (propertySources.contains(DYNAMIC_PROPERTY_SOURCE_NAME)) {
            PropertySource<?> source = propertySources.get(DYNAMIC_PROPERTY_SOURCE_NAME);
            if (source instanceof MapPropertySource) {
                target = (MapPropertySource) source;
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    target.getSource().put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            target = new MapPropertySource(DYNAMIC_PROPERTY_SOURCE_NAME, new HashMap<>(properties));
            propertySources.addFirst(target);
        }

        applicationContext.publishEvent(new EnvironmentChangeEvent(properties.keySet()));
        rebinder.rebind();
    }
}