package com.tenpo.transactions.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(Long id) {
        super("Transaccion no encontrada con ID: " + id);
    }
}
