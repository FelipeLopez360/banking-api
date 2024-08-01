package com.felipelopez.bank_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {

        private Long id;
        private String accountNumber;
        private String ownerName;
        private String ownerEmail;
}
