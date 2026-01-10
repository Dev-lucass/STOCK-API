package com.example.estoque_api.dto.response.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record ToolFilterResponseDTO(long id,
                                    String name,
                                    boolean active,
                                    boolean inUse,
                                    int usageCount,
                                    @JsonFormat(pattern = "HH:mm:ss")
                                    @JsonInclude(JsonInclude.Include.NON_NULL)
                                    LocalTime usageTime,
                                    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                    LocalDateTime createdAt){}
