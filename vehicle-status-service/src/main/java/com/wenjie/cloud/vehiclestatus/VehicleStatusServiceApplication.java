package com.wenjie.cloud.vehiclestatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 车辆状态服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.wenjie.cloud")
public class VehicleStatusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleStatusServiceApplication.class, args);
    }
}
