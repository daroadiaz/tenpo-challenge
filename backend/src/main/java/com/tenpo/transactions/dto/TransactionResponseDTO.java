package com.tenpo.transactions.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private Long id;
    private Integer amount;
    private String merchantName;
    private String tenpistName;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
}
