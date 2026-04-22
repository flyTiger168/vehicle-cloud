package com.wenjie.cloud.vehiclestatus.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.Instant;

/**
 * 车辆状态上报实体
 */
@Data
@Entity
@Table(name = "status_report", indexes = {
        @Index(name = "idx_vin_report_time", columnList = "vin, report_time")
})
public class StatusReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** VIN 车辆识别码（17 位） */
    @Column(name = "vin", length = 17, nullable = false)
    private String vin;

    /** 电池电量（0~100） */
    @Column(name = "battery_level", nullable = false)
    private Integer batteryLevel;

    /** 纬度 */
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    /** 经度 */
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    /** 总里程（km） */
    @Column(name = "mileage", nullable = false)
    private Double mileage;

    /** 车速（km/h） */
    @Column(name = "speed", nullable = false)
    private Double speed;

    /** 温度（℃） */
    @Column(name = "temperature", nullable = false)
    private Double temperature;

    /** 上报时间 */
    @Column(name = "report_time", nullable = false)
    private Instant reportTime;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
}
