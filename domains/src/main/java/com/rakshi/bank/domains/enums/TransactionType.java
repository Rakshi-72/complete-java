package com.rakshi.bank.domains.enums;

import java.io.Serializable;

public enum TransactionType implements Serializable {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER_IN,
    TRANSFER_OUT,
    INTEREST_CREDIT,
    FEE_DEBIT,
    PENALTY,
    REFUND,
    CASHBACK,
    DIVIDEND,
    LOAN_DISBURSEMENT,
    LOAN_REPAYMENT,
    ATM_WITHDRAWAL,
    ONLINE_PAYMENT,
    CARD_PAYMENT,
    UPI_PAYMENT,
    NEFT,
    RTGS,
    IMPS,
    CHEQUE_DEPOSIT,
    CHEQUE_WITHDRAWAL,
    STANDING_INSTRUCTION,
    AUTO_DEBIT,
    REVERSAL
}