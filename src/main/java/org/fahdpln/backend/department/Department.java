package org.fahdpln.backend.department;

import java.util.List;

import org.fahdpln.backend.employee.Employee;
import org.fahdpln.backend.utils.BasicEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "departments")
public class Department extends BasicEntity {

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
