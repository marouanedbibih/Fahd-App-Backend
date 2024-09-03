package org.fahdpln.backend.departement;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartementRequest {
    
    @NotEmpty(message = "Departement name is required")
    private String name;

}
