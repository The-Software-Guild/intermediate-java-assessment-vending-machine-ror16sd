package com.sg.vendingmachine.service;

public class VendingMachineNoItemInventoryException extends Exception{
    public VendingMachineNoItemInventoryException(String msg){super(msg);}
    public VendingMachineNoItemInventoryException(String msg, Throwable cause){super(msg,cause);}
}
