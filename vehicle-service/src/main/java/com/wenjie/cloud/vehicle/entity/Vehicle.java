package com.wenjie.cloud.vehicle.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

/**
 * 车辆实体
 */
@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** VIN 车辆识别码（17 位，唯一） */
    @Column(name = "vin", length = 17, nullable = false, unique = true)
    private String vin;

    /** 车型，如 "AITO M7" */
    @Column(name = "model", length = 64)
    private String model;

    /** 关联车主 ID */
    @Column(name = "owner_user_id")
    private Long ownerUserId;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
