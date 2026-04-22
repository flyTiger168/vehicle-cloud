package com.wenjie.cloud.vehiclestatus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportDTO;
import com.wenjie.cloud.vehiclestatus.dto.StatusReportVO;
import com.wenjie.cloud.vehiclestatus.service.StatusReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * StatusReportController 单元测试
 */
@WebMvcTest(StatusReportController.class)
class StatusReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatusReportService statusReportService;

    private static final String BASE_URL = "/api/v1/status-reports";
    private static final String VALID_VIN = "LWVBD1A56NR100001";

    @Test
    void report_withValidBody_shouldReturn200() throws Exception {
        // given
        var dto = new StatusReportDTO();
        dto.setVin(VALID_VIN);
        dto.setBatteryLevel(85);
        dto.setLatitude(30.5728);
        dto.setLongitude(104.0668);
        dto.setMileage(10000.0);
        dto.setSpeed(60.0);
        dto.setTemperature(25.5);
        dto.setReportTime(Instant.parse("2025-06-15T08:00:00Z"));

        var vo = buildVO(1L, VALID_VIN);
        when(statusReportService.report(any(StatusReportDTO.class))).thenReturn(vo);

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.vin").value(VALID_VIN));
    }

    @Test
    void report_withInvalidVin_shouldReturn400() throws Exception {
        // given - VIN 不是 17 位
        var dto = new StatusReportDTO();
        dto.setVin("SHORT");
        dto.setBatteryLevel(85);
        dto.setLatitude(30.5728);
        dto.setLongitude(104.0668);
        dto.setMileage(10000.0);
        dto.setSpeed(60.0);
        dto.setTemperature(25.5);
        dto.setReportTime(Instant.parse("2025-06-15T08:00:00Z"));

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void report_withBatteryLevelOutOfRange_shouldReturn400() throws Exception {
        // given - 电量超过 100
        var dto = new StatusReportDTO();
        dto.setVin(VALID_VIN);
        dto.setBatteryLevel(150);
        dto.setLatitude(30.5728);
        dto.setLongitude(104.0668);
        dto.setMileage(10000.0);
        dto.setSpeed(60.0);
        dto.setTemperature(25.5);
        dto.setReportTime(Instant.parse("2025-06-15T08:00:00Z"));

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHistory_withValidParams_shouldReturn200() throws Exception {
        // given
        var vo = buildVO(1L, VALID_VIN);
        Page<StatusReportVO> page = new PageImpl<>(List.of(vo), PageRequest.of(0, 20), 1);
        when(statusReportService.getHistory(eq(VALID_VIN), any(Instant.class), any(Instant.class), any()))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get(BASE_URL)
                        .param("vin", VALID_VIN)
                        .param("startTime", "2025-06-01T00:00:00Z")
                        .param("endTime", "2025-06-30T23:59:59Z")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.content[0].vin").value(VALID_VIN));
    }

    @Test
    void getLatest_withValidVin_shouldReturn200() throws Exception {
        // given
        var vo = buildVO(5L, VALID_VIN);
        when(statusReportService.getLatestByVin(VALID_VIN)).thenReturn(vo);

        // when & then
        mockMvc.perform(get(BASE_URL + "/latest/" + VALID_VIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(5))
                .andExpect(jsonPath("$.data.vin").value(VALID_VIN));
    }

    @Test
    void getLatestAll_shouldReturn200() throws Exception {
        // given
        var vo1 = buildVO(1L, VALID_VIN);
        var vo2 = buildVO(2L, "LWVBE2B67NR200001");
        when(statusReportService.getLatestAll()).thenReturn(List.of(vo1, vo2));

        // when & then
        mockMvc.perform(get(BASE_URL + "/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    // ---- 辅助方法 ----

    private StatusReportVO buildVO(Long id, String vin) {
        var vo = new StatusReportVO();
        vo.setId(id);
        vo.setVin(vin);
        vo.setBatteryLevel(85);
        vo.setLatitude(30.5728);
        vo.setLongitude(104.0668);
        vo.setMileage(10000.0);
        vo.setSpeed(60.0);
        vo.setTemperature(25.5);
        vo.setReportTime(Instant.parse("2025-06-15T08:00:00Z"));
        vo.setCreatedAt(Instant.parse("2025-06-15T08:00:01Z"));
        return vo;
    }
}
