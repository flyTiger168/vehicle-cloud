package com.wenjie.cloud.vehicle.repository;

import com.wenjie.cloud.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 车辆数据访问层
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * 根据 VIN 查询车辆
     */
    Optional<Vehicle> findByVin(String vin);

    /**
     * 判断 VIN 是否已存在
     */
    boolean existsByVin(String vin);
}
