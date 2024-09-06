package org.fahdpln.backend.employee;

import java.util.Optional;

import org.fahdpln.backend.departement.Departement;
import org.fahdpln.backend.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e " +
            "JOIN e.user u " +
            "WHERE u.username LIKE %:keyword% " +
            "OR u.name LIKE %:keyword% " +
            "OR u.email LIKE %:keyword% " +
            "OR u.phone LIKE %:keyword% " +
            "OR e.job LIKE %:keyword% " +
            "OR e.departement.name LIKE %:keyword%")
    Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);

    Page<Employee> findByDepartement(Departement departement, Pageable pageable);

    @Query("SELECT e FROM Employee e " +
            "JOIN e.user u " +
            "WHERE (u.username LIKE %:keyword% " +
            "OR u.name LIKE %:keyword% " +
            "OR u.email LIKE %:keyword% " +
            "OR u.phone LIKE %:keyword% " +
            "OR e.job LIKE %:keyword%) " +
            "AND e.departement = :departement")
    Page<Employee> searchEmployeesInDepratement(String keyword, Departement departement, Pageable pageable);

    Optional<Employee> findByUser(User user);

}
