package com.wenjie.cloud.vehiclestatus.service;

import com.wenjie.cloud.vehiclestatus.dto.StatusReportDTO;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

/**
 * 状态上报服务接口
 */
public interface StatusReportService {

    /**
     * 上报车辆状态
     */
    StatusReportVO report(StatusReportDTO dto);

    /**
     * 按 VIN 和时间范围分页查询历史
     */
    Page<StatusReportVO> getHistory(String vin, Instant start, Instant end, Pageable pageable);

    /**
     * 查询某辆车最新状态
     */
    StatusReportVO getLatestByVin(String vin);

    /**
     * 查询所有车辆各自最新状态
     */
    List<StatusReportVO> getLatestAll();
}
