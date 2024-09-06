package org.fahdpln.backend.employee;

import org.fahdpln.backend.utils.MyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Delete employee
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SECRETARY')")
    @DeleteMapping("/api/v1/employee/{id}")
    public ResponseEntity<MyResponse> deleteEmployee(@PathVariable Long id) {
        MyResponse response = employeeService.deleteEmployee(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Update employee
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SECRETARY')")
    @PutMapping("/api/v1/employee/{id}")
    public ResponseEntity<MyResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        MyResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Create a new employee
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SECRETARY')")
    @PostMapping("/api/v1/employee")
    public ResponseEntity<MyResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        MyResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to get employee by id
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SECRETARY')")
    @GetMapping("/api/v1/employee/{id}")
    public ResponseEntity<MyResponse> getEmployeeById(@PathVariable Long id) {
        MyResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to search employee by keyword with pagination
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SECRETARY')")
    @GetMapping("/api/v1/employees/search")
    public ResponseEntity<MyResponse> searchEmployees(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam String keyword,
            @RequestHeader("Authorization") String bearerToken) {

        MyResponse response = employeeService.searchEmployees(keyword, page, size, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to get list of employees with pagination
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SECRETARY')")
    @GetMapping("/api/v1/employees")
    public ResponseEntity<MyResponse> getEmployees(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestHeader("Authorization") String bearerToken) {

        MyResponse response = employeeService.getEmployees(page, size, bearerToken);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
