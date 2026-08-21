package org.example.opsflow.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.department.entiy.Department;
import org.example.opsflow.department.mapper.DepartmentMapper;
import org.example.opsflow.user.dto.AssignDepartmentRequest;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.example.opsflow.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.opsflow.common.utils.Utils.toUserResponse;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;

    @Override
    public void updateUserStatus(Long id, Integer status) {
        User user = userMapper.findById(id);
        if(user == null){
            throw new BusinessException(40006,"用户不存在");
        }
        if(Objects.equals(user.getStatus(),status)){
            return;
        }
        int affectedRows = userMapper.updateUserStatus(id,status);
        if(affectedRows != 1){
            throw new BusinessException(50001,"用户状态修改失败");
        }

    }

    @Override
    public void assignDepartment(Long id, AssignDepartmentRequest request) {
        User user = userMapper.findById(id);
        if(user == null){
            throw new BusinessException(40006,"用户不存在");
        }
        if(Objects.equals(user.getDepartmentId(),request.getDepartmentId())){
            return;
        }
        Department department = departmentMapper.findById(request.getDepartmentId());
        if(department == null){
            throw new BusinessException(40008,"部门不存在");
        }
        if(department.getStatus() == 0){
            throw new BusinessException(40009,"部门已经被禁用");
        }
        int affectedRows = userMapper.updateUserDepartment(id,request.getDepartmentId());
        if(affectedRows != 1){
            throw new BusinessException(50001,"用户部门修改失败");
        }
    }

    @Override
    public PageResponse<UserResponse> getUserPage(int page, int size) {
        if(page < 1){
            throw new BusinessException(40004,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(40004,"每页的数量必须在1-100之间");
        }

        PageHelper.startPage(page,size);

        List<User> users = userMapper.findAll();

        PageInfo<User> pageInfo = new PageInfo<>(users);

        List<UserResponse> records = new ArrayList<>();

        for(User user : users){
            records.add(toUserResponse(user));
        }

        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size);
    }

}
