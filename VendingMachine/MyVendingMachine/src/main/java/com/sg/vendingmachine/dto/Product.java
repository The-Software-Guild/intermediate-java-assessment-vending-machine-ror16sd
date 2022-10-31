package com.sg.vendingmachine.dto;

import com.sg.vendingmachine.service.VendingMachinePersistenceException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

public class Product {
    private String name;
    private BigDecimal cost;
    private int numInStock;

    private final String DELIMITER = "::";

    //Constructors
    public Product(String productAsText) throws VendingMachinePersistenceException {
        try{
            String[] productTokens = productAsText.split(DELIMITER);
            this.name = productTokens[0];
            this.cost = new BigDecimal(productTokens[1]);
            this.numInStock = Integer.parseInt(productTokens[2]);
        }
        catch(PatternSyntaxException | NullPointerException | NumberFormatException e){
            throw new VendingMachinePersistenceException(e.getMessage());
        }
    }
    public Product(String name, BigDecimal cost, int numInStock){
        this.name = name;
        this.cost = cost;
        this.numInStock = numInStock;
    }

    //Setters and Getters
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public BigDecimal getCost() {
        return cost;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getNumInStock() {
        return numInStock;
    }
    public void setNumInStock(int numInStock) {
        this.numInStock = numInStock;
    }

    @Override
    public String toString(){
        return "Product{" + "name =" + name + ", cost =" + cost + ", numInStock =" + numInStock + '}';
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.cost);
        hash = 89 * hash + Objects.hashCode(this.numInStock);
        return hash;
    }
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        final Product other = (Product) obj;
        if(!Objects.equals(this.name, other.name)){
            return false;
        }
        if(!Objects.equals(this.cost, other.cost)){
            return false;
        }
        if(!Objects.equals(this.numInStock, other.numInStock)){
            return false;
        }
        return true;
    }

}
