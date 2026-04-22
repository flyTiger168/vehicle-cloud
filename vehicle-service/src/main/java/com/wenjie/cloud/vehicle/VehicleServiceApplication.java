package com.wenjie.cloud.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 车辆管理服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.wenjie.cloud")
public class VehicleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleServiceApplication.class, args);
    }
}
