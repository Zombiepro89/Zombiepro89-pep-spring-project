package com.example.controller;

import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests that come to our social media platform
 */
 @RestController
public class SocialMediaController {

    @Autowired
    AccountService accService;
    @Autowired
    MessageService msgService;

    // Account related handling
    
    /**
     * Attempts to insert a given account into the database, returning the account, if it was successfully added
     * 
     * HttpStatus 200 is returned when account is added successfully
     * HttpStatus 409 is returned when account's username was already taken
     * HttpStatus 400 is returned otherwise
     * 
     * @param body The account to be inserted into the database
     * @return ResponseEntity<Account> object with the status code and the account, if it was added, null otherwise
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account body){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(accService.registerUser(body));
        }
        catch(SQLException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return ResponseEntity.status(400).body(null);
    }

    
    /**
     * Attempts to login the user
     * 
     * HttpStatus 200 is returned if the account was logged in
     * HttpStatus 401 is returned otherwise
     * 
     * @param body The account details for the account to be logged in
     * @return ResponseEntity<Account> with account details, if successfully logged in, null otherwise
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Account> attemptLogin(@RequestBody Account body){
        Account loginAttempt = accService.attemptLogin(body);

        if(loginAttempt != null){
            return ResponseEntity.status(HttpStatus.OK).body(loginAttempt);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    // Message handling
    
    /**
     * Retrieves all messages from the database
     * 
     * HttpStatus 200 is always expected to return
     * 
     * @return ReponseEntity<List<Message>> which contains every message in the database
     */
    @GetMapping(value = "/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.getAllMessages());
    }

    
    /**
     * Returns a message with the given message_id.
     * 
     * HttpStatus 200 is always expected
     * 
     * @param message_id The message_id of the desired message
     * @return ResponseEntity<Message> containing the message, if found
     */
    @GetMapping(value = "/messages/{message_id}")
    public ResponseEntity<Message> getMessageByID(@PathVariable int message_id){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.getMessageByID(message_id));
    }

    
    /**
     * Returns all messages sent by a given user
     * 
     * HttpStatus 200 is always expected
     * 
     * @param account_id The account_id associated with the account that the messages should be retrieved from
     * @return ResponseEntity<List<Message>> of all messages sent by a user.
     */
    @GetMapping(value = "/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessageSentByUserID(@PathVariable int account_id){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.getMessagesSendByUserID(account_id));
    }

    
    /**
     * Updates a message's text that has the given message_id with the text within the body of the request
     * 
     * HttpStatus 200 if the update was successful
     * HttpStatus 400 otherwise
     * 
     * @param message_id The message to be updated
     * @param body The text to replace the existing message's text
     * @return ResponseEntity<Integer> with how many rows were updated
     */
    @PatchMapping(value = "/messages/{message_id}")
    public ResponseEntity<Integer> patchMessageByID(@PathVariable int message_id, @RequestBody Message body){

        try {
            // If the function executes without exception, the message was updated properly
            msgService.updateMessageByID(message_id, body.getMessageText());
            return ResponseEntity.status(HttpStatus.OK).body(1);
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.status(400).body(null);
    }

    
    /**
     * Posts a message to the platform
     * 
     * HttpStatus 200 if the message was posted
     * HttpStatus 400 otherwise
     * 
     * @param body The message to be posted
     * @return ReponseEntity<Message> containing the message, if it was posted, null otherwise
     */
    @PostMapping(value = "/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message body){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(msgService.createMessage(body));
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return ResponseEntity.status(400).body(null);
    }

    
    /**
     * Deletes a message with the given message_id
     * 
     * HttpStatus 200 is always expected
     * 
     * @param message_id The message that should be deleted
     * @return ResponseEntity<Integer> containing the number of affected rows
     */
    @DeleteMapping(value = "/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageByID(@PathVariable int message_id){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.deleteMessageByID(message_id));
    }
}
