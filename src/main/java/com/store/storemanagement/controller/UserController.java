package com.store.storemanagement.controller;

import com.store.storemanagement.dto.UserDTO;
import com.store.storemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO user) {
        UserDTO savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @PostMapping("/createOperator")
    public ResponseEntity<UserDTO> createOperator(@RequestBody @Valid UserDTO user) {
        UserDTO savedUser = userService.saveOperator(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("{email}")
    @PreAuthorize("hasRole('ADMIN') or (#email == authentication.name)")
    public ResponseEntity<UserDTO> getUser(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN') or (#email == authentication.name)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String email,
                                              @RequestBody @Valid UserDTO updatedUserDTO) {
        UserDTO updatedUser = userService.updateUser(email, updatedUserDTO);
        return ResponseEntity.ok(updatedUser);

    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        if (userService.deleteUser(email)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
