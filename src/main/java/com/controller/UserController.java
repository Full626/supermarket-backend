package com.controller;

import com.annotation.RoleRequired;
import com.common.Result;
import com.constant.Constants;
import com.domain.User;
import com.dto.request.UserCreateRequestDTO;
import com.dto.request.UserLoginRequestDTO;
import com.dto.request.UserUpdateRequestDTO;
import com.dto.response.LoginResponseDTO;
import com.dto.response.UserResponseDTO;
import com.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request) {
        User user = userService.login(request.getUserId(), request.getPwd());

        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        String token = userService.generateToken(user.getUserId());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setToken(token);
        response.setRole(user.getRole());

        return Result.success("登录成功", response);
    }

    @RoleRequired(Constants.ROLE_ADMIN)
    @GetMapping("/list")
    public Result<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> dtoList = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(dtoList);
    }

    @GetMapping("/info/{userId}")
    public Result<UserResponseDTO> getUserInfo(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(convertToDTO(user));
    }

    @RoleRequired(Constants.ROLE_ADMIN)
    @PostMapping("/add")
    public Result<Void> addUser(@Valid @RequestBody UserCreateRequestDTO request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setPwd(request.getPwd());
        user.setUserName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setIdCard(request.getIdCard());
        user.setRole(request.getRole());

        userService.createUser(user);
        return Result.success("添加成功", null);
    }

    @RoleRequired(Constants.ROLE_ADMIN)
    @PutMapping("/update/{userId}")
    public Result<Void> updateUser(@PathVariable String userId,
                                   @RequestBody UserUpdateRequestDTO request) {
        User user = new User();
        user.setUserId(userId);
        user.setUserName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setIdCard(request.getIdCard());
        user.setPwd(request.getPwd());
        user.setRole(request.getRole());

        userService.updateUser(user);
        return Result.success("更新成功", null);
    }

    @RoleRequired(Constants.ROLE_ADMIN)
    @DeleteMapping("/{userId}")
    public Result<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return Result.success("删除成功", null);
    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setPhone(user.getPhone());
        dto.setIdCard(user.getIdCard());
        dto.setRole(user.getRole());
        return dto;
    }
}