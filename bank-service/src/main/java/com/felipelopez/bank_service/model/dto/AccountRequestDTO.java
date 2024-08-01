package com.felipelopez.bank_service.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {

    @NotBlank(message = "El numero de cuenta no puede estar vacio")
    @Size(min = 5, max = 20, message = "El numero de cuenta debe tener entre 5 y 20 caracteres")
    @Pattern(regexp = "[0-9]+", message = "El numero de cuenta solo puede contener numeros")
    private String accountNumber;

    @NotNull(message = "El saldo no puede estar vacio")
    private BigDecimal balance;

    @NotNull(message = "El nombre del titular de cuenta no puede estar vacio")
    private String ownerName;

    @NotNull(message = "El email del titular de cuenta no puede estar vacio")
    @Email(message = "El email del titular de cuenta no es valido")
    private String ownerEmail;
}
