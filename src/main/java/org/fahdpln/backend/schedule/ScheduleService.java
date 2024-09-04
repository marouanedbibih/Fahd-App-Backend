package org.fahdpln.backend.schedule;

import org.fahdpln.backend.employee.Employee;
import org.fahdpln.backend.employee.EmployeeRepository;
import org.fahdpln.backend.exception.MyNotFoundException;
import org.fahdpln.backend.schedule.dtos.ScheduleDTO;
import org.fahdpln.backend.schedule.dtos.ScheduleRequest;
import org.fahdpln.backend.schedule.dtos.ScheduleTime;
import org.fahdpln.backend.utils.MyErrorResponse;
import org.fahdpln.backend.utils.MyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;

    // Service to create a new schedule
    @Transactional
    public MyResponse createSchedule(ScheduleRequest request, Long employeeId) throws MyNotFoundException {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new MyNotFoundException(MyErrorResponse.builder()
                        .message("Employee not found wtih ID:" + employeeId).build()));

        // Check if the employee already has schedules
        if (employee.getSchedules() != null && !employee.getSchedules().isEmpty()) {
            // Delete all schedules for the employee
            scheduleRepository.deleteAllByEmployee(employee);
        }
        // Create new schedules
        request.getSchedule().forEach((weekday, scheduleTime) -> {
            Schedule schedule = Schedule.builder()
                    .employee(employee)
                    .weekday(weekday)
                    .startHour(scheduleTime.getStartHour())
                    .endHour(scheduleTime.getEndHour())
                    .build();
            scheduleRepository.save(schedule);
        });

        return MyResponse.builder()
                .message("Schedule created successfully")
                .status(HttpStatus.CREATED)
                .build();
    }

    // Service to delete all schedules for an employee
    @Transactional
    public MyResponse deleteAllSchedules(Long employeeId) throws MyNotFoundException {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new MyNotFoundException(MyErrorResponse.builder()
                        .message("Employee not found wtih ID:" + employeeId).build()));

        // Delete all schedules for the employee
        scheduleRepository.deleteAllByEmployee(employee);

        return MyResponse.builder()
                .message("Schedules deleted successfully")
                .status(HttpStatus.OK)
                .build();
    }

    // Service to get all schedules for an employee
    public MyResponse getAllSchedules(Long employeeId) throws MyNotFoundException {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new MyNotFoundException(MyErrorResponse.builder()
                        .message("Employee not found wtih ID:" + employeeId).build()));
        // Build a list of ScheduleDTOs from the employee's schedules
        List<ScheduleDTO> scheduleDTOs = employee.getSchedules() == null ? List.of()
                : employee.getSchedules().stream()
                        .map(this::buildScheduleDTO)
                        .collect(Collectors.toList());

        return MyResponse.builder()
                .data(scheduleDTOs)
                .message("Schedules retrieved successfully")
                .status(HttpStatus.OK)
                .build();
    }

    // Build a ScheduleDTO from a Schedule entity
    public ScheduleDTO buildScheduleDTO(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        // Create a ScheduleEntry for each schedule
        return ScheduleDTO.builder()
                .id(schedule.getId())
                .weekday(schedule.getWeekday())
                .scheduleTime(ScheduleTime.builder()
                        .startHour(schedule.getStartHour())
                        .endHour(schedule.getEndHour())
                        .build())
                .build();
    }

}
