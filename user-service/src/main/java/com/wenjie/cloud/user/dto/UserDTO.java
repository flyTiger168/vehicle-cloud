package com.wenjie.cloud.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户创建 / 响应 DTO
 */
@Data
public class UserDTO {

    private Long id;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /** 手机号（11 位） */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phone;
}
