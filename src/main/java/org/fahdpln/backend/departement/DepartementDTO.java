package org.fahdpln.backend.departement;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartementDTO {
    private Long id;
    private String name;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    
}
