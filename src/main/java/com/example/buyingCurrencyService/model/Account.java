package com.example.buyingCurrencyService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "currencyUsers")
public class Account {

    @Id
    private String login;
    private double balance;
    private List<Currency> currencies;

    public Account() {
    }

    public Account(String login, double balance, List<Currency> currencies) {
        this.login = login;
        this.balance = balance;
        this.currencies = currencies;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", balance=" + balance +
                ", wallet=" + currencies +
                '}';
    }
}