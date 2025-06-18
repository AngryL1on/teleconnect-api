package dev.angryl1on.teleconnect.mainservice.service;

import dev.angryl1on.teleconnect.mainservice.dto.request.SendMessageRequestDTO;
import dev.angryl1on.teleconnect.mainservice.exception.ChatException;
import dev.angryl1on.teleconnect.mainservice.exception.MessageException;
import dev.angryl1on.teleconnect.mainservice.exception.UserException;
import dev.angryl1on.teleconnect.mainservice.model.Message;
import dev.angryl1on.teleconnect.mainservice.model.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message sendMessage(SendMessageRequestDTO req, UUID userId) throws UserException, ChatException;

    List<Message> getChatMessages(UUID chatId, User reqUser) throws UserException, ChatException;

    Message findMessageById(UUID messageId) throws MessageException;

    void deleteMessageById(UUID messageId, User reqUser) throws UserException, MessageException;

}
