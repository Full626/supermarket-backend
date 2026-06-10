package com.service;

import com.domain.User;
import com.mapper.UserMapper;
import com.util.MD5Util;  // 改用 MD5Util
import com.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录（使用 MD5 验证）
     */
    public User login(String userId, String rawPassword) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            return null;
        }
        if (MD5Util.matches(rawPassword, user.getPwd())) {
            return user;
        }
        return null;
    }

    /**
     * 生成JWT token
     */
    public String generateToken(String userId) {
        return jwtUtil.generateToken(userId);
    }

    /**
     * 获取所有用户（不包含密码）
     */
    public List<User> getAllUsers() {
        List<User> users = userMapper.findAll();
        return users != null ? users : new ArrayList<>();
    }

    /**
     * 根据ID查询用户（不包含密码）
     */
    public User getUserById(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user != null) {
            user.setPwd(null);
        }
        return user;
    }

    /**
     * 检查用户是否存在
     */
    public boolean existsById(String userId) {
        return userMapper.countByUserId(userId) > 0;
    }

    /**
     * 新增用户（使用 MD5 加密密码）
     */
    public boolean createUser(User user) {
        if (user == null || user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new RuntimeException("用户信息不完整");
        }
        if (existsById(user.getUserId())) {
            throw new RuntimeException("用户名已存在：" + user.getUserId());
        }
        user.setPwd(MD5Util.encode(user.getPwd()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("cashier");
        }
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 更新用户（如果密码有变更则重新加密）
     */
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new RuntimeException("用户信息不完整");
        }
        if (!existsById(user.getUserId())) {
            throw new RuntimeException("用户不存在：" + user.getUserId());
        }
        if (user.getPwd() != null && !user.getPwd().isEmpty()) {
            user.setPwd(MD5Util.encode(user.getPwd()));
        }
        return userMapper.updateUser(user) > 0;
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (!existsById(userId)) {
            throw new RuntimeException("用户不存在：" + userId);
        }
        return userMapper.deleteUser(userId) > 0;
    }

    /**
     * 根据角色查询用户
     */
    public List<User> getUsersByRole(String role) {
        List<User> allUsers = getAllUsers();
        if (role == null || role.isEmpty()) {
            return allUsers;
        }
        return allUsers.stream()
                .filter(user -> role.equals(user.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * 修改用户密码
     */
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = login(userId, oldPassword);
        if (user == null) {
            throw new RuntimeException("原密码错误");
        }
        user.setPwd(MD5Util.encode(newPassword));
        return userMapper.updateUser(user) > 0;
    }
}