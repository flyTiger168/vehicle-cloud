package com.wenjie.cloud.user.controller;

import com.wenjie.cloud.common.dto.ApiResponse;
import com.wenjie.cloud.user.dto.UserDTO;
import com.wenjie.cloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理 REST API
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 创建用户
     */
    @PostMapping
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
        return ApiResponse.success(userService.createUser(dto));
    }

    /**
     * 根据 ID 查询用户
     */
    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUser(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    /**
     * 查询用户列表
     */
    @GetMapping
    public ApiResponse<List<UserDTO>> listUsers() {
        return ApiResponse.success(userService.listUsers());
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }
}