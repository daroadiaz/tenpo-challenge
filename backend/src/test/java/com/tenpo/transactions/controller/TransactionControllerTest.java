package com.tenpo.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tenpo.transactions.dto.TransactionRequestDTO;
import com.tenpo.transactions.dto.TransactionResponseDTO;
import com.tenpo.transactions.exception.GlobalExceptionHandler;
import com.tenpo.transactions.exception.TransactionNotFoundException;
import com.tenpo.transactions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;
    private TransactionResponseDTO responseDTO;
    private TransactionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        responseDTO = TransactionResponseDTO.builder()
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
    void getAllTransactions_ReturnsOkWithList() throws Exception {
        TransactionResponseDTO responseDTO2 = TransactionResponseDTO.builder()
                .id(2L)
                .amount(25000)
                .merchantName("Farmacia")
                .tenpistName("Maria Garcia")
                .transactionDate(LocalDateTime.now().minusHours(2))
                .build();

        when(transactionService.getAllTransactions())
                .thenReturn(Arrays.asList(responseDTO, responseDTO2));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllTransactions_ReturnsEmptyList() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getTransactionById_ExistingId_ReturnsOk() throws Exception {
        when(transactionService.getTransactionById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(15000));
    }

    @Test
    void getTransactionById_NonExistingId_ReturnsNotFound() throws Exception {
        when(transactionService.getTransactionById(999L))
                .thenThrow(new TransactionNotFoundException(999L));

        mockMvc.perform(get("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_ValidRequest_ReturnsCreated() throws Exception {
        when(transactionService.createTransaction(any(TransactionRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(15000));
    }

    @Test
    void createTransaction_NegativeAmount_ReturnsBadRequest() throws Exception {
        TransactionRequestDTO invalidRequest = TransactionRequestDTO.builder()
                .amount(-100)
                .merchantName("Test")
                .tenpistName("Test User")
                .transactionDate(LocalDateTime.now().minusHours(1))
                .build();

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTransaction_FutureDate_ReturnsBadRequest() throws Exception {
        TransactionRequestDTO invalidRequest = TransactionRequestDTO.builder()
                .amount(1000)
                .merchantName("Test")
                .tenpistName("Test User")
                .transactionDate(LocalDateTime.now().plusDays(1))
                .build();

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTransaction_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction(1L);
    }

    @Test
    void deleteTransaction_NonExistingId_ReturnsNotFound() throws Exception {
        doThrow(new TransactionNotFoundException(999L))
                .when(transactionService).deleteTransaction(999L);

        mockMvc.perform(delete("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }
}
