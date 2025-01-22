package com.example.controller;

import com.example.entity.*;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    @Autowired
    AccountService accService;
    @Autowired
    AccountRepository accRepo;
    @Autowired
    MessageService msgService;
    //@Autowired
    //MessageRepository msgRepo;

    // Account related handling
    @PostMapping(value = "/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account body){

        if(accRepo.findAccountByUsername(body.getUsername()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        try {
            accRepo.save(body);

            Account databaseObj = accRepo.findAccountByUsername(body.getUsername());

            return ResponseEntity.status(HttpStatus.OK).body(databaseObj);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return ResponseEntity.status(400).body(null);
    }

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
    @GetMapping(value = "/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.getAllMessages());
    }

    @GetMapping(value = "/messages/{message_id}")
    public ResponseEntity<Message> getMessageByID(@PathVariable int message_id){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.getMessageByID(message_id));
    }

    @GetMapping(value = "/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessageSentByUserID(@PathVariable int account_id){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.getMessagesSendByUserID(account_id));
    }

    @PatchMapping(value = "/messages/{message_id}")
    public ResponseEntity<Integer> patchMessageByID(@PathVariable int message_id, @RequestBody Message body){

        Message updatedMsg = msgService.updateMessageByID(message_id, body.getMessageText());

        if(updatedMsg != null){
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }

        return ResponseEntity.status(400).body(null);
    }

    @PostMapping(value = "/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message body){

        try {
            Message msgInDB = msgService.createMessage(body);

            if(msgInDB != null){
            return ResponseEntity.status(HttpStatus.OK).body(msgInDB);
        }
        } catch (Exception e) {
            /* This catch clause will likely be run when attemptting to 
                add a message from a non-registered user*/
            System.out.println(e.getMessage());
        }
        

        return ResponseEntity.status(400).body(null);
    }

    @DeleteMapping(value = "/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageByID(@PathVariable int message_id){
        return ResponseEntity.status(HttpStatus.OK).body(msgService.deleteMessageByID(message_id));
    }

    

}
