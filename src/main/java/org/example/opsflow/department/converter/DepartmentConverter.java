package org.example.opsflow.department.converter;

import org.example.opsflow.department.entiy.Department;
import org.example.opsflow.department.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DepartmentConverter {
    DepartmentResponse toDepartmentResponse(Department department);
    List<DepartmentResponse> toDepartmentResponseList(List<Department> departments);
}
