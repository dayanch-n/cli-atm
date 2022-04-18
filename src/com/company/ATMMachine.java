package com.company;

public class ATMMachine implements ATMMachineState {
    private ATMMachineState atmMachineState;

    public ATMMachine()
    {
        atmMachineState = new NoDebitCardState();
    }

    public ATMMachineState getAtmMachineState()
    {
        return atmMachineState;
    }

    public void setAtmMachineState( ATMMachineState atmMachineState )
    {
        this.atmMachineState = atmMachineState;
    }

    @Override
    public void insertDebitCard()
    {
        atmMachineState.insertDebitCard();


        if( atmMachineState instanceof NoDebitCardState )
        {

            ATMMachineState hasDebitCardState = new HasDebitCardState();
            setAtmMachineState(hasDebitCardState);
            System.out.println("ATM Machine internal state has been moved to : "
                    + atmMachineState.getClass().getName());
        }

    }

    @Override
    public void ejectDebitCard()
    {
        atmMachineState.ejectDebitCard();

        if( atmMachineState instanceof HasDebitCardState )
        {

            ATMMachineState noDebitCardState = new NoDebitCardState();
            setAtmMachineState(noDebitCardState);
            System.out.println("ATM Machine internal state has been moved to : "
                    + atmMachineState.getClass().getName());
        }

    }

}
