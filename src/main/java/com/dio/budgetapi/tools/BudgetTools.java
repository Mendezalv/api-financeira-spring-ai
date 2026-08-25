package com.dio.budgetapi.tools;

import com.dio.budgetapi.model.Transaction;
import com.dio.budgetapi.repository.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@Configuration
public class BudgetTools {

    public record SaveTransactionRequest(String description, BigDecimal amount, String category) {}
    public record BudgetCheckRequest(String category) {}

    private static final BigDecimal MONTHLY_LIMIT = new BigDecimal("1000.00");

    @Bean
    @Description("Salva uma nova transação financeira. Exige descrição, valor e categoria (ex: Alimentação, Transporte, Lazer).")
    public Function<SaveTransactionRequest, String> saveTransaction(TransactionRepository repository) {
        return request -> {
            Transaction transaction = new Transaction(request.description(), request.amount(), request.category());
            repository.save(transaction);
            return String.format("Transação salva com sucesso. ID: %d", transaction.getId());
        };
    }

    @Bean
    @Description("Verifica o total gasto em uma categoria específica e compara com o limite mensal estabelecido.")
    public Function<BudgetCheckRequest, String> checkBudget(TransactionRepository repository) {
        return request -> {
            List<Transaction> transactions = repository.findByCategoryIgnoreCase(request.category());
            BigDecimal totalSpent = transactions.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal remaining = MONTHLY_LIMIT.subtract(totalSpent);

            return String.format("Gasto total na categoria '%s': R$ %.2f. Limite mensal: R$ %.2f. Saldo restante: R$ %.2f.",
                    request.category(), totalSpent, MONTHLY_LIMIT, remaining);
        };
    }
}