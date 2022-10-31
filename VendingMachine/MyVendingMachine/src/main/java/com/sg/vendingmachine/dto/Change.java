package com.sg.vendingmachine.dto;

import com.sg.vendingmachine.service.VendingMachineInsufficientFundsException;

import java.math.BigDecimal;
import java.util.HashMap;
import com.sg.vendingmachine.dto.CoinValue;

public class Change {
    private int quarters;
    private int dimes;
    private int nickles;
    private int pennies;

    public Change(BigDecimal amount){
        this.quarters = (amount.divide(CoinValue.QUARTERS.getValue())).intValue();
        amount = amount.remainder(CoinValue.QUARTERS.getValue());
        this.dimes = amount.divide(CoinValue.DIME.getValue()).intValue();
        amount = amount.remainder(CoinValue.DIME.getValue());
        this.nickles = amount.divide(CoinValue.NICKLE.getValue()).intValue();
        amount = amount.remainder(CoinValue.NICKLE.getValue());
        this.pennies = amount.divide(CoinValue.PENNY.getValue()).intValue();
    }
    public int getQuarters(){return quarters;}
    public int getDimes(){return dimes;}
    public int getNickles(){return nickles;}
    public int getPennies(){return pennies;}
}
