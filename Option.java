package com.company;

import java.time.LocalDate;
//this is a subclass of Investment which defines an option investment
public class Option extends Investment{
    private LocalDate dateOfOption;

    //constructor
    public Option(){}
    public Option(int noOfShares, String abbreviation, LocalDate dateOfOption, double price) {
        //gets variables from superclass to be used here
        super(noOfShares, abbreviation, price);
        this.dateOfOption = dateOfOption;
    }

    //getters and setters
    public LocalDate getDateOfOption() {
        return dateOfOption;
    }

    public void setDateOfOption(LocalDate dateOfOption) {
        this.dateOfOption = dateOfOption;
    }

    //we get better returns when selling options, so let's increase the value of the options bought by increasing the price by 15%.
    public void setPrice(double price){
        super.setPrice(super.getPrice() * 1.15);
    }

    public String toString(){
        return String.format("%-18s %-18s %-18s %-18s %-18s", "Option:",  super.toString(), "",
                "", getDateOfOption().toString());
    }
}
