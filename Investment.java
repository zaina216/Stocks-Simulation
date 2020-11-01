package com.company;
//this is the investment superclass where common methods and attributes to all subclasses are located
public class Investment {
    //instance variables
    private int noOfShares;
    private String abbreviation;
    private double price;

    //constructors
    public Investment() {}
    public Investment(int noOfShares, String abbreviation, double price) {
        this.noOfShares = noOfShares;
        this.abbreviation = abbreviation;
        this.price = price;
    }

    //getters and setters
    public int getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(int noOfShares) {
        this.noOfShares = noOfShares;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString(){
        return String.format("%-18s %-18s %-18s", abbreviation, noOfShares, price);
    }
}