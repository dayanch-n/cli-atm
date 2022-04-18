package com.company;

import java.util.List;

public interface ATMAccount {
    boolean authorize(int accountId, int accountPin);

    int withdraw(int amount);

    int deposit(int amount);

    int balance();

    int getBalance();

    List<Transaction> history();

    boolean logout();
}
