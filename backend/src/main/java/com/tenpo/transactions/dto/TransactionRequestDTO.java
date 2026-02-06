package com.tenpo.transactions.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDTO {

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 0, message = "El monto no puede ser negativo")
    private Integer amount;

    @NotBlank(message = "El giro o comercio es obligatorio")
    @Size(max = 255, message = "El giro o comercio no puede exceder 255 caracteres")
    private String merchantName;

    @NotBlank(message = "El nombre del Tenpista es obligatorio")
    @Size(max = 255, message = "El nombre del Tenpista no puede exceder 255 caracteres")
    private String tenpistName;

    @NotNull(message = "La fecha de transaccion es obligatoria")
    @PastOrPresent(message = "La fecha de transaccion no puede ser futura")
    private LocalDateTime transactionDate;
}
