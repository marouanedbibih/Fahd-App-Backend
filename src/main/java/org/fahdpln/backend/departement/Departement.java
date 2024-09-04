package org.fahdpln.backend.departement;

import java.util.List;

import org.fahdpln.backend.employee.Employee;
import org.fahdpln.backend.utils.BasicEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "departments")
@AllArgsConstructor
@NoArgsConstructor
public class Departement extends BasicEntity {

    private String name;

    @OneToMany(mappedBy = "departement")
    private List<Employee> employees;


}
