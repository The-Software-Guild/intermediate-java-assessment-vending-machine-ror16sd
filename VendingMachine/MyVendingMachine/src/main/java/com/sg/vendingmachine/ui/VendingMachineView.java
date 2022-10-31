package com.sg.vendingmachine.ui;

import com.sg.vendingmachine.dto.Change;
import com.sg.vendingmachine.dto.Product;
import com.sg.vendingmachine.service.VendingMachinePersistenceException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class VendingMachineView {
    private UserIO io;

    public VendingMachineView(UserIO io){
        this.io = io;
    }

    public void printMenu()  {
        VendingMachineListBanner();
        io.print("Product\t\t\t" + "Cost");
        io.print("-----------------------");
    }

    public void displayProduct(Product product){
        io.print(product.getName() + "\t\t\t" + product.getCost());
    }
    private void VendingMachineListBanner() {
        io.print("====Vending Machine====");
    }
    public void displayBalance(BigDecimal balance){
        io.print("Balance: " + balance.setScale(2, RoundingMode.DOWN));
    }

    public void displayChange(Change funds){
        io.print("Quarters: " + funds.getQuarters());
        io.print("Dimes: " + funds.getDimes());
        io.print("Nickles: " + funds.getNickles());
        io.print("Pennies: " + funds.getPennies());
        io.print("");
    }
    public void displayQuitMessage() {
        io.print("Goodbye!" + "\n");
    }
    public void displayUnknownCommand() {
        io.print("\nInvalid input. Please input 1, 2 or 3" + "\n");
    }

    public BigDecimal displayAddFunds() {
        return new BigDecimal(io.readString("Please Add Money To Balance: "));
    }

    public String getMenuSelection() {
        return io.readString("\n1. Add Funds\n2. Purchase Item\n3. Quit\n Please Select Option: ");
    }

    //returns user input, product name back to controller
    public String getProductSelectedToVend(){
        return io.readString("Please Enter Item To Purchase: ");
    }

    public void displayErrorMessage(String errorMag){
        io.print("=== ERROR ===");
        io.print(errorMag);
    }

    public void displaySuccessVending(Product product) {
        io.print("\n=== VENDING ===");
        io.print("Here is your " + product.getName());
    }
}
