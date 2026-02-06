package com.tenpo.transactions.service;

import com.tenpo.transactions.dto.TransactionRequestDTO;
import com.tenpo.transactions.dto.TransactionResponseDTO;
import com.tenpo.transactions.entity.Transaction;
import com.tenpo.transactions.exception.TransactionNotFoundException;
import com.tenpo.transactions.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private TransactionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .id(1L)
                .amount(15000)
                .merchantName("Supermercado Lider")
                .tenpistName("Juan Perez")
                .transactionDate(LocalDateTime.now().minusHours(1))
                .createdAt(LocalDateTime.now())
                .build();

        requestDTO = TransactionRequestDTO.builder()
                .amount(15000)
                .merchantName("Supermercado Lider")
                .tenpistName("Juan Perez")
                .transactionDate(LocalDateTime.now().minusHours(1))
                .build();
    }

    @Test
    void getAllTransactions_ReturnsListOfTransactions() {
        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .amount(25000)
                .merchantName("Farmacia Cruz Verde")
                .tenpistName("Maria Garcia")
                .transactionDate(LocalDateTime.now().minusHours(2))
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionRepository.findAllByOrderByTransactionDateDesc())
                .thenReturn(Arrays.asList(transaction, transaction2));

        List<TransactionResponseDTO> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transaction.getId(), result.get(0).getId());
        verify(transactionRepository, times(1)).findAllByOrderByTransactionDateDesc();
    }

    @Test
    void getAllTransactions_ReturnsEmptyList() {
        when(transactionRepository.findAllByOrderByTransactionDateDesc())
                .thenReturn(List.of());

        List<TransactionResponseDTO> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTransactionById_ExistingId_ReturnsTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionResponseDTO result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getMerchantName(), result.getMerchantName());
    }

    @Test
    void getTransactionById_NonExistingId_ThrowsException() {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getTransactionById(999L);
        });
    }

    @Test
    void createTransaction_ValidRequest_ReturnsCreatedTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponseDTO result = transactionService.createTransaction(requestDTO);

        assertNotNull(result);
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getMerchantName(), result.getMerchantName());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_ExistingId_DeletesSuccessfully() {
        when(transactionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(transactionRepository).deleteById(1L);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(1L));
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTransaction_NonExistingId_ThrowsException() {
        when(transactionRepository.existsById(999L)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction(999L);
        });
        verify(transactionRepository, never()).deleteById(any());
    }
}
