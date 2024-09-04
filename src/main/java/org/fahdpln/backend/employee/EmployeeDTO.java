package org.fahdpln.backend.employee;

import java.time.LocalDate;

import org.fahdpln.backend.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends UserDTO {

    private Long employeeId;
    private String job;
    private Long departementId;
    private String departementName;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    private Boolean isSecretary;
}
