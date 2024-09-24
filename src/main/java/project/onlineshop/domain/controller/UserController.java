package project.onlineshop.domain.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.service.impl.UserService;
import project.onlineshop.utils.responses.UserResponse;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    @PostMapping("")
    UserResponse createUser(@RequestBody @Valid User user){
        return new UserResponse(service.create(user));
    }
    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable UUID id){
        return new UserResponse(service.getUserById(id));
    }
    @PutMapping("/{id}")
    UserResponse updateUser(@PathVariable UUID id, @RequestBody @Valid User user){
        return new UserResponse(service.update(id, user));
    }
    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable UUID id){
        service.delete(id);
    }
    @PostMapping("/{id}")
    UserResponse addAddress(@PathVariable UUID id, @RequestBody @Valid Address address){
        service.addAddress(id, address);
        return new UserResponse(service.getUserById(id));
    }
}
