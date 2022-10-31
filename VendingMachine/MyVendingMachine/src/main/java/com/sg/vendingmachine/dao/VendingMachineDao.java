package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Product;
import com.sg.vendingmachine.service.VendingMachinePersistenceException;

import java.util.List;
import java.util.Map;

public interface VendingMachineDao {
    Product getItem(String name) throws VendingMachinePersistenceException;
    List<Product> listAllItems() throws VendingMachinePersistenceException;
    Product addItem(Product item) throws VendingMachinePersistenceException;
    Product removeItem(Product item) throws VendingMachinePersistenceException;
    Product changeInventoryCount(Product item, int newCount) throws VendingMachinePersistenceException;

}
