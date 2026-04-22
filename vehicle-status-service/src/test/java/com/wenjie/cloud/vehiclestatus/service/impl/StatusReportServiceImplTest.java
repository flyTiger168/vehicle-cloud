package com.wenjie.cloud.vehiclestatus.service.impl;

import com.wenjie.cloud.common.exception.BusinessException;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportDTO;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportVO;
import com.wenjie.cloud.vehiclestatus.entity.StatusReport;
import com.wenjie.cloud.vehiclestatus.repository.StatusReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * StatusReportServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
class StatusReportServiceImplTest {

    @Mock
    private StatusReportRepository statusReportRepository;

    @InjectMocks
    private StatusReportServiceImpl statusReportService;

    private static final String VALID_VIN = "LWVBD1A56NR100001";

    @Test
    void report_withValidDTO_shouldSaveAndReturnVO() {
        // given
        var dto = buildDTO(Instant.now().minus(1, ChronoUnit.HOURS));

        var saved = buildEntity(1L, dto.getVin());
        when(statusReportRepository.save(any(StatusReport.class))).thenReturn(saved);

        // when
        StatusReportVO result = statusReportService.report(dto);

        // then
        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        assertEquals(VALID_VIN, result.getVin());
        verify(statusReportRepository).save(any(StatusReport.class));
    }

    @Test
    void report_withFutureReportTime_shouldThrowBusinessException() {
        // given
        var dto = buildDTO(Instant.now().plus(1, ChronoUnit.HOURS));

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> statusReportService.report(dto));
        assertEquals(3001, ex.getErrorCode());
    }

    @Test
    void getHistory_withValidParams_shouldReturnPageOfVO() {
        // given
        var start = Instant.parse("2025-06-01T00:00:00Z");
        var end = Instant.parse("2025-06-30T23:59:59Z");
        Pageable pageable = PageRequest.of(0, 20);

        var entity = buildEntity(1L, VALID_VIN);
        Page<StatusReport> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(statusReportRepository.findByVinAndReportTimeBetween(VALID_VIN, start, end, pageable))
                .thenReturn(page);

        // when
        Page<StatusReportVO> result = statusReportService.getHistory(VALID_VIN, start, end, pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(VALID_VIN, result.getContent().get(0).getVin());
    }

    @Test
    void getHistory_withStartTimeAfterEndTime_shouldThrowBusinessException() {
        // given
        var start = Instant.parse("2025-06-30T23:59:59Z");
        var end = Instant.parse("2025-06-01T00:00:00Z");
        Pageable pageable = PageRequest.of(0, 20);

        // when & then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> statusReportService.getHistory(VALID_VIN, start, end, pageable));
        assertEquals(3002, ex.getErrorCode());
    }

    @Test
    void getLatestByVin_withExistingData_shouldReturnLatestVO() {
        // given
        var entity = buildEntity(5L, VALID_VIN);
        when(statusReportRepository.findFirstByVinOrderByReportTimeDesc(VALID_VIN))
                .thenReturn(Optional.of(entity));

        // when
        StatusReportVO result = statusReportService.getLatestByVin(VALID_VIN);

        // then
        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals(VALID_VIN, result.getVin());
    }

    @Test
    void getLatestByVin_withNoData_shouldThrowBusinessException() {
        // given
        when(statusReportRepository.findFirstByVinOrderByReportTimeDesc(VALID_VIN))
                .thenReturn(Optional.empty());

        // when & then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> statusReportService.getLatestByVin(VALID_VIN));
        assertEquals(3003, ex.getErrorCode());
    }

    @Test
    void getLatestAll_shouldReturnList() {
        // given
        var entity1 = buildEntity(1L, VALID_VIN);
        var entity2 = buildEntity(2L, "LWVBE2B67NR200001");
        when(statusReportRepository.findLatestForAllVehicles()).thenReturn(List.of(entity1, entity2));

        // when
        List<StatusReportVO> result = statusReportService.getLatestAll();

        // then
        assertEquals(2, result.size());
        verify(statusReportRepository).findLatestForAllVehicles();
    }

    // ---- 辅助方法 ----

    private StatusReportDTO buildDTO(Instant reportTime) {
        var dto = new StatusReportDTO();
        dto.setVin(VALID_VIN);
        dto.setBatteryLevel(85);
        dto.setLatitude(30.5728);
        dto.setLongitude(104.0668);
        dto.setMileage(10000.0);
        dto.setSpeed(60.0);
        dto.setTemperature(25.5);
        dto.setReportTime(reportTime);
        return dto;
    }

    private StatusReport buildEntity(Long id, String vin) {
        var entity = new StatusReport();
        entity.setId(id);
        entity.setVin(vin);
        entity.setBatteryLevel(85);
        entity.setLatitude(30.5728);
        entity.setLongitude(104.0668);
        entity.setMileage(10000.0);
        entity.setSpeed(60.0);
        entity.setTemperature(25.5);
        entity.setReportTime(Instant.parse("2025-06-15T08:00:00Z"));
        entity.setCreatedAt(Instant.parse("2025-06-15T08:00:01Z"));
        return entity;
    }
}
