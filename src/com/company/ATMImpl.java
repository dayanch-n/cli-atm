package com.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * ATM Machine Implementation - Basically a wrapper for ATMAccount with auth and some logic at the ATM layer
 */
public class ATMImpl implements ATM {

    Map<Integer, ATMAccount> accounts; // All accounts registered on the ATM
    int amountInAtm; // Total amount in ATM available to withdraw
    ATMAccount authorizedAccount; // The current authorized account, only 1 account at a time can be authorized

    public ATMImpl() {
        this.accounts = new HashMap<>();
        this.amountInAtm = 10000;
        this.authorizedAccount = null;
    }

    /**
     * Get ATMAccount
     * @param accountId
     * @return ATMAccount
     */
    private ATMAccount getAccount(int accountId) {
        return this.accounts.get(accountId);
    }

    /**
     * Method to open an account on this ATM
     * @param accountId
     * @param accountPin
     * @return ATMAccount instance
     */
    @Override
    public ATMAccount openAccount(int accountId, int accountPin) {
        if (getAccount(accountId) == null) {
            ATMAccount account = new ATMAccountImpl(accountId, accountPin);
            this.accounts.put(accountId, account);
            System.out.println(String.format("Account %s created.", accountId));
            return account;
        } else {
            System.out.println("Account already exists.");
            return getAccount(accountId);
        }
    }

    /**
     * Login method for the ATM
     * @param accountId
     * @param accountPin
     * @return
     */
    @Override
    public ATMAccount authorize(int accountId, int accountPin) {
        if (this.authorizedAccount == null) { // See if there is an account currently logged in
            if (getAccount(accountId) != null) { // If there is an account with that Id found, try to log in
                ATMAccount account = getAccount(accountId);
                if (account.authorize(accountId, accountPin)) { // Login
                    this.authorizedAccount = account; // Set this as the current authorized account
                    return account;
                } else {
                    return null;
                }
            } else {
                System.out.println(String.format("Account %s does not already exist. Must open that account first.", accountId));
                return null;
            }
        } else {
            System.out.println("Another account is already logged in, please logout of it first.");
            return this.authorizedAccount;
        }
    }

    /**
     * Withdraw method for the ATM
     * @param amount
     * @return balance after withdrawal
     */
    @Override
    public int withdraw(int amount) {
        if (this.authorizedAccount != null) {
            if (amount % 10 != 0) { // Assure the withdrawal request is in multiples of $20
                System.out.println("Amount of withdrawal has to be in multiples of $10.");
                return this.authorizedAccount.getBalance();
            } else if (this.authorizedAccount.getBalance() < 0) { // Check to make sure the account isn't overdrawn
                System.out.println("Your account is overdrawn! You may not make withdrawals at this time.");
                return this.authorizedAccount.getBalance();
            } else if (this.amountInAtm <= 0) { // Check to make sure there's enough money in the ATM
                System.out.println("Unable to process your withdrawal at this time.");
                return this.authorizedAccount.getBalance();
            } else if (amount > this.amountInAtm) { // If amount is greater than amount in ATM, only let them withdraw however much is in ATM
                amount = this.amountInAtm;
                this.amountInAtm -= amount;
                System.out.println("Unable to dispense full amount requested at this time.");
                return this.authorizedAccount.withdraw(amount);
            } else {
                this.amountInAtm -= amount;
                return this.authorizedAccount.withdraw(amount);
            }
        } else {
            System.out.println("Account not authorized, please authorize first!");
            return 0;
        }
    }

    /**
     * Deposit money into the ATM/account
     * @param amount
     * @return
     */
    @Override
    public int deposit(int amount) {
        if (this.authorizedAccount != null) {
            this.amountInAtm += amount;
            return this.authorizedAccount.deposit(amount);
        } else {
            System.out.println("Account not authorized, please authorize first!");
            return 0;
        }
    }

    /**
     * Check balance in account
     * @return balance
     */
    @Override
    public int balance() {
        if (this.authorizedAccount != null) {
            return this.authorizedAccount.balance();
        } else {
            System.out.println("Account not authorized, please authorize first!");
            return 0;
        }
    }

    /**
     * Check the history of transactions
     * @return transactions
     */
    @Override
    public List<Transaction> history() {
        if (this.authorizedAccount != null) {
            return this.authorizedAccount.history();
        } else {
            System.out.println("Account not authorized, please authorize first!");
            return new Stack<>();
        }
    }

    /**
     * Log out of account
     * @return
     */
    @Override
    public boolean logout() {
        if (this.authorizedAccount != null) {
            this.authorizedAccount.logout();
            this.authorizedAccount = null;
            return true;
        } else {
            System.out.println("No account is currently authorized.");
            return false;
        }
    }
}
