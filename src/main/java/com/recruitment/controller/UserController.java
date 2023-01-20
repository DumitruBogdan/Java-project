package com.recruitment.controller;

import com.recruitment.dto.UserDTO;
import com.recruitment.exception.handler.ErrorResponse;
import com.recruitment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Get all users", responses = {
            @ApiResponse(description = "Received all users", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user", responses = {
            @ApiResponse(description = "Found user", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "User not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public UserDTO getUserById(@Parameter(description = "ID of the user we want to get")
                               @PathVariable(value = "id") Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Get assigned candidates", responses = {
            @ApiResponse(description = "Found assigned candidates", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "Assugned candidates not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/assigned/{id}")
    public List<Long> getAssignedCandidates(@Parameter(description = "ID of the user that wants to get the assigned candidates")
                                            @PathVariable(value = "id") Long userId) {
        return userService.getAssignedCandidates(userId);
    }

    @Operation(summary = "Create user", responses = {
            @ApiResponse(description = "Created user", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "User already exists", responseCode = "409",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public UserDTO createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User structure")
                              @RequestBody UserDTO createUser) {
        return userService.create(createUser);
    }

    @Operation(summary = "Update user", responses = {
            @ApiResponse(description = "Updated user", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "User not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public UserDTO updateUser(@Parameter(description = "ID of the user we want to update")
                              @PathVariable("id") Long userId,
                              @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User structure")
                              @RequestBody UserDTO updateUser) {
        updateUser.setId(userId);
        return userService.update(updateUser);
    }

    @Operation(summary = "Delete user", responses = {
            @ApiResponse(description = "Deleted user", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(description = "User not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@Parameter(description = "ID of the user we want to delete")
                           @PathVariable("id") Long userId) {
        userService.deleteUser(userId);
    }
}
