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
    private Long departmentId;

    // Equals Method
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserDTO))
            return false;

        UserDTO userDTO = (UserDTO) o;

        if (!getUserId().equals(userDTO.getUserId()))
            return false;
        if (!getUsername().equals(userDTO.getUsername()))
            return false;
        if (!getName().equals(userDTO.getName()))
            return false;
        if (!getEmail().equals(userDTO.getEmail()))
            return false;
        if (!getPhone().equals(userDTO.getPhone()))
            return false;
        if (!getPassword().equals(userDTO.getPassword()))
            return false;
        if (!getCreatedAt().equals(userDTO.getCreatedAt()))
            return false;
        if (!getUpdatedAt().equals(userDTO.getUpdatedAt()))
            return false;
        return getRole() == userDTO.getRole();
    }

    // HashCode Method
    @Override
    public int hashCode() {
        int result = getUserId().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getPhone().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getCreatedAt().hashCode();
        result = 31 * result + getUpdatedAt().hashCode();
        result = 31 * result + getRole().hashCode();
        return result;
    }


}
