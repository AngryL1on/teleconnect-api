package dev.angryl1on.teleconnect.mainservice.controllers;

import dev.angryl1on.teleconnect.mainservice.config.JwtConstants;
import dev.angryl1on.teleconnect.mainservice.dto.request.SendMessageRequestDTO;
import dev.angryl1on.teleconnect.mainservice.dto.response.ApiResponseDTO;
import dev.angryl1on.teleconnect.mainservice.dto.response.MessageDTO;
import dev.angryl1on.teleconnect.mainservice.exception.ChatException;
import dev.angryl1on.teleconnect.mainservice.exception.MessageException;
import dev.angryl1on.teleconnect.mainservice.exception.UserException;
import dev.angryl1on.teleconnect.mainservice.model.Message;
import dev.angryl1on.teleconnect.mainservice.model.User;
import dev.angryl1on.teleconnect.mainservice.service.MessageService;
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
@RequestMapping("/api/messages")
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/create")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody SendMessageRequestDTO req,
                                                  @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws ChatException, UserException {

        User user = userService.findUserByProfile(jwt);
        Message message = messageService.sendMessage(req, user.getId());
        log.info("User {} sent message: {}", user.getEmail(), message.getId());

        return new ResponseEntity<>(MessageDTO.fromMessage(message), HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageDTO>> getChatMessages(@PathVariable UUID chatId,
                                                         @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws ChatException, UserException {

        User user = userService.findUserByProfile(jwt);
        List<Message> messages = messageService.getChatMessages(chatId, user);

        return new ResponseEntity<>(MessageDTO.fromMessages(messages), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteMessage(@PathVariable UUID id,
                                                        @RequestHeader(JwtConstants.TOKEN_HEADER) String jwt)
            throws UserException, MessageException {

        User user = userService.findUserByProfile(jwt);
        messageService.deleteMessageById(id, user);
        log.info("User {} deleted message: {}", user.getEmail(), id);

        ApiResponseDTO res = ApiResponseDTO.builder()
                .message("Message deleted successfully")
                .status(true)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
