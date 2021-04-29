package com.example.buyingCurrencyService.controller;

import com.example.buyingCurrencyService.model.Account;
import com.example.buyingCurrencyService.model.Currency;
import com.example.buyingCurrencyService.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class BuyingCurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/accountInfo")
    public Account getAccountInfo(Authentication authentication) {
        return currencyService.getAccount(authentication.getName());
    }

    @GetMapping("/accountInfo/{currencyName}")
    public Currency getParticularCurrency(@PathVariable String currencyName, Authentication authentication) {
        return currencyService
                .getParticularUserCurrencyAndCheckIfCurrencyPresent(authentication.getName(), currencyName);
    }

    @PutMapping("/accountInfo")
    public Account updateAccount(@RequestBody Currency currency, Authentication authentication) {
        return currencyService.topUpAnAccount(authentication.getName(), currency);
    }
}