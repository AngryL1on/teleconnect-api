package dev.angryl1on.teleconnect.mainservice.service;

import dev.angryl1on.teleconnect.mainservice.dto.request.UpdateUserRequestDTO;
import dev.angryl1on.teleconnect.mainservice.exception.UserException;
import dev.angryl1on.teleconnect.mainservice.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findUserById(UUID id) throws UserException;

    User findUserByProfile(String jwt) throws UserException;

    User updateUser(UUID id, UpdateUserRequestDTO request) throws UserException;

    List<User> searchUser(String query);

    List<User> searchUserByName(String name);

}
