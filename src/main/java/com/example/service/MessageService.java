package com.example.service;

import com.example.repository.MessageRepository;
import com.example.entity.Message;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    MessageRepository msgRepo;

    @Autowired
    public MessageService(MessageRepository msgRepo){
        this.msgRepo = msgRepo;
    }

    public List<Message> getAllMessages(){
        return msgRepo.findAll();
    }

    public Message getMessageByID(int message_id){
        return msgRepo.findMessageByMessageId(message_id);
    }

    public List<Message> getMessagesSendByUserID(int postedBy){
        return msgRepo.findMessagesByPostedBy(postedBy);
    }

    public Message updateMessageByID(int message_id, String newMessage){

        if(newMessage.length() < 1 || newMessage.length() > 255){
            return null;
        }

        Message orgMsg = getMessageByID(message_id);

        if(orgMsg != null){
            orgMsg.setMessageText(newMessage);

            return msgRepo.save(orgMsg);
        }
        
        return null;
    }

    public Message createMessage(Message newMsg){
        String msgText = newMsg.getMessageText();

        if(msgText.length() > 0 && msgText.length() <= 255){
            return msgRepo.save(newMsg);
        }

        return null;
    }

    public Integer deleteMessageByID(int message_id){
        Message deletedMessage = getMessageByID(message_id);

        if(deletedMessage != null){
            msgRepo.delete(deletedMessage);
            return 1;
        }

        return null;
    }
}
