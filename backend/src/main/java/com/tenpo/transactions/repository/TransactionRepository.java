package com.tenpo.transactions.repository;

import com.tenpo.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByOrderByTransactionDateDesc();

    List<Transaction> findByTenpistNameContainingIgnoreCase(String tenpistName);
}
