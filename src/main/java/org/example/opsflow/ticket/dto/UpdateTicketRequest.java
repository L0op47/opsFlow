package org.example.opsflow.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.opsflow.ticket.enums.TicketPriority;

@Data
public class UpdateTicketRequest {
    @NotBlank
    @Size(max = 100,message = "标题长度不可以超过100个字符")
    private String title;
    @NotBlank
    @Size(max = 200,message = "描述长度不可以超过200个字符")
    private String description;
    @NotBlank
    @Size(max=32,message = "类型名字不能超过32个字符")
    private String category;

    private TicketPriority priority;
}
