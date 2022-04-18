package com.company;

public class NoDebitCardState implements ATMMachineState {

    @Override
    public void insertDebitCard()
    {
        System.out.println("DebitCard inserted ....");

    }

    @Override
    public void ejectDebitCard()
    {

        System.out.println("No Debit Card in ATM Machine slot, so you cannot eject the Debit Card...");
    }

}
