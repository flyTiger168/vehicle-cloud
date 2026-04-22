package com.wenjie.cloud.user.repository;

import com.wenjie.cloud.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 用户数据访问层
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据手机号查询用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 判断手机号是否已存在
     */
    boolean existsByPhone(String phone);
}
