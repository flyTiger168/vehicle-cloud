package com.wenjie.cloud.vehicle.service;

import com.wenjie.cloud.vehicle.dto.VehicleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 车辆管理服务接口
 */
public interface VehicleService {

    /**
     * 创建车辆
     */
    VehicleDTO createVehicle(VehicleDTO dto);

    /**
     * 根据 ID 查询车辆
     */
    VehicleDTO getVehicleById(Long id);

    /**
     * 查询所有车辆列表
     */
    List<VehicleDTO> listVehicles();

    /**
     * 分页查询车辆列表
     */
    Page<VehicleDTO> listVehicles(Pageable pageable);

    /**
     * 根据 ID 删除车辆
     */
    void deleteVehicle(Long id);
}
