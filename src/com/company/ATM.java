package com.company;

import java.util.List;

public interface ATM {
    ATMAccount openAccount(int accountId, int accountPin);

    ATMAccount authorize(int accountId, int accountPin);

    int withdraw(int amount);

    int deposit(int amount);

    int balance();

    List<Transaction> history();

    boolean logout();
}
