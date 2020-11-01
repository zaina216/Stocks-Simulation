package com.company;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.Date;

public class stocksSimulation extends Main{

    public stocksSimulation(ArrayList<Investment> investmentArrayList){
        for(int i = 0; i < investmentArrayList.size(); i++){
            //stock gets assigned a random percentage between 1% and 10%
            if(investmentArrayList.get(i) instanceof Share){
                investmentArrayList.set(i, changeSharePrice(investmentArrayList.get(i)));
                //displaySimulation(investmentArrayList);
            } else if(investmentArrayList.get(i) instanceof Option){
                //today's date
                LocalDate localDate = LocalDate.now();
                String now = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate);
                Option tempOption = (Option)investmentArrayList.get(i);

                //if the date of option equals today's date, then ask the user if they want to sell the option, or not
                if(tempOption.getDateOfOption().toString().equals(now)){
                    System.out.println("Do you want to buy " + tempOption.getNoOfShares() + " shares of " + tempOption.getAbbreviation() + "[Y/N]?");
                    Scanner input = new Scanner(System.in);
                    String choice = input.next().toLowerCase();
                    for (int i1 = 0; i1 < investmentArrayList.size(); i1++) {
                        Investment investment = investmentArrayList.get(i1);
                        if (tempOption.getAbbreviation().equals(investment.getAbbreviation())) {
                            if (choice.charAt(0) == 'y') {
                                sellShare(investmentArrayList, investment.getAbbreviation());
                                System.out.println("Option bought");
                            } else {
                                System.out.println("Option not bought");
                            }
                            Investment removed = investmentArrayList.remove(i);
                            i--;
                            break;
                        }
                    }
                }
                //automatically sells bond if the date of expiry is equal to today's date
            } else if(investmentArrayList.get(i) instanceof Bond){
                LocalDate localDate = LocalDate.now();
                Date today = java.sql.Date.valueOf(localDate);
                String now = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate);
                Bond tempBond = (Bond)investmentArrayList.get(i);
                if(today.equals(java.sql.Date.valueOf(tempBond.getExpiryDate()))){
                    sellBond(investmentArrayList, tempBond.getAbbreviation());
                    Investment remove = investmentArrayList.remove(i);
                    i--;
                }
            }
        }
    }
    public void displaySimulation(ArrayList<Investment> investmentArrayList){
        //this together with showing that the ArrayList is filled with subclasses shows polymorphism
        System.out.printf ("%-18s %-18s %-18s %-18s %-18s %-18s %-18s %-18s %n","Type of investment",
                "Abbreviation","number of shares","price","change","percentage","start date","expiry date");
        for (Investment investment : investmentArrayList) {
            System.out.println(investment.toString());
        }
    }

    //share price goes randomly up and down between 0% to 10%
    public Share changeSharePrice(Investment investment){
        Share tempShare = (Share) investment;
        Random r = new Random();
        double rN = r.nextDouble() * 0.01;

        Random rr = new Random();
        int rN2 = rr.nextInt(2);
        if (rN2 == 0) {
            tempShare.setPercentage(-rN*100);
            tempShare.setPlusMinus("Down");
            tempShare.setPrice(tempShare.getPrice()*(1-rN));
        } else {
            tempShare.setPercentage(rN*100);
            tempShare.setPlusMinus("Up");
            tempShare.setPrice(tempShare.getPrice()*(1+rN));
        }
        return tempShare;
    }
}


















