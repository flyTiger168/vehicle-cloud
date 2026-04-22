package com.wenjie.cloud.user.service.impl;

import com.wenjie.cloud.common.exception.BusinessException;
import com.wenjie.cloud.user.dto.UserDTO;
import com.wenjie.cloud.user.entity.User;
import com.wenjie.cloud.user.repository.UserRepository;
import com.wenjie.cloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new BusinessException(2001, "手机号已存在: " + dto.getPhone());
        }

        var entity = new User();
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setCreatedAt(Instant.now());

        var saved = userRepository.save(entity);
        log.info("用户创建成功, id={}, phone={}", saved.getId(), saved.getPhone());
        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        var entity = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(2002, "用户不存在, id=" + id));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException(2002, "用户不存在, id=" + id);
        }
        userRepository.deleteById(id);
        log.info("用户删除成功, id={}", id);
    }

    // ---- 内部转换 ----

    private UserDTO toDTO(User entity) {
        var dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        return dto;
    }
}
