package com.company;

import java.time.LocalDateTime;

/**
 * Transaction Data Class
 */
public class Transaction {
    LocalDateTime dateTime; // Date time of the transaction
    int amount; // Amount of transaction
    int balanceAfterTransaction; // Balance after transacton

    public Transaction(LocalDateTime dateTime, int amount, int balanceAfterTransaction) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }
}
