package com.wenjie.cloud.vehiclestatus.controller;

import com.wenjie.cloud.common.dto.ApiResponse;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportDTO;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportVO;
import com.wenjie.cloud.vehiclestatus.service.StatusReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

/**
 * 车辆状态上报 REST API
 */
@RestController
@RequestMapping("/api/v1/status-reports")
@RequiredArgsConstructor
public class StatusReportController {

    private final StatusReportService statusReportService;

    /**
     * 上报车辆状态
     */
    @PostMapping
    public ApiResponse<StatusReportVO> report(@Valid @RequestBody StatusReportDTO dto) {
        return ApiResponse.success(statusReportService.report(dto));
    }

    /**
     * 按 VIN 和时间范围分页查询历史
     */
    @GetMapping
    public ApiResponse<Page<StatusReportVO>> getHistory(
            @RequestParam String vin,
            @RequestParam Instant startTime,
            @RequestParam Instant endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reportTime"));
        return ApiResponse.success(statusReportService.getHistory(vin, startTime, endTime, pageable));
    }

    /**
     * 查询某辆车最新状态
     */
    @GetMapping("/latest/{vin}")
    public ApiResponse<StatusReportVO> getLatest(@PathVariable String vin) {
        return ApiResponse.success(statusReportService.getLatestByVin(vin));
    }

    /**
     * 查询所有车辆各自最新状态
     */
    @GetMapping("/latest")
    public ApiResponse<List<StatusReportVO>> getLatestAll() {
        return ApiResponse.success(statusReportService.getLatestAll());
    }
}
