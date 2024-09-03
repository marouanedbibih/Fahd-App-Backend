package org.fahdpln.backend.user;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String password;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private UserRole role;

}
