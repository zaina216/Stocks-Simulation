package com.company;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//this is a subclass of Investment which defines a share investment
public class Share extends Investment{
    //instance variables
    private String plusMinus;
    private double percentage;

    public Share(){}

    //constructor
    public Share(int noOfShares, String abbreviation, String plusMinus, double percentage, double price) {
        //gets variables from superclass to be used here
        super(noOfShares, abbreviation, price);
        this.plusMinus = plusMinus;
        this.percentage = percentage;
    }

    //getters and setters
    public String getPlusMinus() {
        return plusMinus;
    }

    //sometimes, the share price of a company drops or increases significantly due to a number of factors. we can simulate this price change.
    public void setPrice(double price){
        Random chance = new Random();
        int crashChance = chance.nextInt(100);
        double droppedPrice = price * ThreadLocalRandom.current().nextDouble(0.6, 0.8);
        if (crashChance > 95) {
            setPlusMinus("Down");
            System.out.println("This stock has crashed.");
            super.setPrice(price - droppedPrice);
        } else if(crashChance < 5){
            setPlusMinus("Up");
            System.out.println("This stock has surged.");
            super.setPrice(price + droppedPrice);
        } else {
            super.setPrice(price);
        }
    }

    public void setPlusMinus(String plusMinus) {
        this.plusMinus = plusMinus;
    }
    public double getPercentage() {
        return percentage;
    }
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    public String toString(){
        return String.format("%-18s %-18s %-18s %-18s", "Share:", super.toString(), getPlusMinus(),
                        getPercentage());
    }
}
