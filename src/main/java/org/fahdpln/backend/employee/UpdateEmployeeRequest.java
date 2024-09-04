package org.fahdpln.backend.employee;

import org.fahdpln.backend.user.UpdateUserRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UpdateEmployeeRequest extends UpdateUserRequest {

    @NotBlank(message = "Job is required")
    private String job;

    @NotNull(message = "Department ID is required")
    private Long departementId;

    @NotNull(message = "Secretary status is required")
    private Boolean isSecretary;
}
