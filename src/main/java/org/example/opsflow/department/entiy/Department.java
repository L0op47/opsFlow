package org.example.opsflow.department.entiy;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Department {
    private Long id;
    private String name;
    private String code;
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
