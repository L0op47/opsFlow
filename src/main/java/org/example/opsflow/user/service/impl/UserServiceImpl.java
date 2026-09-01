package org.example.opsflow.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.department.entiy.Department;
import org.example.opsflow.department.mapper.DepartmentMapper;
import org.example.opsflow.user.converter.UserConverter;
import org.example.opsflow.user.dto.AssignDepartmentRequest;
import org.example.opsflow.user.dto.UpdateUserRequest;
import org.example.opsflow.user.dto.UserDetailResponse;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.example.opsflow.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;
    private final UserConverter userConverter;

    @Override
    public UserDetailResponse getUserDetail(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return userConverter.toUserDetailResponse(user);
    }

    @Override
    public UserResponse updateBasicInfo(Long id, UpdateUserRequest request) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        String realName = request.getRealName().trim();
        String email = trimNullable(request.getEmail());
        String phone = trimNullable(request.getPhone());
        if (Objects.equals(user.getRealName(), realName)
                && Objects.equals(user.getEmail(), email)
                && Objects.equals(user.getPhone(), phone)) {
            return userConverter.toUserResponse(user);
        }

        user.setRealName(realName);
        user.setEmail(email);
        user.setPhone(phone);
        int affectedRows = userMapper.updateBasicInfo(user);
        if (affectedRows != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED, "用户基本资料更新失败");
        }

        User savedUser = userMapper.findById(id);
        if (savedUser == null) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED, "用户基本资料更新后查询失败");
        }
        return userConverter.toUserResponse(savedUser);
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        User user = userMapper.findById(id);
        if(user == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if(Objects.equals(user.getStatus(),status)){
            return;
        }
        int affectedRows = userMapper.updateUserStatus(id,status);
        if(affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"用户状态更新失败");
        }

    }

    @Override
    public void assignDepartment(Long id, AssignDepartmentRequest request) {
        User user = userMapper.findById(id);
        if(user == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if(Objects.equals(user.getDepartmentId(),request.getDepartmentId())){
            return;
        }
        Department department = departmentMapper.findById(request.getDepartmentId());
        if(department == null){
            throw new BusinessException(ErrorCode.DEPARTMENT_DISABLED);
        }
        if(department.getStatus() == 0){
            throw new BusinessException(ErrorCode.DEPARTMENT_DISABLED);
        }
        int affectedRows = userMapper.updateUserDepartment(id,request.getDepartmentId());
        if(affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"分配部门失败");
        }
    }

    @Override
    public User getActiveUser(String username) {
        User user = userMapper.findByUsername(username);

        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        if (!Objects.equals(user.getStatus(), 1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        return user;
    }

    @Override
    public PageResponse<UserResponse> getUserPage(int page, int size) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        PageHelper.startPage(page,size);

        List<User> users = userMapper.findAll();

        PageInfo<User> pageInfo = new PageInfo<>(users);

        List<UserResponse> records = userConverter.toUserResponseList(users);

        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size);
    }

    private String trimNullable(String value) {
        return value == null ? null : value.trim();
    }

}
