package org.fahdpln.backend.employee;

import java.util.List;
import java.util.Map;

import org.fahdpln.backend.departement.Departement;
import org.fahdpln.backend.departement.DepartementService;
import org.fahdpln.backend.exception.MyAlreadyExistException;
import org.fahdpln.backend.exception.MyNotFoundException;
import org.fahdpln.backend.jwt.JwtUtils;
import org.fahdpln.backend.user.User;
import org.fahdpln.backend.user.UserDTO;
import org.fahdpln.backend.user.UserRole;
import org.fahdpln.backend.user.UserService;
import org.fahdpln.backend.utils.MyErrorResponse;
import org.fahdpln.backend.utils.MyResponse;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

        private final EmployeeRepository employeeRepository;
        private final UserService userService;
        private final DepartementService departementService;
        private final JwtUtils jwtUtils;

        // Delete employee
        @Transactional
        public MyResponse deleteEmployee(Long id) throws MyNotFoundException {
                // Find the employee
                Employee employee = this.findEmployeeById(id);
                // Build the user DTO
                UserDTO userDTO = this.buildUserDTO(employee);
                // Delete the user
                userService.deleteUser(userDTO);
                // Delete the employee
                employeeRepository.delete(employee);
                // Return response
                return MyResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Employee deleted successfully")
                                .build();
        }

        // Get employee by id
        public MyResponse getEmployeeById(Long id) throws MyNotFoundException {
                Employee employee = this.findEmployeeById(id);
                EmployeeDTO employeeDTO = this.buildEmployeeDTO(employee);
                employeeDTO.setPassword("");
                return MyResponse.builder()
                                .status(HttpStatus.OK)
                                .data(employeeDTO)
                                .build();
        }

        // Update employee
        @Transactional
        public MyResponse updateEmployee(Long id, UpdateEmployeeRequest request)
                        throws MyNotFoundException, MyAlreadyExistException {
                // Find the employee
                Employee employee = this.findEmployeeById(id);
                // Find the Departement
                Departement departement = departementService.findDepartementById(request.getDepartementId());
                // Update the user
                UserDTO userDTO = this.buildUserDTOFromUpdateRequest(request);
                userDTO.setUserId(employee.getUser().getId());
                userDTO.setCreatedAt(employee.getUser().getCreatedAt());
                userDTO.setUpdatedAt(employee.getUser().getUpdatedAt());
                if (!request.getPassword().isEmpty()) {
                        userDTO.setPassword(request.getPassword());
                } else {
                        userDTO.setPassword(employee.getUser().getPassword());
                }
                User user = userService.updateUser(userDTO);
                // Update the employee
                employee.setUser(user);
                employee.setJob(request.getJob());
                employee.setDepartement(departement);
                employee.setUpdatedAt(user.getUpdatedAt());
                employee.setIsSecretary(request.getIsSecretary());
                employeeRepository.save(employee);

                // Return response
                return MyResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Employee updated successfully")
                                .data(this.buildEmployeeDTO(employee))
                                .build();
        }

        // Create a new employee
        @Transactional
        public MyResponse createEmployee(EmployeeRequest request) throws MyAlreadyExistException, MyNotFoundException {
                // Find the departement
                Departement departement = departementService.findDepartementById(request.getDepartementId());
                // Build the UserDTO
                UserDTO userDTO = this.buildUserDTOFromRequest(request);
                // Create the user
                User user = userService.createNewUser(userDTO);
                // Create the employee
                Employee employee = Employee.builder()
                                .user(user)
                                .job(request.getJob())
                                .departement(departement)
                                .isSecretary(request.getIsSecretary())
                                .build();
                employeeRepository.save(employee);
                return MyResponse.builder()
                                .status(HttpStatus.CREATED)
                                .message("Employee created successfully")
                                .build();

        }

        // Search employees by keyword with pagination
        public MyResponse searchEmployees(String keyword, int page, int size, String bearerToken) {
                // Extract user from the bearer token
                User user = jwtUtils.extractUserFromBeaererToken(bearerToken);
                // Get page of employees
                Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
                Page<Employee> employeesPage;
                if (user.getRole() == UserRole.SECRETARY) {
                        Departement departement = user.getEmployee().getDepartement();
                        employeesPage = employeeRepository.searchEmployeesInDepratement(keyword, departement, pageable);

                } else {
                        employeesPage = employeeRepository.searchEmployees(keyword, pageable);
                }

                // If no employees found
                if (employeesPage.getContent().isEmpty()) {
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .message("No employees found")
                                        .build();
                } else {
                        // Build list of employee DTO
                        List<EmployeeDTO> employeesDTO = employeesPage.getContent().stream()
                                        .map(this::buildEmployeeDTO)
                                        .toList();
                        // Meta Data
                        Map<String, Object> meta = Map.of(
                                        "totalPages", employeesPage.getTotalPages(),
                                        "totalElements", employeesPage.getTotalElements(),
                                        "currentPage", employeesPage.getNumber() + 1,
                                        "size", size);
                        // Return Response
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .data(employeesDTO)
                                        .meta(meta)
                                        .build();
                }
        }

        // Get list of employees with pagination
        public MyResponse getEmployees(int page, int size, String bearerToken) {
                User user = jwtUtils.extractUserFromBeaererToken(bearerToken);
                // Get Employees page
                Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
                Page<Employee> employeesPage;
                // Chec if the user is a secretary or admin
                if (user.getRole() == UserRole.SECRETARY) {
                        Departement departement = user.getEmployee().getDepartement();
                        employeesPage = employeeRepository.findByDepartement(departement, pageable);
                } else {
                        employeesPage = employeeRepository.findAll(pageable);
                }

                if (employeesPage.getContent().isEmpty()) {
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .message("No employees found")
                                        .build();
                } else {
                        // Build list of employee DTO
                        List<EmployeeDTO> employeesDTO = employeesPage.getContent().stream()
                                        .map(this::buildEmployeeDTO)
                                        .toList();
                        // Meta Data
                        Map<String, Object> meta = Map.of(
                                        "totalPages", employeesPage.getTotalPages(),
                                        "totalElements", employeesPage.getTotalElements(),
                                        "currentPage", employeesPage.getNumber() + 1,
                                        "size", size);
                        // Return Response
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .data(employeesDTO)
                                        .meta(meta)
                                        .build();
                }
        }

        // Build emplyee DTO
        private EmployeeDTO buildEmployeeDTO(Employee employee) {
                return EmployeeDTO.builder()
                                .employeeId(employee.getId())
                                .userId(employee.getUser().getId())
                                .username(employee.getUser().getUsername())
                                .name(employee.getUser().getName())
                                .email(employee.getUser().getEmail())
                                .phone(employee.getUser().getPhone())
                                .role(employee.getUser().getRole())
                                .job(employee.getJob())
                                .departementId(employee.getDepartement().getId())
                                .departementName(employee.getDepartement().getName())
                                .createdAt(employee.getCreatedAt())
                                .updatedAt(employee.getUpdatedAt())
                                .isSecretary(employee.getIsSecretary())
                                .build();
        }

        // Builde the UserDTO from the Employee Entity
        private UserDTO buildUserDTO(Employee employee) {
                return UserDTO.builder()
                                .userId(employee.getUser().getId())
                                .username(employee.getUser().getUsername())
                                .name(employee.getUser().getName())
                                .email(employee.getUser().getEmail())
                                .phone(employee.getUser().getPhone())
                                .password(employee.getUser().getPassword())
                                .role(employee.getUser().getRole())
                                .createdAt(employee.getUser().getCreatedAt())
                                .updatedAt(employee.getUser().getUpdatedAt())
                                .build();
        }

        // Build the UserDTO from the EmployeeRequest
        private UserDTO buildUserDTOFromRequest(EmployeeRequest request) {
                UserDTO userDTO = UserDTO.builder()
                                .username(request.getUsername())
                                .name(request.getName())
                                .email(request.getEmail())
                                .phone(request.getPhone())
                                .password(request.getPassword())
                                .build();
                if (request.getIsSecretary()) {
                        userDTO.setRole(UserRole.SECRETARY);
                } else {
                        userDTO.setRole(UserRole.EMPLOYEE);
                }
                return userDTO;
        }

        private UserDTO buildUserDTOFromUpdateRequest(UpdateEmployeeRequest request) {
                UserDTO userDTO = UserDTO.builder()
                                .username(request.getUsername())
                                .name(request.getName())
                                .email(request.getEmail())
                                .phone(request.getPhone())
                                .password(request.getPassword())
                                .build();
                if (request.getIsSecretary()) {
                        userDTO.setRole(UserRole.SECRETARY);
                } else {
                        userDTO.setRole(UserRole.EMPLOYEE);
                }
                return userDTO;
        }

        // Fins the employee by id
        private Employee findEmployeeById(Long id) throws MyNotFoundException {
                return employeeRepository.findById(id)
                                .orElseThrow(() -> new MyNotFoundException(MyErrorResponse.builder()
                                                .message("Employee not found with ID: " + id)
                                                .build()));
        }

}
