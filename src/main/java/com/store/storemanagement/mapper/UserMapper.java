package com.store.storemanagement.mapper;

import com.store.storemanagement.dto.UserDTO;
import com.store.storemanagement.entity.User;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}
