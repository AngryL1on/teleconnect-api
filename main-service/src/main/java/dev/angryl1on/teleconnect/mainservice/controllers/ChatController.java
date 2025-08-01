package dev.angryl1on.teleconnect.mainservice.controllers;

import dev.angryl1on.teleconnect.mainservice.config.JwtConstants;
import dev.angryl1on.teleconnect.mainservice.dto.request.GroupChatRequestDTO;
import dev.angryl1on.teleconnect.mainservice.dto.response.ApiResponseDTO;
import dev.angryl1on.teleconnect.mainservice.dto.response.ChatDTO;
import dev.angryl1on.teleconnect.mainservice.exception.ChatException;
import dev.angryl1on.teleconnect.mainservice.exception.UserException;
import dev.angryl1on.teleconnect.mainservice.model.Chat;
import dev.angryl1on.teleconnect.mainservice.model.User;
import dev.angryl1on.teleconnect.mainservice.service.ChatService;
import dev.angryl1on.teleconnect.mainservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final UserService userService;
    private final ChatService chatService;

    @PostMapping("/single")
    public ResponseEntity<ChatDTO> createSingleChat(@RequestBody UUID userId,
                                                    @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException {

        User user = userService.findUserByProfile(jwt);
        Chat chat = chatService.createChat(user, userId);
        log.info("User {} created single chat: {}", user.getEmail(), chat.getId());

        return new ResponseEntity<>(ChatDTO.fromChat(chat), HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<ChatDTO> createGroupChat(@RequestBody GroupChatRequestDTO req,
                                                   @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException {

        User user = userService.findUserByProfile(jwt);
        Chat chat = chatService.createGroup(req, user);
        log.info("User {} created group chat: {}", user.getEmail(), chat.getId());

        return new ResponseEntity<>(ChatDTO.fromChat(chat), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatDTO> findChatById(@PathVariable("id") UUID id)
            throws ChatException {

        Chat chat = chatService.findChatById(id);

        return new ResponseEntity<>(ChatDTO.fromChat(chat), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ChatDTO>> findAllChatsByUserId(@RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException {

        User user = userService.findUserByProfile(jwt);
        List<Chat> chats = chatService.findAllByUserId(user.getId());

        return new ResponseEntity<>(ChatDTO.fromChats(chats), HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<ChatDTO> addUserToGroup(@PathVariable UUID chatId, @PathVariable UUID userId,
                                                  @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException, ChatException {

        User user = userService.findUserByProfile(jwt);
        Chat chat = chatService.addUserToGroup(userId, chatId, user);
        log.info("User {} added user {} to group chat: {}", user.getEmail(), userId, chat.getId());

        return new ResponseEntity<>(ChatDTO.fromChat(chat), HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<ChatDTO> removeUserFromGroup(@PathVariable UUID chatId, @PathVariable UUID userId,
                                                       @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException, ChatException {

        User user = userService.findUserByProfile(jwt);
        Chat chat = chatService.removeFromGroup(chatId, userId, user);
        log.info("User {} removed user {} from group chat: {}", user.getEmail(), userId, chat.getId());

        return new ResponseEntity<>(ChatDTO.fromChat(chat), HttpStatus.OK);
    }

    @PutMapping("/{chatId}/markAsRead")
    public ResponseEntity<ChatDTO> markAsRead(@PathVariable UUID chatId,
                                              @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException, ChatException {

        User user = userService.findUserByProfile(jwt);
        Chat chat = chatService.markAsRead(chatId, user);
        log.info("Chat {} marked as read for user: {}", chatId, user.getEmail());

        return new ResponseEntity<>(ChatDTO.fromChat(chat), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDTO> deleteChat(@PathVariable UUID id,
                                                     @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException, ChatException {

        User user = userService.findUserByProfile(jwt);
        chatService.deleteChat(id, user.getId());
        log.info("User {} deleted chat: {}", user.getEmail(), id);

        ApiResponseDTO res = ApiResponseDTO.builder()
                .message("Chat deleted successfully")
                .status(true)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
