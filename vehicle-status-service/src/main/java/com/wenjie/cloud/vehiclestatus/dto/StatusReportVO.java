package com.wenjie.cloud.vehiclestatus.dto;

import lombok.Data;

import java.time.Instant;

/**
 * 状态上报查询出参 VO
 */
@Data
public class StatusReportVO {

    private Long id;

    /** VIN 车辆识别码（17 位） */
    private String vin;

    /** 电池电量（0~100） */
    private Integer batteryLevel;

    /** 纬度 */
    private Double latitude;

    /** 经度 */
    private Double longitude;

    /** 总里程（km） */
    private Double mileage;

    /** 车速（km/h） */
    private Double speed;

    /** 温度（℃） */
    private Double temperature;

    /** 上报时间 */
    private Instant reportTime;

    /** 创建时间 */
    private Instant createdAt;
}
