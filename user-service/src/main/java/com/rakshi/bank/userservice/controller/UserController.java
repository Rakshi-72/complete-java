package com.rakshi.bank.userservice.controller;

import com.rakshi.bank.userservice.service.userService.UserService;
import com.rakshi.bank.userservice.utils.annotations.WrapData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management",
        description = "APIs for user management operations including user lookup and profile management")
public class UserController {

    private final UserService userService;

    @GetMapping("/{identity}")
    @WrapData(message = "user found")
    @Operation(
            summary = "Find user by identity",
            description = """
                    Find a user by their identity which can be one of:
                    - Email address (e.g., user@example.com)
                    - Phone number (e.g., +1234567890)
                    - Customer ID (e.g., CUST123456789)
                    
                    Access control:
                    - Admin users can access any user's information
                    - Regular users can only access their own information
                    """,
            tags = {"User Management"}
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Successful Response",
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
                    description = "Invalid identity format",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Invalid Identity",
                                    value = """
                                            {
                                                "success": false,
                                                "error": "Find by identity failed",
                                                "status": "BAD_REQUEST",
                                                "data": "Invalid identity invalid@format",
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied - insufficient permissions",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Access Denied",
                                    value = """
                                            {
                                                "success": false,
                                                "error": "Find by identity failed",
                                                "status": "UNAUTHORIZED",
                                                "data": "Permission denied",
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "User Not Found",
                                    value = """
                                            {
                                                "success": false,
                                                "error": "Find by identity failed",
                                                "status": "NOT_FOUND",
                                                "data": "User not found with email user@example.com",
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    public Object findByIdentity(
            @Parameter(
                    description = """
                            User identity - can be one of:
                            - Email: user@example.com
                            - Phone: +1234567890 (E.164 format)
                            - Customer ID: CUST123456789
                            """,
                    example = "user@example.com",
                    required = true
            )
            @PathVariable String identity
    ) {
        return userService.findByIdentity(identity);
    }

}
