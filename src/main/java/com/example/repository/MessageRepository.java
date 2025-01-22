package com.example.repository;

import com.example.entity.Message;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findMessagesByPostedBy(int postedBy);

    // Possibly won't need this
    Message findMessageByMessageId(int message_id);

}
