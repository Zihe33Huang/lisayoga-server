package com.zihe.tams.module;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangzihe
 * @date 2023/5/25 5:40 PM
 */
@RestController
public class HealthController {
    @RequestMapping("/health")
    public String health() {
        return "ok";
    }
}
