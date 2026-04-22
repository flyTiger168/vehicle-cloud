package com.wenjie.cloud.user.service;

import com.wenjie.cloud.user.dto.UserDTO;

import java.util.List;

/**
 * 用户管理服务接口
 */
public interface UserService {

    /**
     * 创建用户
     */
    UserDTO createUser(UserDTO dto);

    /**
     * 根据 ID 查询用户
     */
    UserDTO getUserById(Long id);

    /**
     * 查询所有用户列表
     */
    List<UserDTO> listUsers();

    /**
     * 根据 ID 删除用户
     */
    void deleteUser(Long id);
}
