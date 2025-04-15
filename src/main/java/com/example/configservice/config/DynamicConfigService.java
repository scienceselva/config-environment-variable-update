package com.example.configservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.StreamSupport;

@Service
public class DynamicConfigService {

    private final ConfigurableEnvironment environment;
    private final PropertySourceRefresher propertySourceRefresher;

    @Autowired
    public DynamicConfigService(ConfigurableEnvironment environment, 
                              PropertySourceRefresher propertySourceRefresher) {
        this.environment = environment;
        this.propertySourceRefresher = propertySourceRefresher;
    }

    public String updateProperty(String key, String value) {
        Map<String, Object> newProperties = new HashMap<>();
        newProperties.put(key, value);
        
        propertySourceRefresher.updateProperties(newProperties);
        return String.format("Property %s updated to %s", key, value);
    }

    public String updateEnvVariable(String key, String value) {
        try {
            Map<String, Object> envProperties = new HashMap<>();
            envProperties.put(key, value);
            
            propertySourceRefresher.updateProperties(envProperties);
            return String.format("Environment variable %s updated to %s (Note: Only affects current JVM)", key, value);
        } catch (Exception e) {
            return "Failed to update environment variable: " + e.getMessage();
        }
    }

    public Map<String, Object> listAllProperties() {
        Map<String, Object> allProperties = new HashMap<>();
        
        StreamSupport.stream(environment.getPropertySources().spliterator(), false)
            .filter(ps -> ps instanceof EnumerablePropertySource)
            .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
            .flatMap(Arrays::stream)
            .distinct()
            .forEach(prop -> {
                try {
                    allProperties.put(prop, environment.getProperty(prop));
                } catch (Exception e) {
                    allProperties.put(prop, "Error: " + e.getMessage());
                }
            });
            
        return allProperties;
    }
}