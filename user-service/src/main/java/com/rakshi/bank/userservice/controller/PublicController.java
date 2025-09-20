package com.rakshi.bank.userservice.controller;

import com.rakshi.bank.domains.dto.request.CreateUserRequest;
import com.rakshi.bank.userservice.service.publicService.PublicService;
import com.rakshi.bank.userservice.utils.annotations.WrapData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/users")
@RequiredArgsConstructor
@Tag(name = "Public User Operations",
        description = "Public APIs that don't require authentication")
public class PublicController {

    private final PublicService publicService;

    @PostMapping("/register")
    @WrapData(status = HttpStatus.CREATED, message = "User registered successfully")
    @Operation(
            summary = "Register a new user",
            description = """
                    Register a new user in the banking system. This endpoint:
                    - Creates a new customer account
                    - Assigns default CUSTOMER role
                    - Validates email and phone number uniqueness
                    - Encrypts the password using BCrypt
                    
                    No authentication required.
                    """,
            tags = {"Public User Operations"}
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Successful Registration",
                                    value = """
                                            {
                                                "success": true,
                                                "status": "SUCCESS",
                                                "data": {
                                                    "userId": "CUST123456789",
                                                    "firstName": "John",
                                                    "lastName": "Doe",
                                                    "email": "john.doe@example.com",
                                                    "phoneNumber": "+1234567890",
                                                    "roles": ["CUSTOMER"],
                                                    "createdAt": "2024-01-15T10:30:00",
                                                    "updatedAt": "2024-01-15T10:30:00"
                                                },
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Validation Error",
                                    value = """
                                            {
                                                "success": false,
                                                "error": "Invalid request data",
                                                "status": "BAD_REQUEST",
                                                "data": {
                                                    "email": ["Email is required", "Invalid email format"],
                                                    "phoneNumber": ["Phone number is required"],
                                                    "password": ["Password must be at least 8 characters"]
                                                },
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "User Exists",
                                    value = """
                                            {
                                                "success": false,
                                                "error": "User already exists",
                                                "status": "CONFLICT",
                                                "data": "User already exists with email john.doe@example.com",
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    public Object register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            )
            @Valid @RequestBody CreateUserRequest request) {
        return publicService.registerUser(request);
    }

}
