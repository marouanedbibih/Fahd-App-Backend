package org.fahdpln.backend.schedule;

import org.fahdpln.backend.schedule.dtos.ScheduleRequest;
import org.fahdpln.backend.utils.MyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // Endpoint to create a new schedule
    @PostMapping("/api/v1/employee/{employeeId}/schedules")
    public ResponseEntity<MyResponse> createSchedule(@RequestBody @Valid ScheduleRequest request,
            @PathVariable Long employeeId) {
        MyResponse response = scheduleService.createSchedule(request, employeeId);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

    // Get all schedules for an employee
    @GetMapping("/api/v1/employee/{employeeId}/schedules")
    public ResponseEntity<MyResponse> getEmployeeSchedules(@PathVariable Long employeeId) {
        MyResponse response = scheduleService.getAllSchedules(employeeId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Delete all schedules for an employee
    @DeleteMapping("/api/v1/employee/{employeeId}/schedules")
    public ResponseEntity<MyResponse> deleteAllSchedules(@PathVariable Long employeeId) {
        MyResponse response = scheduleService.deleteAllSchedules(employeeId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
