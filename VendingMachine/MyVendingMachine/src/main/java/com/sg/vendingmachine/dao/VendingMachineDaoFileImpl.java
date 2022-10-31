package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Product;
import com.sg.vendingmachine.service.VendingMachinePersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


public class VendingMachineDaoFileImpl implements VendingMachineDao{
    private final String PRODUCT_FILE;
    private static final String DELIMITER = "::";

    Map<String, Product> products = new HashMap<>();

    public VendingMachineDaoFileImpl(){
        PRODUCT_FILE = "product.txt";
    }
    public VendingMachineDaoFileImpl(String productTextFile){
        PRODUCT_FILE = productTextFile;
    }


    public Product getItem(String name) throws VendingMachinePersistenceException{
        products = readFile(PRODUCT_FILE);
        return products.get(name);
    }

    public List<Product> listAllItems() throws VendingMachinePersistenceException{
        products = readFile(PRODUCT_FILE);
        return new ArrayList<Product>(products.values());
    }

    public Product addItem(Product item) throws VendingMachinePersistenceException{
        products = readFile(PRODUCT_FILE);
        Product res = products.put(item.getName(), item);
        writeFile(new ArrayList<Product>(products.values()));
        return res;
    }

    public Product removeItem(Product item) throws VendingMachinePersistenceException{
        products = readFile(PRODUCT_FILE);
        Product res = products.remove(item.getName());
        writeFile(new ArrayList<Product>(products.values()));
        return res;
    }
    public Product changeInventoryCount(Product item, int newCount) throws VendingMachinePersistenceException{
           item.setNumInStock(item.getNumInStock() - newCount);
           Product res = products.put(item.getName(),item);
           writeFile(new ArrayList<Product>(products.values()));
           return res;
    }

    //File IO
    public Product unmarshallItem(String line) {
        String[] productTokens = line.split(DELIMITER);
        String productName = productTokens[0];
        //convert string to respective types for the Item
        BigDecimal itemCost = new BigDecimal(productTokens[1]);
        int numInStock = Integer.parseInt(productTokens[2]);

        Product productFromFile = new Product(productName, itemCost,numInStock);
        return productFromFile;
    }
    public String marshallItem(Product product) {
        return product.getName() + DELIMITER + product.getCost() + DELIMITER + product.getNumInStock();
    }

    public void writeFile(List<Product> list) throws VendingMachinePersistenceException{
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(PRODUCT_FILE));
        }
        catch(IOException e)
        {
            throw new VendingMachinePersistenceException("Could not save product data", e);
        }
        String productAsText;
        for(Product currentProduct : list){
            productAsText = marshallItem(currentProduct);
            out.println(productAsText);
            out.flush();
        }
        out.close();
    }

    public Map<String,Product> readFile(String file) throws VendingMachinePersistenceException{
       Scanner scanner;
       Map<String,Product> productsFromFile = new HashMap<>();
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch(FileNotFoundException e)
        {
            throw new VendingMachinePersistenceException("Could not read file", e);
        }
        String currentLine;
        Product currentProduct;
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentProduct = unmarshallItem(currentLine);
            productsFromFile.put(currentProduct.getName(),currentProduct);
        }
        scanner.close();
        return productsFromFile;
    }

}
