package com.sg.vendingmachine.controller;
import com.sg.vendingmachine.dto.Change;
import com.sg.vendingmachine.dto.Product;
import com.sg.vendingmachine.service.VendingMachineInsufficientFundsException;
import com.sg.vendingmachine.service.VendingMachineNoItemInventoryException;
import com.sg.vendingmachine.service.VendingMachinePersistenceException;
import com.sg.vendingmachine.service.VendingMachineServiceLayer;
import com.sg.vendingmachine.ui.UserIO;
import com.sg.vendingmachine.ui.UserIOConsoleImpl;
import com.sg.vendingmachine.ui.VendingMachineView;

import java.math.BigDecimal;

public class VendingMachineController {

    private UserIO io = new UserIOConsoleImpl();
    private VendingMachineView view;
    private VendingMachineServiceLayer service;

    public VendingMachineController(VendingMachineView view,VendingMachineServiceLayer service){
        this.view = view;
        this.service = service;
    }

    public void run(){
        BigDecimal balance = new BigDecimal(0.00);
        boolean keepGoing = true;
        try {
            while (keepGoing) {
                //PRINT ALL PRODUCTS
                productMenu();
                //PRINT BALANCE
                view.displayBalance(balance);
                String menuSelection = view.getMenuSelection();
                switch(menuSelection){
                    case "1":
                        //ADD TO BALANCE
                        //addFunds calls to view for read in value
                        balance = addFunds(balance);
                        break;
                    case "2":
                        String productSelected;
                        //asks user to select product to buy
                        productSelected = view.getProductSelectedToVend();
                        try{
                            balance = buyProduct(balance, productSelected);
                           // printBalanceAndChange(balance);
                        } catch(VendingMachineNoItemInventoryException | VendingMachineInsufficientFundsException e){
                            view.displayBalance(balance);
                            view.displayErrorMessage(e.getMessage());
                        }
                        break;
                    case "3":
                        try{
                            quit(balance);
                        }catch(VendingMachineInsufficientFundsException e){
                            view.displayBalance(balance);
                            view.displayErrorMessage(e.getMessage());
                        }
                        keepGoing = false;
                        break;
                    default:
                        view.displayUnknownCommand();
                }

            }
        } catch(VendingMachinePersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }

        view.displayQuitMessage();
    }

    private void printBalanceAndChange(BigDecimal balance) {
        view.displayBalance(balance);
        //convert balance to Change obj object for printing to console
        Change change = new Change(balance);
        view.displayChange(change);
    }

    private void productMenu() throws VendingMachinePersistenceException{
        try {
            view.printMenu();
            for (Product p : service.listAllProducts()) {
                view.displayProduct(p);
            }
        }catch(VendingMachineNoItemInventoryException | VendingMachinePersistenceException e){
                throw new VendingMachinePersistenceException(e.getMessage());
            }
        }


    private BigDecimal buyProduct(BigDecimal balance, String productSelected) throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException, VendingMachineInsufficientFundsException{
        //Convert string to product item in list, check if valid/in stock. Through service layer
        //service-layer to compare if balance is enough for productSelected
        BigDecimal updatedBalance = new BigDecimal("0.00");
        try{
            //get product by name
            Product product = service.getProduct(productSelected);
            //update balance
            updatedBalance = service.sellProduct(balance,product);
            //update inventory/numInStock
            service.changeInventoryCount(product,1);
            view.displaySuccessVending(product);
            return updatedBalance;
        }catch (VendingMachineNoItemInventoryException|VendingMachineInsufficientFundsException | VendingMachinePersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }
        return balance;
    }

    public BigDecimal addFunds(BigDecimal balance){
       return balance.add(view.displayAddFunds());
    }


    public void quit(BigDecimal balance) throws VendingMachineInsufficientFundsException{
        printBalanceAndChange(balance);
    }

}
