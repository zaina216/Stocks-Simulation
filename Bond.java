package com.company;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//this is a subclass of Investment which defines a bond investment
public class Bond extends Investment{
    //instance variables
    private LocalDate startDate;
    private LocalDate expiryDate;
    private boolean turnedIntoShare = false;
    private LocalDate todaysDate;

    //constructors
    public Bond(){}
    public Bond(int noOfShares, String abbreviation, LocalDate startDate, LocalDate expiryDate, double price) {
        //gets variables from superclass to be used here
        super(noOfShares, abbreviation, price);
        this.startDate = startDate;
        this.expiryDate = expiryDate;
    }

    //getters and setters
    //bonds have interests paid to the owner. The interest depends on the days since purchase
    public double getPrice(){
        todaysDate = LocalDate.now();

        Date startDate = java.sql.Date.valueOf(getStartDate());
        Date interestDate = java.sql.Date.valueOf(todaysDate);

        long timeDiff = Math.abs(interestDate.getTime() - startDate.getTime());
        long dayDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

        //using the interest formula: interest = (starting_amount)(1+interest_rate)^time.
        //interest rate is 10%
        double interest = super.getPrice() + super.getPrice() * Math.pow((1.1), Math.toIntExact(dayDiff));
        super.setPrice(interest);
        return (interest);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isTurnedIntoShare() {
        return turnedIntoShare;
    }

    public void setTurnedIntoShare(boolean turnedIntoShare) {
        this.turnedIntoShare = turnedIntoShare;
    }


    public String toString() {
        return String.format("%-18s %-18s %-18s %-18s %-18s %-18s", "Bond:", super.toString(), "", "",startDate.toString(),
                expiryDate.toString());
    }
}