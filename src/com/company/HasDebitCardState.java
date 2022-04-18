package com.company;

public class HasDebitCardState implements ATMMachineState{
    @Override
    public void insertDebitCard()
    {
        System.out.println("Debit Card is already there,So you cannot insert the Debit Card ...");

    }

    @Override
    public void ejectDebitCard()
    {

        System.out.println("Debit Card is ejected...");
    }

}
