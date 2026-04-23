package com.wenjie.cloud.vehicle.controller;

import com.wenjie.cloud.common.dto.ApiResponse;
import com.wenjie.cloud.vehicle.dto.VehicleDTO;
import com.wenjie.cloud.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 车辆管理 REST API
 */
@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * 创建车辆
     */
    @PostMapping
    public ApiResponse<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO dto) {
        return ApiResponse.success(vehicleService.createVehicle(dto));
    }

    /**
     * 根据 ID 查询车辆
     */
    @GetMapping("/{id}")
    public ApiResponse<VehicleDTO> getVehicle(@PathVariable Long id) {
        return ApiResponse.success(vehicleService.getVehicleById(id));
    }

    /**
     * 分页查询车辆列表
     */
    @GetMapping
    public ApiResponse<Page<VehicleDTO>> listVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(vehicleService.listVehicles(PageRequest.of(page, pageSize)));
    }

    /**
     * 删除车辆
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ApiResponse.success(null);
    }
}
