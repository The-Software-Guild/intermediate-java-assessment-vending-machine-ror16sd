package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineAuditDao;
import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dto.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer{
    private VendingMachineDao dao;
    private VendingMachineAuditDao adao;

    //public VendingMachineServiceLayerImpl() throws VendingMachinePersistenceException {}

    public VendingMachineServiceLayerImpl(VendingMachineDao dao, VendingMachineAuditDao adao) {
        this.dao = dao;
        this.adao = adao;
    }

    @Override
    public Product getProduct(String name) throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException {
        Product product = dao.getItem(name);
        if((product != null)&& product.getNumInStock() > 0) {
            return product;
        }
        else{
            throw new VendingMachineNoItemInventoryException("Item Out of Stock.");
        }
    }

    @Override
    public List<Product> listAllProducts() throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException {

        return dao.listAllItems()
                .stream()
                .filter(product->product.getNumInStock()>0)
                .collect(Collectors.toList());
    }

    @Override
    public Product addProduct(Product product) throws VendingMachinePersistenceException,VendingMachineDataValidationException {
        validateProductData(product);
        Product addedProduct= dao.addItem(product);
        adao.writeAuditEntry("Product " + product.getName() + " ADDED.");
        return addedProduct;
    }

    @Override
    public Product removeProduct(Product product) throws VendingMachinePersistenceException {
        Product removedProduct = dao.removeItem(product);
        adao.writeAuditEntry("Product " + removedProduct.getName() + " REMOVED");
        return removedProduct;
    }

    @Override
    public Product changeInventoryCount(Product product, int newCount) throws VendingMachinePersistenceException, VendingMachineNoItemInventoryException {
        Product changedStock = dao.changeInventoryCount(product,newCount);
        adao.writeAuditEntry("Product: " + changedStock.getName()  + " " + newCount + "[SOLD]");
        return changedStock;
    }

    @Override
    public BigDecimal sellProduct(BigDecimal totalFunds, Product product) throws VendingMachineInsufficientFundsException, VendingMachineNoItemInventoryException {
        if(totalFunds.compareTo(product.getCost()) > 0){
            if(product.getNumInStock() > 0)
                return totalFunds.subtract(product.getCost());
            else
                throw new VendingMachineNoItemInventoryException("Product Sold Out");
        }
        else{
            throw new VendingMachineInsufficientFundsException("Insufficient Funds");
        }
    }

    private void validateProductData(Product product) throws VendingMachineDataValidationException{
        if(product.getName() == null
            || product.getName().trim().length() == 0
            || product.getCost() == null
            || product.getNumInStock() < 0){
            throw new VendingMachineDataValidationException("ERROR: All fields [NAME, COST, NUMINSTOCK] are required");
        }
    }
}
