package com.wenjie.cloud.vehicle.service.impl;

import com.wenjie.cloud.common.exception.BusinessException;
import com.wenjie.cloud.vehicle.dto.VehicleDTO;
import com.wenjie.cloud.vehicle.entity.Vehicle;
import com.wenjie.cloud.vehicle.repository.VehicleRepository;
import com.wenjie.cloud.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 车辆管理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public VehicleDTO createVehicle(VehicleDTO dto) {
        if (vehicleRepository.existsByVin(dto.getVin())) {
            throw new BusinessException(1001, "VIN 已存在: " + dto.getVin());
        }

        var entity = new Vehicle();
        entity.setVin(dto.getVin());
        entity.setModel(dto.getModel());
        entity.setOwnerUserId(dto.getOwnerUserId());
        entity.setCreatedAt(Instant.now());

        var saved = vehicleRepository.save(entity);
        log.info("车辆创建成功, id={}, vin={}", saved.getId(), saved.getVin());
        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        var entity = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(1002, "车辆不存在, id=" + id));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> listVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> listVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new BusinessException(1002, "车辆不存在, id=" + id);
        }
        vehicleRepository.deleteById(id);
        log.info("车辆删除成功, id={}", id);
    }

    // ---- 内部转换 ----

    private VehicleDTO toDTO(Vehicle entity) {
        var dto = new VehicleDTO();
        dto.setId(entity.getId());
        dto.setVin(entity.getVin());
        dto.setModel(entity.getModel());
        dto.setOwnerUserId(entity.getOwnerUserId());
        return dto;
    }
}
