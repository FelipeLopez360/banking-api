package com.felipelopez.bank_service.service;

import com.felipelopez.bank_service.exception.BadRequestException;
import com.felipelopez.bank_service.exception.ResourceNotFoundException;
import com.felipelopez.bank_service.mapper.TransactionMapper;
import com.felipelopez.bank_service.model.dto.TransactionReportDTO;
import com.felipelopez.bank_service.model.dto.TransactionRequestDTO;
import com.felipelopez.bank_service.model.dto.TransactionResponseDTO;
import com.felipelopez.bank_service.model.entity.Account;
import com.felipelopez.bank_service.model.entity.Transaction;
import com.felipelopez.bank_service.repository.AccountRepository;
import com.felipelopez.bank_service.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findBySourceOrTargetAccountNumber(accountNumber);
        return transactionMapper.convertToListDTO(transactions);
    }

    @Transactional(readOnly = true)
    public List<TransactionReportDTO> generateTransactionReport(String startDateStr, String endDateStr, String accountNumber) {

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        List<Object[]> transactionData = transactionRepository.getTransactionCountByDateRangeAdnAccountNumber(startDate, endDate, accountNumber);
        return transactionData.stream()
                .map(transactionMapper::convertTransactionReportDTO)
                .toList();
    }

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO) {
        // Obtener las cuentas involucradas en la transacción y verificar si existen
        Account sourceAccount = accountRepository.findByAccountNumber(String.valueOf(transactionDTO.getSourceAccountNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de origen no existe"));

        Account targetAccount = accountRepository.findByAccountNumber(String.valueOf(transactionDTO.getTargetAccountNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de destino no existe"));

        // Verificar si el saldo de la cuenta de origen es suficiente para realizar la transacción
        BigDecimal amount = transactionDTO.getAmount();
        BigDecimal sourceAccountBalance = sourceAccount.getBalance();
        if (sourceAccountBalance.compareTo(amount) < 0) {
            throw new BadRequestException("Saldo insuficiente en la cuenta de origen:" + sourceAccount.getAccountNumber());
        }

        Transaction transaction = transactionMapper.convertToEntity(transactionDTO);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction = transactionRepository.save(transaction);

        // Actualizar los saldos de las cuentas
        BigDecimal newSourceAccountBalance = sourceAccountBalance.subtract(amount);
        BigDecimal newTargetAccountBalance = targetAccount.getBalance().add(amount);

        sourceAccount.setBalance(newSourceAccountBalance);
        targetAccount.setBalance(newTargetAccountBalance);

        // Guardar los cambios en las cuentas
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return transactionMapper.convertToDTO(transaction);
    }

}
