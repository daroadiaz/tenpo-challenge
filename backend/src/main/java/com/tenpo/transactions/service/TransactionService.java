package com.tenpo.transactions.service;

import com.tenpo.transactions.dto.TransactionRequestDTO;
import com.tenpo.transactions.dto.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {

    List<TransactionResponseDTO> getAllTransactions();

    TransactionResponseDTO getTransactionById(Long id);

    TransactionResponseDTO createTransaction(TransactionRequestDTO request);

    void deleteTransaction(Long id);
}
