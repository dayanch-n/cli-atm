package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ATM account implementation
 */
public class ATMAccountImpl implements ATMAccount {

    int accountId; // Id of account
    int accountPin; // Would hash this before storing it either here or in the DB if using external libraries
    int balance; // Balance of account
    Stack<Transaction> history; // History of account

    public ATMAccountImpl(int accountId, int accountPin) {
        this.accountId = accountId;
        this.accountPin = accountPin;
        this.balance = 0;
        this.history = new Stack<>();
    }

    /**
     * Get balance of account
     * @return balance
     */
    @Override
    public int getBalance() {
        return this.balance;
    }

    /**
     * Authorize this account
     * @param accountId
     * @param accountPin
     * @return whether or not the authorization was successful
     */
    @Override
    public boolean authorize(int accountId, int accountPin) {
        // Would compare the hashes of the pin instead if using external libraries
        if (this.accountId == accountId && this.accountPin == accountPin) {
            System.out.println(String.format("%s successfully authorized.", accountId));
            return true;
        } else {
            System.out.println("Authorization failed.");
            return false;
        }
    }

    /**
     * Withdraw money from account
     * @param amount
     * @return valance after withdrawl
     */
    @Override
    public int withdraw(int amount) {
        if (this.balance - amount >= 0) {
            this.balance -= amount;
            System.out.println(String.format("Amount dispensed: $%s", amount));
            System.out.println(String.format("Current balance: $%s", this.balance));
        } else if (this.balance - amount < 0) {
            this.balance -= (amount + 5);
            System.out.println(String.format("Amount dispensed: $%s", amount));
            System.out.println(String.format("You have been charged an overdraft fee of $5. Current balance: $%s", this.balance));
        }
        // Add transaction to history
        LocalDateTime localDateTime = LocalDateTime.now();
        Transaction transaction = new Transaction(localDateTime, -amount, this.balance);
        this.history.push(transaction);
        return balance;
    }

    /**
     * Deposit money into the account
     * @param amount
     * @return balance after deposit
     */
    @Override
    public int deposit(int amount) {
        this.balance += amount;
        LocalDateTime localDateTime = LocalDateTime.now();
        Transaction transaction = new Transaction(localDateTime, amount, this.balance);
        this.history.push(transaction);
        System.out.println(String.format("Current balance: $%s", this.balance));
        return this.balance;
    }

    /**
     * Check balance of account
     * @return balance
     */
    @Override
    public int balance() {
        System.out.println(String.format("Current balance: $%s", this.balance));
        return this.balance;
    }

    /**
     * Print history of account
     * @return return history of account
     */
    @Override
    public List<Transaction> history() {
        if (this.history.size() > 0) {
            for (Transaction transaction : this.history) {
                LocalDateTime dateTime = transaction.dateTime;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println(String.format("%s %s %s", dateTime.format(formatter), transaction.amount, transaction.balanceAfterTransaction));
            }
        } else {
            System.out.println("No history found.");
        }
        return new ArrayList(this.history);
    }

    /**
     * Logout of account
     * @return idempotent log out true
     */
    @Override
    public boolean logout() {
        System.out.println(String.format("Account %s logged out.", this.accountId));
        return true;
    }
}
