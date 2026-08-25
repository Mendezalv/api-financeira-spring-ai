package com.dio.budgetapi.repository;

import com.dio.budgetapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCategoryIgnoreCase(String category);
}