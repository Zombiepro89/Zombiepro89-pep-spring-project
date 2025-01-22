package com.example.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accRepo;

    @Autowired 
    public AccountService(AccountRepository accRepo){
        this.accRepo = accRepo;
    }

    public Account attemptLogin(Account loginDetails){
        return accRepo.findAccountByUsernameAndPassword(loginDetails.getUsername(), loginDetails.getPassword());
    }
}
