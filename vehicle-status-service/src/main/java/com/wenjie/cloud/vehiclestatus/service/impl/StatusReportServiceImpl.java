package com.wenjie.cloud.vehiclestatus.service.impl;

import com.wenjie.cloud.common.exception.BusinessException;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportDTO;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportVO;
import com.wenjie.cloud.vehiclestatus.entity.StatusReport;
import com.wenjie.cloud.vehiclestatus.repository.StatusReportRepository;
import com.wenjie.cloud.vehiclestatus.service.StatusReportService;
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
 * 状态上报服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatusReportServiceImpl implements StatusReportService {

    private final StatusReportRepository statusReportRepository;

    @Override
    @Transactional
    public StatusReportVO report(StatusReportDTO dto) {
        if (dto.getReportTime().isAfter(Instant.now())) {
            throw new BusinessException(3001, "上报时间不能晚于当前时间");
        }

        var entity = toEntity(dto);
        var saved = statusReportRepository.save(entity);
        log.info("状态上报成功, vin={}, reportTime={}", saved.getVin(), saved.getReportTime());
        return toVO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StatusReportVO> getHistory(String vin, Instant start, Instant end, Pageable pageable) {
        if (start.isAfter(end)) {
            throw new BusinessException(3002, "查询起始时间不能晚于结束时间");
        }

        return statusReportRepository.findByVinAndReportTimeBetween(vin, start, end, pageable)
                .map(this::toVO);
    }

    @Override
    @Transactional(readOnly = true)
    public StatusReportVO getLatestByVin(String vin) {
        if (vin == null || vin.length() != 17) {
            throw new BusinessException(3004, "VIN 格式不正确");
        }

        return statusReportRepository.findFirstByVinOrderByReportTimeDesc(vin)
                .map(this::toVO)
                .orElseThrow(() -> new BusinessException(3003, "该车辆无状态数据"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusReportVO> getLatestAll() {
        return statusReportRepository.findLatestForAllVehicles().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    // ---- 内部转换 ----

    private StatusReport toEntity(StatusReportDTO dto) {
        var entity = new StatusReport();
        entity.setVin(dto.getVin());
        entity.setBatteryLevel(dto.getBatteryLevel());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setMileage(dto.getMileage());
        entity.setSpeed(dto.getSpeed());
        entity.setTemperature(dto.getTemperature());
        entity.setReportTime(dto.getReportTime());
        return entity;
    }

    private StatusReportVO toVO(StatusReport entity) {
        var vo = new StatusReportVO();
        vo.setId(entity.getId());
        vo.setVin(entity.getVin());
        vo.setBatteryLevel(entity.getBatteryLevel());
        vo.setLatitude(entity.getLatitude());
        vo.setLongitude(entity.getLongitude());
        vo.setMileage(entity.getMileage());
        vo.setSpeed(entity.getSpeed());
        vo.setTemperature(entity.getTemperature());
        vo.setReportTime(entity.getReportTime());
        vo.setCreatedAt(entity.getCreatedAt());
        return vo;
    }
}
