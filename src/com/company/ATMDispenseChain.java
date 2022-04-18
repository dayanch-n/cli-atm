package com.company;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ATMDispenseChain {

    // New ATM instance
    private static ATM atm = new ATMImpl();
    private DispenseChain c1;

    public ATMDispenseChain() {
        // initialize the chain
        this.c1 = new Dispenser50();
        DispenseChain c2 = new Dispenser20();
        DispenseChain c3 = new Dispenser10();

        // set the chain of responsibility
        c1.setNextChain(c2);
        c2.setNextChain(c3);
    }

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        ATMMachine atmMachine = new ATMMachine();


        // Have to create an account before being able to access it
        System.out.println("ATM Machine Current state : "
                + atmMachine.getAtmMachineState().getClass().getName());

        System.out.println("Welcome! Enter credentials to open an account...");
        boolean accountCreated = false;
        while (!accountCreated) {
            try {
                System.out.print("AccountID: ");
                int accountId = Integer.parseInt(in.next());
                System.out.print("AccountPin: ");
                int accountPin = Integer.parseInt(in.next());
                if (accountId < 0 || accountPin < 0) {
                    throw new NumberFormatException("Inputs have to be positive");
                }
                atm.openAccount(accountId, accountPin);
                accountCreated = true;
                atmMachine.insertDebitCard();

                /*
                 * Debit Card has been inserted so internal state of ATM
                 * has been changed to 'hasDebitCardState'
                 */

                System.out.println("ATM Machine Current state : "
                        + atmMachine.getAtmMachineState().getClass().getName());
            } catch (Exception e) {
                System.out.println("Inputs have to be valid positive integers");
            }
        }

        // Timer for 2 min timeout
        Timer timer = new Timer();
        timer.schedule(createTask(), 120000);

        System.out.println("Available Commands");
        System.out.println("1. authorize");
        System.out.println("2. open");
        System.out.println("3. withdraw");
        System.out.println("4. deposit");
        System.out.println("5. balance");
        System.out.println("6. history");
        System.out.println("7. logout");
        System.out.println("8. end");

        while (in.hasNextLine()) {
            timer.cancel(); // Cancel the timer once the input is registered
            String[] input = in.nextLine().split(" "); // Split by whitespace for command amount inputs
            String command = input[0]; // Get the command


            switch (command) {
                case "authorize":
                    // Validate the input
                    try {
                        int accountIdReauth = Integer.parseInt(input[1]);
                        int accountPinReauth = Integer.parseInt(input[2]);
                        if (accountIdReauth < 0 || accountPinReauth < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }
                        // Call the authorize method
                        atm.authorize(accountIdReauth, accountPinReauth);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: authorize <accountId> <accountPin>");
                    }
                    break;
                case "open":
                    // Validate the input
                    try {
                        int accountIdReauth = Integer.parseInt(input[1]);
                        int accountPinReauth = Integer.parseInt(input[2]);
                        if (accountIdReauth < 0 || accountPinReauth < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }
                        // Call the openAccount method
                        atm.openAccount(accountIdReauth, accountPinReauth);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: open <accountId> <accountPin>");
                    }
                    break;
                case "withdraw":
                    // Validate the input
                    try {

                        ATMDispenseChain atmDispenser = new ATMDispenseChain();


                        int amount = Integer.parseInt(input[1]);
                        if (amount < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }

                        if (amount % 10 != 0) {
                            System.out.println("Amount should be in multiple of 10s.");
                            throw new NumberFormatException("Amount should be in multiple of 10s");
                        }
                        // process the request
                        atmDispenser.c1.dispense(new Currency(amount));

                        // Call the withdrawal method
                        atm.withdraw(amount);


                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: withdraw <amount>");
                    }
                    break;
                case "deposit":
                    // Validate the input
                    try {
                        int amount = Integer.parseInt(input[1]);

                        ATMDispenseChain atmDispenser = new ATMDispenseChain();

                        if (amount < 0) {
                            System.out.println("Inputs have to be positive");
                            throw new NumberFormatException("Inputs have to be positive");
                        }

                        if (amount % 10 != 0) {
                            System.out.println("Amount should be in multiple of 10s.");
                            throw new NumberFormatException("Amount should be in multiple of 10s");
                        }
                        // process the request
                        atmDispenser.c1.dispense(new Currency(amount));

                        // Call the deposit method
                        atm.deposit(amount);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("Invalid input! Please try again...\nFormat is: deposit <amount>");
                    }
                    break;
                case "balance":
                    // Call the balance method
                    atm.balance();
                    break;
                case "history":
                    // Call the history method
                    atm.history();
                    break;
                case "logout":
                    atmMachine.ejectDebitCard();
                    System.out.println("ATM Machine Current state : "
                            + atmMachine.getAtmMachineState().getClass().getName());
                    // Call the logout method
                    atm.logout();
                    break;
                case "end":
                    // End the program
                    atmMachine.ejectDebitCard();
                    System.out.println("ATM Machine Current state : "
                            + atmMachine.getAtmMachineState().getClass().getName());
                    System.out.println("Ending program!");
                    return;
                case "":
                    break;
                default:
                    // If nothing matches the input default to this
                    System.out.println("Invalid input! Please try again... Available commands {authorize, open, withdraw, deposit, balance, history, logout, end}");
                    break;
            }
            // Reset the timer
            timer = new Timer();
            timer.schedule(createTask(), 120000);
            System.out.print("Please enter a command: ");
        }
    }

    // Timeout task to call logout after session expires
    public static TimerTask createTask() {
        return new TimerTask() {
            @Override
            public void run() {
                atm.logout();
            }
        };
    }
}
