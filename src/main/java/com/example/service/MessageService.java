package com.example.service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    MessageRepository msgRepo;
    AccountRepository accRepo;

    @Autowired
    public MessageService(MessageRepository msgRepo, AccountRepository accRepo){
        this.msgRepo = msgRepo;
        this.accRepo = accRepo;
    }

    /**
     * Gets all messages from the database
     * 
     * @return A List object containing every message in the database
     */
    public List<Message> getAllMessages(){
        return msgRepo.findAll();
    }

    /**
     * Retrieves a message with the specified message_id
     * 
     * @param message_id The message to retrieve
     * @return The Message object associated with the message_id or null otherwise
     */
    public Message getMessageByID(int message_id){
        return msgRepo.findMessageByMessageId(message_id);
    }

    /**
     * Retrieves all messages posted by a give user
     * 
     * @param postedBy The user ID to retrieve messages from
     * @return A List object containing all messages sent by this user
     */
    public List<Message> getMessagesSendByUserID(int postedBy){
        return msgRepo.findMessagesByPostedBy(postedBy);
    }

    /**
     * Updates a message that has the message_id
     * 
     * @param message_id The message to be updated
     * @param newMessage The new text that the message should display
     * @throws IllegalArgumentException If newMessage is blank or has more than 255 characters
     * @throws SQLException If the message doesn't already exist in the database
     */
    public void updateMessageByID(int message_id, String newMessage) throws IllegalArgumentException, SQLException{

        if(newMessage.length() < 1 || newMessage.length() > 255){
            throw new IllegalArgumentException("Message text must not be blank and not exceed 255 characters.");
        }

        Message orgMsg = getMessageByID(message_id);

        if(orgMsg == null){
            throw new SQLException("Message with message_id " + message_id + " not found.");
        }

        orgMsg.setMessageText(newMessage);
        msgRepo.save(orgMsg);
    }

    /**
     * Inserts the message inside the database, if it is valid
     * 
     * 
     * @param newMsg The Message object to be inserted
     * @return The Message object that was inserted into the database or null otherwise
     * 
     * @throws IllegalArgumentException If message_text is blank or has over 255 characaters
     */
    public Message createMessage(Message newMsg) throws IllegalArgumentException{
        String msgText = newMsg.getMessageText();

        if(msgText.length() <= 0 || msgText.length() > 255){
            throw new IllegalArgumentException("Message Text must not be blank and must not exceed 255 characters.");
        }

        return msgRepo.save(newMsg);
    }

    /**
     * Deletes the message with the given message_id
     * 
     * @param message_id The message to be deleted
     * @return The number of rows affected or null if no rows were affected.
     */
    public Integer deleteMessageByID(int message_id){
        Message deletedMessage = getMessageByID(message_id);

        if(deletedMessage != null){
            msgRepo.delete(deletedMessage);
            return 1;
        }

        return null;
    }
}
