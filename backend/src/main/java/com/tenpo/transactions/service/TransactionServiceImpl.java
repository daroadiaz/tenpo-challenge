package com.tenpo.transactions.service;

import com.tenpo.transactions.dto.TransactionRequestDTO;
import com.tenpo.transactions.dto.TransactionResponseDTO;
import com.tenpo.transactions.entity.Transaction;
import com.tenpo.transactions.exception.TransactionNotFoundException;
import com.tenpo.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAllByOrderByTransactionDateDesc()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return toResponseDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO request) {
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .merchantName(request.getMerchantName())
                .tenpistName(request.getTenpistName())
                .transactionDate(request.getTransactionDate())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        transaction.setAmount(request.getAmount());
        transaction.setMerchantName(request.getMerchantName());
        transaction.setTenpistName(request.getTenpistName());
        transaction.setTransactionDate(request.getTransactionDate());

        Transaction updated = transactionRepository.save(transaction);
        return toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException(id);
        }
        transactionRepository.deleteById(id);
    }

    private TransactionResponseDTO toResponseDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .merchantName(transaction.getMerchantName())
                .tenpistName(transaction.getTenpistName())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
