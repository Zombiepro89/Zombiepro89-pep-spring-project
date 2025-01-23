package com.example.service;

import java.sql.SQLException;

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

    /**
     * Adds the provided account to the database
     * 
     * @param accountDetails The account to be registered
     * @return The Account object that represents the account added to the database
     * 
     * @throws IllegalArgumentException If the password is invalid or username is blank
     * @throws SQLException If the username has already been taken
     */
    public Account registerUser(Account accountDetails) throws IllegalArgumentException, SQLException{
        if(accRepo.findAccountByUsername(accountDetails.getUsername()) != null){
            throw new SQLException("Username is already taken.");
        }

        if(accountDetails.getUsername().length() < 1){
            throw new IllegalArgumentException("Username must not be blank.");
        }

        if (accountDetails.getPassword().length() < 4){
            throw new IllegalArgumentException("Password expected to be 4 characters or longer, was " 
                + accountDetails.getPassword().length() + " instead.");
        }

        return accRepo.save(accountDetails);
    }

    /**
     * Attempts to login a user if the account details are valid
     * 
     * @param loginDetails The details for the desired account
     * @return The Account that was logged into or null otherwise
     */
    public Account attemptLogin(Account loginDetails){
        return accRepo.findAccountByUsernameAndPassword(loginDetails.getUsername(), loginDetails.getPassword());
    }
}
