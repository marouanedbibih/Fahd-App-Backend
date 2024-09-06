package org.fahdpln.backend.employee;

import org.fahdpln.backend.departement.Departement;
import org.fahdpln.backend.schedule.Schedule;
import org.fahdpln.backend.user.User;
import org.fahdpln.backend.utils.BasicEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends BasicEntity {

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @ToString.Exclude
    private User user;

    private String job;

    @Column(name = "is_secretary")
    private Boolean isSecretary;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    // Schedule
    @OneToMany(mappedBy = "employee")
    private List<Schedule> schedules;

}
