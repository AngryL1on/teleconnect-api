package dev.angryl1on.teleconnect.mainservice.service.implementation;

import dev.angryl1on.teleconnect.mainservice.dto.request.SendMessageRequestDTO;
import dev.angryl1on.teleconnect.mainservice.exception.ChatException;
import dev.angryl1on.teleconnect.mainservice.exception.MessageException;
import dev.angryl1on.teleconnect.mainservice.exception.UserException;
import dev.angryl1on.teleconnect.mainservice.model.Chat;
import dev.angryl1on.teleconnect.mainservice.model.Message;
import dev.angryl1on.teleconnect.mainservice.model.User;
import dev.angryl1on.teleconnect.mainservice.repository.MessageRepository;
import dev.angryl1on.teleconnect.mainservice.service.ChatService;
import dev.angryl1on.teleconnect.mainservice.service.MessageService;
import dev.angryl1on.teleconnect.mainservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserService userService;
    private final ChatService chatService;
    private final MessageRepository messageRepository;

    @Override
    public Message sendMessage(SendMessageRequestDTO req, UUID userId) throws UserException, ChatException {

        User user = userService.findUserById(userId);
        Chat chat = chatService.findChatById(req.chatId());

        Message message = Message.builder()
                .chat(chat)
                .user(user)
                .content(req.content())
                .timeStamp(LocalDateTime.now())
                .readBy(new HashSet<>(Set.of(user.getId())))
                .build();

        chat.getMessages().add(message);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatMessages(UUID chatId, User reqUser) throws UserException, ChatException {

        Chat chat = chatService.findChatById(chatId);

        if (!chat.getUsers().contains(reqUser)) {
            throw new UserException("User isn't related to chat " + chatId);
        }

        return messageRepository.findByChat_Id(chat.getId());
    }

    @Override
    public Message findMessageById(UUID messageId) throws MessageException {

        Optional<Message> message = messageRepository.findById(messageId);

        if (message.isPresent()) {
            return message.get();
        }

        throw new MessageException("Message not found " + messageId);
    }

    @Override
    public void deleteMessageById(UUID messageId, User reqUser) throws UserException, MessageException {

        Message message = findMessageById(messageId);

        if (message.getUser().getId().equals(reqUser.getId())) {
            messageRepository.deleteById(messageId);
            return;
        }

        throw new UserException("User is not related to message " + message.getId());
    }

}
