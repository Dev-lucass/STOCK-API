package com.example.estoque_api.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryId implements Serializable {
    private Long userId;
    private Long productId;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
}


