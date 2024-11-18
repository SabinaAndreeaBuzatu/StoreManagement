package com.store.storemanagement.service;

import com.store.storemanagement.dto.UserDTO;
import com.store.storemanagement.entity.Role;
import com.store.storemanagement.entity.User;
import com.store.storemanagement.exception.UserNotFoundException;
import com.store.storemanagement.mapper.UserMapper;
import com.store.storemanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    public UserDTO saveUser(UserDTO userDTO) {
        if (userRepository.existsById(userDTO.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    public UserDTO saveOperator(UserDTO userDTO) {
        if (userRepository.existsById(userDTO.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists.");
        }
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.OPERATOR);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findById(email)
                .map(userMapper::userToUserDTO)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
    }

    public UserDTO updateUser(String email, UserDTO updatedUserDTO) {
        User existingUser = userRepository.findById(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        existingUser.setUsername(updatedUserDTO.getUsername());
        existingUser.setPassword(new BCryptPasswordEncoder().encode(updatedUserDTO.getPassword()));

        User savedUser = userRepository.save(existingUser);

        return new UserDTO(savedUser.getEmail(), savedUser.getUsername(), savedUser.getRole().toString());
    }

    public boolean deleteUser(String email) {
        if (userRepository.existsById(email)) {
            userRepository.deleteById(email);
            return true;
        }
        return false;
    }

}
