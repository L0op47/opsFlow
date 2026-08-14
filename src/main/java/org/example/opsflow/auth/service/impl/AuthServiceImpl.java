package org.example.opsflow.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.auth.dto.CurrentUserResponse;
import org.example.opsflow.auth.dto.LoginRequest;
import org.example.opsflow.auth.dto.LoginResponse;
import org.example.opsflow.auth.dto.RegisterRequest;
import org.example.opsflow.auth.service.AuthService;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.security.jwt.JwtUtil;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userMapper.existsByUsername(registerRequest.getUsername()))
            throw new BusinessException(40001,"用户名已存在");
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setRealName(registerRequest.getRealName());
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userMapper.findByUsername(loginRequest.getUsername());
        if(user == null){
            throw new BusinessException(40004,"用户名不存在");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new BusinessException(40002,"密码错误");
        }
        if(user.getStatus()!=1){
            throw new BusinessException(40003,"用户已被禁用");
        }
        String token = jwtUtil.generateToken(user.getId(),user.getUsername());


        return new LoginResponse(token);
    }

    @Override
    public CurrentUserResponse getCurrentUser(String username) {
        User user = userMapper.findByUsername(username);
        if(user == null){
            throw new BusinessException(40004,"用户名不存在");
        }
        return new CurrentUserResponse(user.getId(),user.getUsername(),user.getRealName(),user.getEmail(),user.getPhone(),user.getDepartmentId(),user.getStatus());
    }
}
