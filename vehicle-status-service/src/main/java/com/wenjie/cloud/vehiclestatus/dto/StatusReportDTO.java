package com.wenjie.cloud.vehiclestatus.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * 状态上报入参 DTO
 */
@Data
public class StatusReportDTO {

    /** VIN 车辆识别码（17 位） */
    @NotBlank(message = "VIN 不能为空")
    @Size(min = 17, max = 17, message = "VIN 必须为 17 位")
    private String vin;

    /** 电池电量（0~100） */
    @NotNull(message = "电池电量不能为空")
    @Min(value = 0, message = "电池电量最小为 0")
    @Max(value = 100, message = "电池电量最大为 100")
    private Integer batteryLevel;

    /** 纬度 */
    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90", message = "纬度最小为 -90")
    @DecimalMax(value = "90", message = "纬度最大为 90")
    private Double latitude;

    /** 经度 */
    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180", message = "经度最小为 -180")
    @DecimalMax(value = "180", message = "经度最大为 180")
    private Double longitude;

    /** 总里程（km） */
    @NotNull(message = "里程不能为空")
    @DecimalMin(value = "0", message = "里程不能为负数")
    private Double mileage;

    /** 车速（km/h） */
    @NotNull(message = "车速不能为空")
    @DecimalMin(value = "0", message = "车速不能为负数")
    private Double speed;

    /** 温度（℃） */
    @NotNull(message = "温度不能为空")
    private Double temperature;

    /** 上报时间 */
    @NotNull(message = "上报时间不能为空")
    private Instant reportTime;
}
