package com.wenjie.cloud.vehicle.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 车辆创建 / 响应 DTO
 */
@Data
public class VehicleDTO {

    private Long id;

    /** VIN 车辆识别码（17 位） */
    @NotBlank(message = "VIN 不能为空")
    @Size(min = 17, max = 17, message = "VIN 必须为 17 位")
    private String vin;

    /** 车型 */
    @NotBlank(message = "车型不能为空")
    private String model;

    /** 关联车主 ID */
    private Long ownerUserId;
}
