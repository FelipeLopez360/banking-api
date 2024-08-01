package com.felipelopez.bank_service.mapper;

import com.felipelopez.bank_service.model.dto.TransactionReportDTO;
import com.felipelopez.bank_service.model.dto.TransactionRequestDTO;
import com.felipelopez.bank_service.model.dto.TransactionResponseDTO;
import com.felipelopez.bank_service.model.entity.Transaction;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class TransactionMapper {

    private final ModelMapper modelMapper;

    public Transaction convertToEntity(TransactionRequestDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);
    }

    public TransactionResponseDTO convertToDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionResponseDTO.class);
    }

    public List<TransactionResponseDTO> convertToListDTO(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }
    public TransactionReportDTO convertTransactionReportDTO(Object[] transactionData){
        TransactionReportDTO reportDTO = new TransactionReportDTO();
        reportDTO.setDate((LocalDate) transactionData[0]);
        reportDTO.setTransactionCount((long) transactionData[1]);
        return reportDTO;
    }
}
