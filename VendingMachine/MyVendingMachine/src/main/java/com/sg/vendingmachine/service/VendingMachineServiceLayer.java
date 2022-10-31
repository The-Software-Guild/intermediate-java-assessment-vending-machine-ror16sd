package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public interface VendingMachineServiceLayer {
    Product getProduct(String name) throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException;
    List<Product> listAllProducts() throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException;
    Product addProduct(Product product) throws VendingMachinePersistenceException, VendingMachineDataValidationException;
    Product removeProduct(Product product) throws VendingMachinePersistenceException;
    Product changeInventoryCount (Product product, int newCount) throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException;
    BigDecimal sellProduct(BigDecimal totalFunds, Product product) throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException, VendingMachineInsufficientFundsException;
}
