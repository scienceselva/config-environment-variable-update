package com.example.configservice.config;

//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class DynamicConfigController {

    private final DynamicConfigService configService;

    public DynamicConfigController(DynamicConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/update")
    //@PreAuthorize("hasRole('ADMIN')")
    public String updateProperty(
            @RequestParam String key,
            @RequestParam String value) {
        return configService.updateProperty(key, value);
    }

    @GetMapping("/env/update")
    //@PreAuthorize("hasRole('ADMIN')")
    public String updateEnvVariable(
            @RequestParam String key,
            @RequestParam String value) {
        return configService.updateEnvVariable(key, value);
    }

    @GetMapping("/list")
    //@PreAuthorize("hasRole('USER')")
    public Map<String, Object> listProperties() {
        return configService.listAllProperties();
    }
}