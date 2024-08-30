package com.felipelopez.bank_service.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

        private Long id;

        @NotNull(message = "El numero de la cuenta de origen no puede estar vacio")
        private Long sourceAccountNumber;

        @NotNull(message = "El numero de la cuenta de destino no puede estar vacio")
        private Long targetAccountNumber;

        @NotNull(message = "El monto de la transaccion no puede estar vacio")
        @DecimalMin(value = "0.01", message = "El monto de la transaccion debe ser mayor a cero")
        private BigDecimal amount;

        @NotBlank(message = "La descripcion de la transaccion no puede estar vacia")
        private String description;
}
