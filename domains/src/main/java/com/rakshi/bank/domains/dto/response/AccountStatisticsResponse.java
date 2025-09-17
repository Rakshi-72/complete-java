package com.rakshi.bank.domains.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record AccountStatisticsResponse(
        String accountId,
        String accountNumber,

        // Balance statistics
        BigDecimal currentBalance,
        BigDecimal averageMonthlyBalance,
        BigDecimal highestBalance,
        BigDecimal lowestBalance,

        // Transaction statistics
        Long totalTransactions,
        Long totalDebits,
        Long totalCredits,
        BigDecimal totalDebitAmount,
        BigDecimal totalCreditAmount,

        // Monthly breakdown
        Map<String, BigDecimal> monthlySpending, // Month -> Amount
        Map<String, Long> monthlyTransactionCount, // Month -> Count

        // Category wise spending
        Map<String, BigDecimal> categoryWiseSpending, // Category -> Amount

        // Date range
        LocalDate fromDate,
        LocalDate toDate,

        // Limits utilization
        BigDecimal dailyLimitUtilized,
        BigDecimal monthlyLimitUtilized,
        Double dailyLimitUtilizationPercentage,
        Double monthlyLimitUtilizationPercentage
) implements Serializable {
}