package com.company;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    //main method controls the whole execution of the program
    //global master balance variable to add more realism to this program
    //make this not a static variable
    static double masterBalance = 500.0;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //this is a master arrayList which stores all the different types of investment the user makes
        ArrayList<Investment> investmentArrayList = new ArrayList<>();
        System.out.println("Welcome to the stock market simulator.");
        while(true) {
            System.out.println("Press d to display your portfolio.\n" +
                    "Press b to buy\n" +
                    "Press s to sell\n" +
                    "Press q to quit\n" +
                    "Press t to start the simulation");

            String choice = input.next().toLowerCase();

            switch (choice) {
                case "d":
                    displayPortfolio(investmentArrayList);
                    break;
                case "b":
                    buy(investmentArrayList);
                    break;
                case "s":
                    sell(investmentArrayList);
                    break;
                case "q":
                    System.exit(0);
                    break;
                case "t":
                    //fix simulation display before submitting
                    System.out.println("Starting simulation");
                    stocksSimulation sm = new stocksSimulation(investmentArrayList);
                    sm.displaySimulation(investmentArrayList);
                    break;
                default:
                    System.out.println("Unexpected value: " + choice);
            }
        }
    }

    //gives the user an option to sell different types of investments
    public static void sell(ArrayList<Investment> investmentArrayList){
        System.out.println("What do you want to sell? 'option' to release an option and 'share' to sell a share");
        Scanner input = new Scanner(System.in);
        String d = input.next().toLowerCase();
        switch (d) {
            case "bond":
                sellBond(investmentArrayList, null);
                break;
            case "option":
                releaseOption(investmentArrayList, null);
                break;
            case "share":
                sellShare(investmentArrayList, null);
                break;
            default:
                System.out.println("Unexpected value: " + d);
        }
    }
    //gives the user an option to buy different types of investments
    public static void buy(ArrayList<Investment> investmentArrayList){
        System.out.println("What do you want to buy? Type 'option' to take an option, 'share' to buy a share or 'bond' to buy a bond");
        Scanner input = new Scanner(System.in);
        String usr = input.next().toLowerCase();
        switch (usr) {
            case "bond":
                investmentArrayList.add(buyBond(investmentArrayList));
                break;
            case "option":
                investmentArrayList.add(haveOption(investmentArrayList));
                break;
            case "share":
                Investment temp = buyShare(investmentArrayList);
                if (temp != null) {
                    investmentArrayList.add(temp);
                }
                break;
            default:
                System.out.println("Unexpected value: " + usr);
        }
    }

    //displays current portfolio
    public static void displayPortfolio(ArrayList<Investment> investmentArrayList){
        if(investmentArrayList.size() == 0){
            System.out.println("list size is 0");
            System.out.println("Your balance: "+masterBalance);
        } else {
            System.out.println("Your balance: "+masterBalance);
            System.out.println("Your portfolio:");
            System.out.printf ("%-18s %-18s %-18s %-18s %-18s %-18s %-18s %-18s %n","Type of investment",
                    "Abbreviation","number of shares","price","change","percentage","start date","expiry date");
            //loops through master ArrayList and checks subclass datatypes of all investment type elements in this list and calls toString on all on them
            for (Investment investment : investmentArrayList) {
                try {
                    System.out.println(investment.toString());
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static Investment buyShare(ArrayList<Investment> investmentArrayList){
        boolean found = false;
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the company abbreviation: ");
        String abbreviation = input.next();

        //Checks if there is a share in an existing company in the user's portfolio. If so,
        //add to the number of shares in the company, instead of creating a new share object.
        for (Investment investment : investmentArrayList) {
            if (investment.getAbbreviation().equals(abbreviation) && investment instanceof Share) {
                found = true;
                System.out.println("Enter the number of shares you wish to buy from existing company: ");
                int noOfShares = input.nextInt();
                if (masterBalance - noOfShares * investment.getPrice() < 0) {
                    System.out.println("Balance is too small to buy these shares");
                } else {
                    investment.setNoOfShares(investment.getNoOfShares() + noOfShares);
                    masterBalance -= noOfShares * investment.getPrice();
                }
                break;
            }
        }

        if(!found){
            System.out.println("Enter share price: ");
            double price = input.nextDouble();
            System.out.println("Enter the number of shares you wish to buy: ");
            int noOfShares = input.nextInt();
            if(masterBalance - noOfShares*price < 0){
                System.out.println("Balance is too small to buy these shares");
            } else {
                masterBalance -= noOfShares*price;
            }
            return new Share(noOfShares, abbreviation, "", 0.0, price);
        }
        return null;
    }

    public static ArrayList<Investment> sellShare(ArrayList<Investment> investmentArrayList, String abbreviation){
        //this piece of code will put all indexes of Options and Bonds in InvestmentArrayList into idxOfBondsOption
        //in a process to help stop shares being directly sold being used by Bonds and Options/
        //The only way these shares must be sold is if the options or bonds associated with them being sold.
        boolean canSell;
        ArrayList<Integer> idxOfBondsOptions = new ArrayList<Integer>();
        for (int i = 0; i < investmentArrayList.size(); i++) {
            if (investmentArrayList.get(i) instanceof Bond || investmentArrayList.get(i) instanceof Option) {
                idxOfBondsOptions.add(i);
            }
        }

        Scanner input = new Scanner(System.in);
        if (abbreviation == null){
            System.out.println("Enter the abbreviation of the company whose share(s) you want to sell");
            abbreviation = input.next();
        }

        for (int i = 0; i < investmentArrayList.size(); i++) {
            Investment investment = investmentArrayList.get(i);
            canSell = true;
            if (investment instanceof Share) {
                if (investment.getAbbreviation().equals(abbreviation)) {
                    System.out.println("How many shares do you want to sell?");
                    int noOfShares = input.nextInt();
                    for (Integer idxOfBondsOption : idxOfBondsOptions) {
                        if (investment.getAbbreviation().equals(investmentArrayList.get(idxOfBondsOption).getAbbreviation())
                                && investment.getNoOfShares() - noOfShares < investmentArrayList.get(idxOfBondsOption).getNoOfShares()) {
                            System.out.println("Cannot sell this number of shares as a bond or option is using some of these shares");
                            canSell = false;
                            break;
                        }
                    }
                    //removes share object if the number of shares in the bond/option equals the number of shares
                    //in the share object it's associated with.
                    if (canSell) {
                        if (investment.getNoOfShares() - noOfShares == 0) {
                            investmentArrayList.remove(i);
                            i--;
                        } else {
                            investment.setNoOfShares(investment.getNoOfShares() - noOfShares);
                        }
                        masterBalance += noOfShares * investment.getPrice();
                    }
                }
            }
        }
        return investmentArrayList;
    }

    public static Investment buyBond(ArrayList<Investment> investmentArrayList){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the start date: ");
        System.out.println("Enter the start year: ");
        int startDay = input.nextInt();
        System.out.println("Enter the start month: ");
        int startMonth = input.nextInt();
        System.out.println("Enter the start day: ");
        int startYear = input.nextInt();

        LocalDate startDate = LocalDate.of(startDay, startMonth, startYear);
        String now = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(startDate);

        System.out.println("Enter the end date: ");
        System.out.println("Enter the end year: ");
        int endDay = input.nextInt();
        System.out.println("Enter the end month: ");
        int endMonth = input.nextInt();
        System.out.println("Enter the end day: ");
        int endYear = input.nextInt();

        System.out.println("Enter the current share price for this company: ");
        double price = input.nextDouble();

        System.out.println("Enter the company abbreviation: ");
        String abbreviation = input.next();

        System.out.println("Enter the number of shares: ");
        int noOfShares = input.nextInt();
        LocalDate expiryDate = LocalDate.of(endDay, endMonth, endYear);
        String now2 = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(expiryDate);

        if(investmentArrayList.size() == 0){
            investmentArrayList.add(new Share(noOfShares, abbreviation, "", 0.0, price));
            masterBalance -= noOfShares * price;
        } else {
            for (Investment investment : investmentArrayList) {
                if (investment.getAbbreviation().equals(abbreviation) && investment instanceof Share) {
                    investment.setNoOfShares(investment.getNoOfShares() + noOfShares);
                    masterBalance -= investment.getNoOfShares()*investment.getPrice();
                } else {
                    investmentArrayList.add(new Share(noOfShares, abbreviation, "", 0.0, price));
                    masterBalance -= noOfShares * price;
                }
            }
        }

        Investment tempBond = new Bond(noOfShares, abbreviation, startDate, expiryDate, price);
        return tempBond;
    }



    public static void releaseOption(ArrayList<Investment> investmentArrayList, String abbreviation) {
        boolean found = false;
        Option tempOption = new Option();
        System.out.println("Enter the abbreviation of the company whose option you want to release");
        Scanner input = new Scanner(System.in);
        if(abbreviation == null) {
            abbreviation = input.next();
        }

        //code removes option from list
        for (int i = 0; i < investmentArrayList.size(); i++) {
            found = true;
            Investment investment = investmentArrayList.get(i);
            if (investment instanceof Option && investment.getAbbreviation().equals(abbreviation)) {
                tempOption = (Option) investment;
                //Dynamic binding: setNoOfShares is overridden in the Option class
                tempOption.setNoOfShares(tempOption.getNoOfShares());
                masterBalance -= tempOption.getPrice() * tempOption.getNoOfShares();
                Investment removed = investmentArrayList.remove(i);
            }
        }
        //and this removes corresponding shares
        if(found){
            for (int i = 0; i < investmentArrayList.size(); i++) {
                if (investmentArrayList.get(i) instanceof Share && investmentArrayList.get(i).getAbbreviation().equals(abbreviation)){
                    if(investmentArrayList.get(i).getNoOfShares() - tempOption.getNoOfShares() > 0){
                        investmentArrayList.get(i).setNoOfShares(investmentArrayList.get(i).getNoOfShares() - tempOption.getNoOfShares());
                    } else if(investmentArrayList.get(i).getNoOfShares() - tempOption.getNoOfShares() == 0){
                        investmentArrayList.remove(i);
                    } else {
                        System.out.println("Number of shares you want to sell is too large");
                    }
                }
            }
        }
    }

    public static void sellBond(ArrayList<Investment> investmentArrayList, String abbreviation) {
        boolean found = false;
        Bond tempBond = new Bond();
        System.out.println("Enter the abbreviation of the company whose bond you want to sell");
        Scanner input = new Scanner(System.in);
        if(abbreviation == null) {
            abbreviation = input.next();
        }

        //code removes bond from list
        for (int i = 0; i < investmentArrayList.size(); i++) {
            found = true;
            Investment investment = investmentArrayList.get(i);
            if (investment instanceof Bond && investment.getAbbreviation().equals(abbreviation)) {
                tempBond = (Bond) investment;
                //Dynamic binding: setNoOfShares is overridden in the Bond class
                tempBond.setNoOfShares(tempBond.getNoOfShares());
                masterBalance += tempBond.getPrice() * tempBond.getNoOfShares();
                Investment removed = investmentArrayList.remove(i);
            }
        }

        //and this removes corresponding shares
        if(found){
            for (int i = 0; i < investmentArrayList.size(); i++) {
                if (investmentArrayList.get(i) instanceof Share && investmentArrayList.get(i).getAbbreviation().equals(abbreviation)){
                    if(investmentArrayList.get(i).getNoOfShares() - tempBond.getNoOfShares() > 0){
                        investmentArrayList.get(i).setNoOfShares(investmentArrayList.get(i).getNoOfShares() - tempBond.getNoOfShares());
                    } else if(investmentArrayList.get(i).getNoOfShares() - tempBond.getNoOfShares() == 0){
                        investmentArrayList.remove(i);
                    } else {
                        System.out.println("Number of shares you want to sell is too large");
                    }
                }
            }
        }
    }

    //creates an option investment for the user
    public static Investment haveOption(ArrayList<Investment> investmentArrayList){
        Scanner input = new Scanner(System.in);
        boolean added = false;

        System.out.println("Enter the company abbreviation: ");
        String abbreviation = input.next();
        System.out.println("Enter the number of shares: ");
        int noOfShares = input.nextInt();
        System.out.println("Enter the current share price for this company: ");
        double price = input.nextDouble();

        System.out.println("Enter the date of the option: ");
        System.out.println("Enter the day: ");
        int endDay = input.nextInt();
        System.out.println("Enter the month: ");
        int endMonth = input.nextInt();
        System.out.println("Enter the year: ");
        int endYear = input.nextInt();

        LocalDate localDate = LocalDate.of(endYear, endMonth, endDay);
        String now = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate);
        System.out.println(now);

        if(investmentArrayList.size() == 0){
            investmentArrayList.add(new Share(noOfShares, abbreviation, "", 0.0, price));
            masterBalance -= noOfShares * price;
        } else {
            for (int i = 0; i < investmentArrayList.size(); i++) {
                if (investmentArrayList.get(i).getAbbreviation().equals(abbreviation) && investmentArrayList.get(i) instanceof Share) {
                    investmentArrayList.get(i).setNoOfShares(investmentArrayList.get(i).getNoOfShares() + noOfShares);
                    masterBalance -= investmentArrayList.get(i).getPrice() * noOfShares;
                } else {
                    added = true;
                    investmentArrayList.add(new Share(noOfShares, abbreviation, "", 0.0, price));
                    masterBalance -= noOfShares * price;
                }
                if(added) break;
            }
        }
        Investment tempOption = new Option(noOfShares, abbreviation, localDate, price);
        return tempOption;
    }
}